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
package edu.emory.mathcs.nlp.learn.sgd.adadelta;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AdaDelta extends StochasticGradientDescent
{
	protected final double epsilon = 0.0000001;
	protected WeightVector diagonals;
	protected double decaying_rate;
	protected double growth_rate;

	public AdaDelta(WeightVector weightVector, boolean average, double learningRate, double decayingRate)
	{
		super(weightVector, average, learningRate);
		diagonals  = weightVector.createEmptyVector();
		decaying_rate = decayingRate;
		growth_rate = 1-decayingRate;
	}
	
	protected void updateDiagonals(WeightVector gradient)
	{
		ParallelDecay.parallelDecay(diagonals.toArray(), (float)(decaying_rate));
		for (int i=0; i<gradient.toArray().length; i++)
			diagonals.toArray()[i] += growth_rate*MathUtils.sq(gradient.toArray()[i]);
	}
	
	protected double getGradient(int label, int featureIndex)
	{
		return learning_rate / (epsilon + Math.sqrt(diagonals.get(label, featureIndex)));
	}

	@Override
	public String toString()
	{
		StringJoiner join = new StringJoiner(", ");
		
		join.add("average = " + isAveraged());
		join.add("learning rate = "+learning_rate);
		join.add("decaying rate = "+decaying_rate);
		
		return "AdaDelta: "+join.toString();
	}
}
