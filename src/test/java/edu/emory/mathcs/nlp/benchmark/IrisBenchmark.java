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
package edu.emory.mathcs.nlp.benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.common.util.Splitter;
import edu.emory.mathcs.nlp.deeplearning.network.FeedForwardNeuralNetwork;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.SparseVector;
import edu.emory.mathcs.nlp.learn.vector.Vector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class IrisBenchmark
{
	@Test
	public void test() throws IOException
	{
		String filename = "src/test/resources/dat/iris.data";
		BufferedReader reader = IOUtils.createBufferedReader(filename);
		Map<String,List<Vector>> map = new HashMap<>();
		List<Instance> trn = new ArrayList<>();
		List<Instance> tst = new ArrayList<>();
		int i, label, len, inputSize = 4;
		double ratio = 0.8;
		List<Vector> list;
		SparseVector x;
		String line;
		String[] t;
		
		while ((line = reader.readLine()) != null)
		{
			t = Splitter.splitCommas(line);
			x = new SparseVector();
			
			for (i=0; i<inputSize; i++)
				x.add(i, Float.parseFloat(t[i]));

			map.computeIfAbsent(t[inputSize], k -> new ArrayList<>()).add(x);
		}
		
		label = 0;
		
		for (Entry<String,List<Vector>> e : map.entrySet())
		{
			list = e.getValue();
			len  = (int)(ratio * list.size());
			
			for (i=0; i<len; i++)
				trn.add(new Instance(label, list.get(i)));
			
			for (; i<list.size(); i++)
				tst.add(new Instance(label, list.get(i)));
			
			label++;
		}
		
		FeedForwardNeuralNetwork nn = new FeedForwardNeuralNetwork(0.01, inputSize, map.size(), 5);
//		WeightVector w = new MultinomialWeightVector(map.size(), inputSize);
//		StochasticGradientDescent nn = new AdaGrad(w, false, 0.01);
		int correct, total = tst.size(), b = 0;
		double best = 0;
		
		for (i=0; i<100; i++)
		{
			nn.train(trn);
			correct = 0;
			
			for (Instance inst : tst)
			{
				if (inst.isLabel(nn.predictBest(inst.getVector()).getLabel()))
//				if (inst.isLabel(w.predictBest(inst.getVector()).getLabel()))
					correct++;
			}
			
			if (best < MathUtils.accuracy(correct, total))
			{
				best = MathUtils.accuracy(correct, total);
				b = i;
			}
			
			System.out.printf("%5.2f (%d/%d)\n", MathUtils.accuracy(correct, total), correct, total);
		}
		
		System.out.println(b+": "+best);
	}
}
