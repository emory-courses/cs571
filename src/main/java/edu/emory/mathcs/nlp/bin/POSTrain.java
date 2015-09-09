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
package edu.emory.mathcs.nlp.bin;

import java.util.List;
import java.util.Set;

import edu.emory.mathcs.nlp.common.util.BinUtils;
import edu.emory.mathcs.nlp.common.util.FileUtils;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.pos.AmbiguityClassMap;
import edu.emory.mathcs.nlp.component.pos.DocumentFrequencyMap;
import edu.emory.mathcs.nlp.component.pos.POSConfig;
import edu.emory.mathcs.nlp.component.pos.POSNode;
import edu.emory.mathcs.nlp.component.pos.POSState;
import edu.emory.mathcs.nlp.component.pos.POSTagger;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.component.util.NLPConfig;
import edu.emory.mathcs.nlp.component.util.NLPTrain;
import edu.emory.mathcs.nlp.component.util.eval.AccuracyEval;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSTrain extends NLPTrain<POSNode,String,POSState<POSNode>>
{
	public POSTrain(String[] args)
	{
		super(args);
	}
	
	public void train()
	{
		NLPConfig<POSNode> configuration = new POSConfig(IOUtils.createFileInputStream(configuration_file));
		List<String>       trainFiles    = FileUtils.getFileList(train_path  , train_ext);
		List<String>       developFiles  = FileUtils.getFileList(develop_path, develop_ext);
		TSVReader<POSNode> reader        = configuration.getTSVReader();
		
		StringModel model = new StringModel(new MultinomialWeightVector());
		POSTagger<POSNode> tagger = new POSTagger<>(model);
		tagger.setEval(new AccuracyEval());

		train(reader, trainFiles, developFiles, tagger, configuration);
	}
	
	@Override
	public void collect(TSVReader<POSNode> reader, List<String> inputFiles, NLPComponent<POSNode,String,POSState<POSNode>> component, NLPConfig<POSNode> configuration)
	{
		POSTagger<POSNode> tagger = (POSTagger<POSNode>)component;
		POSConfig config = (POSConfig)configuration;
		
		DocumentFrequencyMap df = new DocumentFrequencyMap(config.getDocumentSize());
		AmbiguityClassMap    ac = new AmbiguityClassMap();
		
		iterate(reader, inputFiles, nodes -> collect(nodes, df, ac));
		Set<String> set = df.create(config.getDocumentFrequencyCutoff());
		ac.expand(config.getAmbiguityClassThreshold());
		
		tagger.setTrainWordSet(set);
		tagger.setAmbiguityClassMap(ac);
		
		BinUtils.LOG.info(String.format("- # of ambiguity classes  : %d\n", ac.size()));
		BinUtils.LOG.info(String.format("- # of training word types: %d\n", set.size()));
	}
	
	private void collect(POSNode[] nodes, DocumentFrequencyMap df, AmbiguityClassMap ac)
	{
		df.add(nodes);
		ac.add(nodes);
	}
	
	static public void main(String[] args)
	{
		new POSTrain(args).train();
	}
}
