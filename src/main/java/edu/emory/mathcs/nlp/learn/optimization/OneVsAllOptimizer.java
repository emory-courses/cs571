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
package edu.emory.mathcs.nlp.learn.optimization;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.emory.mathcs.nlp.learn.util.BinomialLabel;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
abstract public class OneVsAllOptimizer extends Optimizer
{
	public OneVsAllOptimizer(WeightVector weightVector)
	{
		super(weightVector);
	}
	
	public void train(List<Instance> instances, int threadSize)
	{	
		if (weight_vector.isBinomial())
			trainBinomial(instances);
		else
			trainMultinomial(instances, threadSize);
	}
	
	private void trainBinomial(List<Instance> instances)
	{
		update(instances, BinomialLabel.POSITIVE);
	}
	
	private void trainMultinomial(List<Instance> instances, int threadSize)
	{
		ExecutorService executor = Executors.newFixedThreadPool(threadSize);
		int currLabel, size = weight_vector.labelSize();
		
		for (currLabel=0; currLabel<size; currLabel++)
			executor.execute(new TrainTask(instances, currLabel));
		
		executor.shutdown();
		
		try
		{
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch (InterruptedException e) {e.printStackTrace();}
	}
	
	class TrainTask implements Runnable
	{
		List<Instance> instances;
		int curr_label;
		
		/** @param currLabel the current label to train. */
		public TrainTask(List<Instance> instances, int currLabel)
		{
			this.instances = instances;
			curr_label = currLabel;
		}
		
		public void run()
		{
			update(instances, curr_label);
		}
    }
	
	abstract public void update(List<Instance> instances, int currLabel);
	
	protected double getScore(Vector x, float[] weight)
	{
		double score = 0;
		
		for (IndexValuePair p : x)
			score += weight[p.getIndex()] * p.getValue();
		
		return score;
	}
	
	protected void updateWeights(Vector x, float[] weight, double gradient)
	{
		for (IndexValuePair p : x)
			weight[p.getIndex()] += gradient * p.getValue();
	}
}