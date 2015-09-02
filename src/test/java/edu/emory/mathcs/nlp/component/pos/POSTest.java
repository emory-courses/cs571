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

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.emory.mathcs.nlp.component.util.AccuracyEval;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;
import edu.emory.mathcs.nlp.learn.sgd.perceptron.MultinomialPerceptron;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.util.IOUtils;
import edu.emory.mathcs.nlp.util.Splitter;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSTest
{
	@Test
	public void test() throws Exception
	{
		POSTagger<POSNode> tagger = new POSTagger<>(0);
		int sc = 0;
		List<POSNode> nodes;
		
		String filename = "/Users/jdchoi/Desktop/tmp/wsj/pos/wsj-pos";
		BufferedReader in = IOUtils.createBufferedReader(filename+".trn");
		
		while ((nodes = readNext(in)) != null)
		{
			tagger.process(nodes);
			sc++;
		}
		
		System.out.println("Training sentences: "+sc);
		
		StringModel model = tagger.getModel();
		List<Instance> instances = model.vectorize(2, 2);
		StochasticGradientDescent sgd = new MultinomialPerceptron(model.getWeightVector(), false, 0.01f);
		AccuracyEval eval = new AccuracyEval();
		tagger.setEval(eval);
		
		for (int i=0; i<50; i++)
		{
			sgd.train(instances);
			eval.clear();
			in = IOUtils.createBufferedReader(filename+".dev");
		
			while ((nodes = readNext(in)) != null)
				tagger.process(nodes);
			
			System.out.printf("%3d: %5.2f\n", i, eval.accuracy());
		}
	}
	
	List<POSNode> readNext(BufferedReader reader) throws Exception
	{
		List<POSNode> list = new ArrayList<>();
		String line;
		String[] t;
		
		while ((line = reader.readLine()) != null)
		{
			if ((line = line.trim()).isEmpty())
			{
				if (list.isEmpty()) continue;
				break;
			}
			
			t = Splitter.splitTabs(line);
			list.add(new POSNode(t[0], t[1]));
		}
		
		return list.isEmpty() ? null : list;
	}
}
