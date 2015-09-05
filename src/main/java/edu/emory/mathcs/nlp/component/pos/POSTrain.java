/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.nlp.component.pos;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import edu.emory.mathcs.nlp.bin.NLPTrain;
import edu.emory.mathcs.nlp.common.util.FileUtils;
import edu.emory.mathcs.nlp.component.eval.AccuracyEval;
import edu.emory.mathcs.nlp.component.eval.Eval;
import edu.emory.mathcs.nlp.component.util.NLPFlag;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSTrain extends NLPTrain<POSNode>
{
	protected String config_file;
	
	int document_frequency_cutoff = 2;
	int document_size = 1500;
	double ambiguity_class_threshold = 0.4;
	DocumentFrequencyMap df_map;
	AmbiguityClassMap    ac_map;
	Set<String>          lswf_set;
	
	
	public void train(POSConfig config) throws IOException
	{
		List<String> trainFiles   = FileUtils.getFileList(train_path  , train_ext);
		List<String> developFiles = FileUtils.getFileList(develop_path, develop_ext);
		StringModel model = new StringModel(new MultinomialWeightVector());
		POSTagger<POSNode> tagger = new POSTagger<>(NLPFlag.TRAIN, model);
		Eval eval = new AccuracyEval();
		
		collect(trainFiles);
		iterate(trainFiles, nodes -> tagger.process(nodes));
		
		StochasticGradientDescent sgd = config.getTrainer(model);
		tagger.setFlag(NLPFlag.EVALUATE);
		tagger.setEval(eval);
		
		for (int i=0; i<50; i++)
		{
			sgd.train(model.getInstanceList());
			eval.clear();
			iterate(developFiles, nodes -> tagger.process(nodes));
			System.out.printf("%3d: %5.2f\n", i, eval.score());
		}
	}
	
	public void collect(List<String> inputFiles) throws IOException
	{
		iterate(inputFiles, nodes -> collect(nodes));
		lswf_set = df_map.create(document_frequency_cutoff);
		ac_map.expand(ambiguity_class_threshold);
	}
	
	private void collect(POSNode[] nodes)
	{
		df_map.add(nodes);
		for (POSNode node : nodes) ac_map.add(node);
	}
}
