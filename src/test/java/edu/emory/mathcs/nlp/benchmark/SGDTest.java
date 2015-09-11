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

import java.io.FileInputStream;
import java.util.List;

import edu.emory.mathcs.nlp.common.collection.tuple.DoubleIntPair;
import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;
import edu.emory.mathcs.nlp.learn.sgd.adagrad.BinomialAdaGrad;
import edu.emory.mathcs.nlp.learn.sgd.adagrad.MultinomialAdaGrad;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.util.LibSVMReader;
import edu.emory.mathcs.nlp.learn.weight.BinomialWeightVector;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SGDTest
{
//	@Test
	public void testBinomialPerceptron() throws Exception
	{
		String filename = "src/test/resources/edu/emory/mathcs/nlp/dat/a9a";
		
		LibSVMReader trn = new LibSVMReader(new FileInputStream(filename+".trn"));
		LibSVMReader tst = new LibSVMReader(new FileInputStream(filename+".tst"));
		
		WeightVector w = new BinomialWeightVector(trn.featureSize());
		StochasticGradientDescent sgd = new BinomialAdaGrad(w, true, 0.01f);
		DoubleIntPair max = new DoubleIntPair(0,0);
		double acc;
		
		for (int i=0; i<100; i++)
		{
			sgd.train(trn.getInstanceList(), 1);
			acc = getAccuracy(tst.getInstanceList(), w);
			if (acc > max.d) max.set(acc, i);
			System.out.printf("%d: %5.2f\n", i, acc);
		}

		System.out.printf("%d: %5.2f\n", max.i, max.d);	
	}
	
//	@Test
	public void testMultinomialPerceptron() throws Exception
	{
		String filename = "src/test/resources/edu/emory/mathcs/nlp/dat/news20";
		
		LibSVMReader trn = new LibSVMReader(new FileInputStream(filename+".trn"));
		LibSVMReader tst = new LibSVMReader(new FileInputStream(filename+".tst"));
		
		WeightVector w = new MultinomialWeightVector(trn.labelSize(), trn.featureSize());
		StochasticGradientDescent sgd = new MultinomialAdaGrad(w, true, 0.01f);
		DoubleIntPair max = new DoubleIntPair(0,0);
		double acc;
		
		for (int i=0; i<100; i++)
		{
			sgd.train(trn.getInstanceList(), 1);
			acc = getAccuracy(tst.getInstanceList(), w);
			if (acc > max.d) max.set(acc, i);
			System.out.printf("%d: %5.2f\n", i, acc);
		}

		System.out.printf("%d: %5.2f\n", max.i, max.d);
	}
	
	double getAccuracy(List<Instance> instances, WeightVector w)
	{
		int yhat, correct = 0;
		
		for (Instance instance : instances)
		{
			yhat = w.predictBest(instance.getVector()).getLabel();
			if (instance.getLabel() == yhat) correct++;
		}
		
		return MathUtils.getAccuracy(correct, instances.size());
	}
}
