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
package edu.emory.mathcs.nlp.benchmark.pos;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

import edu.emory.mathcs.nlp.common.collection.tuple.DoubleIntPair;
import edu.emory.mathcs.nlp.common.util.FileUtils;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.dep.DEPNode;
import edu.emory.mathcs.nlp.component.pos.AmbiguityClassMap;
import edu.emory.mathcs.nlp.component.pos.POSIndex;
import edu.emory.mathcs.nlp.component.pos.POSNode;
import edu.emory.mathcs.nlp.component.pos.POSState;
import edu.emory.mathcs.nlp.component.pos.POSTagger;
import edu.emory.mathcs.nlp.component.pos.feature.POSFeatureTemplate0;
import edu.emory.mathcs.nlp.component.util.NLPFlag;
import edu.emory.mathcs.nlp.component.util.eval.AccuracyEval;
import edu.emory.mathcs.nlp.component.util.eval.Eval;
import edu.emory.mathcs.nlp.component.util.feature.FeatureTemplate;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.optimization.OnlineOptimizer;
import edu.emory.mathcs.nlp.learn.optimization.sgd.AdaGrad;
import edu.emory.mathcs.nlp.learn.optimization.sgd.Perceptron;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSBenchmark
{
	@Test
	public void baseline() throws IOException
	{
		run(new POSFeatureTemplate0(), 1, true);
		
		DEPNode[] nodes = null;
		AmbiguityClassMap map = new AmbiguityClassMap();
		map.add(nodes);
	}
	
	public void run(FeatureTemplate<POSNode,POSState<POSNode>> template, int type, boolean average) throws IOException
	{
		final String root = "/Users/jdchoi/Documents/Data/experiments/wsj/wsj-pos/";
		TSVReader<POSNode> reader = new TSVReader<>(new POSIndex(0,1));
		List<String> trnFiles = FileUtils.getFileList(root+"trn/", "pos");
		List<String> devFiles = FileUtils.getFileList(root+"dev/", "pos");
		Collections.sort(trnFiles);
		Collections.sort(devFiles);
		
		// collect ambiguity classes
		AmbiguityClassMap map = new AmbiguityClassMap();
		iterate(reader, trnFiles, nodes -> map.add(nodes));
		map.expand(0.4);
		
		// collect training instances
		StringModel model = new StringModel(new MultinomialWeightVector());
		POSTagger<POSNode> tagger = new POSTagger<>(model);
		tagger.setFlag(NLPFlag.TRAIN);
		tagger.setAmbiguityClassMap(map);
		tagger.setFeatureTemplate(template);
		iterate(reader, trnFiles, nodes -> tagger.process(nodes));
		
		// map string into a vector space
		final int label_cutoff   = 0;
		final int feature_cutoff = 0;
		model.vectorize(label_cutoff, feature_cutoff, false);
		
		// train the statistical model
		final double learning_rate = 0.02;
		final int epochs = 200;
		
		WeightVector weight = model.getWeightVector();
		OnlineOptimizer sgd = null;
		
		switch (type)
		{
		case 0: sgd = new Perceptron(weight, average, learning_rate); break;
		case 1: sgd = new AdaGrad   (weight, average, learning_rate); break;
		}
		
		Eval eval = new AccuracyEval();
		tagger.setFlag(NLPFlag.EVALUATE);
		tagger.setEval(eval);

		DoubleIntPair best = new DoubleIntPair(-1, -1);
		double currScore;
		
		for (int i=0; i<epochs; i++)
		{
			sgd.train(model.getInstanceList());
			eval.clear();
			iterate(reader, devFiles, nodes -> tagger.process(nodes));
			currScore = eval.score();
			System.out.printf("%4d: %5.2f\n", i, currScore);
			if (best.d < currScore) best.set(currScore, i);
		}
		
		System.out.printf("Best: %d - %5.2f\n", best.i, best.d);
	}
	
	void iterate(TSVReader<POSNode> reader, List<String> filenames, Consumer<POSNode[]> f) throws IOException
	{
		for (String filename : filenames)
		{
			reader.open(IOUtils.createFileInputStream(filename));
			POSNode[] nodes;
			
			while ((nodes = reader.next()) != null)
				f.accept(nodes);
			
			reader.close();	
		}
	}
}
