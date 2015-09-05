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
import java.util.function.Consumer;

import org.junit.Test;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.eval.AccuracyEval;
import edu.emory.mathcs.nlp.component.eval.Eval;
import edu.emory.mathcs.nlp.component.util.NLPFlag;
import edu.emory.mathcs.nlp.component.util.TSVReader;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;
import edu.emory.mathcs.nlp.learn.sgd.adagrad.MultinomialAdaGradHinge;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSDevelop
{
	@Test
	public void develop() throws IOException
	{
		final String  root = "/Users/jdchoi/Emory/webpage/public_html/courses/cs571/dat/";
		final String  train_file   = root+"wsj-pos.trn";
		final String  develop_file = root+"wsj-pos.dev";
		final boolean average = false;
		final double  ambiguity_class_threshold = 0.4;
		final double  learning_rate = 0.02;
		final double  ridge = 0.1;
		final int     epochs = 100;
		final int     label_cutoff   = 2;
		final int     feature_cutoff = 2;
		
		TSVReader<POSNode> reader = new TSVReader<>(new POSIndex(0,1));
		
		// collect ambiguity classes from the training data
		System.out.println("Collecting ambiguity classes");
		AmbiguityClassMap ambi = new AmbiguityClassMap();
		iterate(reader, train_file, nodes -> ambi.addAll(nodes));
		ambi.expand(ambiguity_class_threshold);
		
		// collect training instances from the training data
		System.out.println("Collecting training instances");
		StringModel model = new StringModel(new MultinomialWeightVector());
		POSTagger<POSNode> tagger = new POSTagger<>(NLPFlag.TRAIN, model);
		tagger.setAmbiguityClassMap(ambi);
		iterate(reader, train_file, nodes -> tagger.process(nodes));
		model.vectorize(label_cutoff, feature_cutoff);
		
		// train the statistical model using the development data
		StochasticGradientDescent sgd = new MultinomialAdaGradHinge(model.getWeightVector(), average, learning_rate, ridge);
		Eval eval = new AccuracyEval();
		tagger.setFlag(NLPFlag.EVALUATE);
		tagger.setEval(eval);
		
		for (int i=0; i<epochs; i++)
		{
			sgd.train(model.getInstanceList());
			eval.clear();
			iterate(reader, develop_file, nodes -> tagger.process(nodes));
			System.out.printf("%3d: %5.2f\n", i, eval.score());
		}
	}
	
	void iterate(TSVReader<POSNode> reader, String filename, Consumer<POSNode[]> f) throws IOException
	{
		reader.open(IOUtils.createFileInputStream(filename));
		POSNode[] nodes;
		
		while ((nodes = reader.next()) != null)
			f.accept(nodes);
		
		reader.close();
	}
}
