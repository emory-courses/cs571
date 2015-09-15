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
public abstract class OnlineOptimizer extends Optimizer
{
	protected WeightVector average_vector;
	protected final double learning_rate;
	protected Random random;
	protected int steps;
	
	/**
	 * @param weightVector the weight vector to be trained (may contain previously learned weights). 
	 * @param average if true, use averaged SGD.
	 * @param learningRate the learning rate.
	 */
	public OnlineOptimizer(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector);
		average_vector = average ? weightVector.createEmptyVector() : null;
		learning_rate  = learningRate;
		random         = new Random(5);
	}
	
	/** Calls {@link #train(List, int)} for 1 epoch. */
	public void train(List<Instance> instances)
	{
		train(instances, 1);
	}
	
	/** Shuffles the trainign instances. */
	public void shuffle(List<Instance> instances)
	{
		Collections.shuffle(instances, random);
	}
	
	/** @return true if averaged SGD is used. */
 	public boolean isAveraged()
	{
		return average_vector != null;
	}
 	
 	/**
	 * Trains the weight vector using the instances for a certain number of epochs.
	 * If averaged SGD is used, the weights are averaged after all epochs.
	 * The instances will be shuffled for every epoch.
	 */
 	public abstract void train(List<Instance> instances, int epochs);
 	
//	============================== UPDATE ==============================

	/** Updates the weight vector given the training instance. */
	protected void update(Instance instance)
	{
		if (weight_vector.isBinomial())
			updateBinomial(instance);
		else
			updateMultinomial(instance);
	}
	
	/**
	 * Updates the weight vector for binomial classification.
	 * Called by {@link #update(Instance)}.
	 */
	protected abstract void updateBinomial(Instance instance);
	/**
	 * Updates the weight vector for multinomial classification.
	 * Called by {@link #update(Instance)}.
	 */
	protected abstract void updateMultinomial(Instance instance);

	protected int bestHingeBinomial(Vector x)
	{
		Prediction p = weight_vector.predictBest(x);
		return (Math.abs(p.getScore()) >= 0.5) ? p.getLabel() : -p.getLabel();
	}
 	
 	protected int bestHingeMultinomial(Instance instance)
	{
		double[] scores = weight_vector.scores(instance.getVector());
		scores[instance.getLabel()] -= 1;
		return DSUtils.maxIndex(scores);
	}
}
