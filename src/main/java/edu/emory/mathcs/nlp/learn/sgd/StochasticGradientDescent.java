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
package edu.emory.mathcs.nlp.learn.sgd;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.emory.mathcs.nlp.common.util.DSUtils;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.util.Prediction;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class StochasticGradientDescent
{
	protected WeightVector average_vector;
	protected WeightVector weight_vector;
	protected final double learning_rate;
	protected final Random random;
	
	/**
	 * @param weightVector the weight vector to be trained (may contain previously learned weights). 
	 * @param average if true, use averaged SGD.
	 * @param learningRate the learning rate.
	 */
	public StochasticGradientDescent(WeightVector weightVector, boolean average, double learningRate)
	{
		average_vector = average ? weightVector.createEmptyVector() : null;
		weight_vector  = weightVector;
		learning_rate  = learningRate;
		random         = new Random(5);
	}
	
	/** Calls {@link #train(List, int)} for 1 epoch. */
	public void train(List<Instance> instances)
	{
		train(instances, 1);
	}
	
	/**
	 * Trains the weight vector using the instances for a certain number of epochs.
	 * If averaged SGD is used, the weights are averaged after all epochs.
	 * The instances will be shuffled for every epoch.
	 */
	public void train(List<Instance> instances, int epochs)
	{
		int steps = 1;
		
		for (; epochs>0; epochs--)
		{
			Collections.shuffle(instances, random);
			
			for (Instance instance : instances)
				updateWeightVector(instance, steps++);
		}
		
		updateWeightVectorMiniBatch();
		
		if (isAveraged() && steps > 0)
		{
			average(steps);
			average_vector.fill(0);
		}
	}
	
	/**
	 * Updates the weight vector given the training instance.
	 * @param steps the total number of instance visited during training.
	 */
	protected abstract void updateWeightVector(Instance instance, int steps);
	protected abstract void updateWeightVectorMiniBatch();
	
	protected void average(int steps)
	{
		float[] weight  = weight_vector .toArray();
		float[] average = average_vector.toArray();
		float   norm    = 1f / steps;
		
		for (int i=0; i<weight.length; i++)
			weight[i] -= average[i] * norm; 
	}
	
	/** @return true if averaged SGD is used. */
 	public boolean isAveraged()
	{
		return average_vector != null;
	}
 	
 	protected int bestBinomialLabelHinge(Vector x)
	{
		Prediction p = weight_vector.predictBest(x);
		return (Math.abs(p.getScore()) >= 0.5) ? p.getLabel() : -p.getLabel();
	}
 	
 	protected int bestMultinomialLabelHinge(Instance instance)
	{
		double[] scores = weight_vector.scores(instance.getVector());
		scores[instance.getLabel()] -= 1;
		return DSUtils.maxIndex(scores);
	}
}
