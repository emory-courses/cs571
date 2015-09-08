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
import edu.emory.mathcs.nlp.common.util.BinUtils;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.component.util.NLPConfig;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSTrain extends NLPTrain<POSNode>
{
	protected String config_file;
	Set<String> train_word_set;
	
	public POSTrain(String[] args)
	{
		
	}
	
	
	public void train() throws IOException
	{
//		List<String> trainFiles   = FileUtils.getFileList(train_path  , train_ext);
//		List<String> developFiles = FileUtils.getFileList(develop_path, develop_ext);
//		StringModel model = new StringModel(new MultinomialWeightVector());
//		POSTagger<POSNode> tagger = new POSTagger<>(NLPFlag.TRAIN, model);
//		Eval eval = new AccuracyEval();
		
		
	}
	
	@Override
	public void collect(List<String> inputFiles, NLPComponent<POSNode> component, NLPConfig<POSNode> configuration)
	{
		POSTagger<POSNode> tagger = (POSTagger<POSNode>)component;
		POSConfig config = (POSConfig)configuration;
		
		DocumentFrequencyMap df = new DocumentFrequencyMap(config.getDocumentSize());
		AmbiguityClassMap    ac = new AmbiguityClassMap();
		
		iterate(inputFiles, nodes -> collect(nodes, df, ac));
		Set<String> set = df.create(config.getDocumentFrequencyCutoff());
		ac.expand(config.getAmbiguityClassThreshold());
		
		tagger.setTrainWordSet(set);
		tagger.setAmbiguityClassMap(ac);
		
		BinUtils.LOG.info(String.format("- # of ambiguity classes: %d\n", ac.size()));
		BinUtils.LOG.info(String.format("- # of training words: %d\n", set.size()));
	}
	
	private void collect(POSNode[] nodes, DocumentFrequencyMap df, AmbiguityClassMap ac)
	{
		df.add(nodes);
		ac.add(nodes);
	}
}
