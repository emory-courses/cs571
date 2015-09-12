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

import java.util.Arrays;
import java.util.StringJoiner;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AdaDelta extends StochasticGradientDescent
{
	protected final double epsilon = 0.00001;
	protected WeightVector diagonals;
	protected WeightVector gradients;
	protected int          mini_batch;
	
	protected final double decaying_rate;
	protected final double growth_rate;
	protected final int    batch_size;

	public AdaDelta(WeightVector weightVector, boolean average, double learningRate, double decayingRate, int batchSize)
	{
		super(weightVector, average, learningRate);
		diagonals  = weightVector.createEmptyVector();
		gradients  = weightVector.createEmptyVector();
		mini_batch = 0;
		
		decaying_rate = decayingRate;
		growth_rate   = 1-decayingRate;
		batch_size    = batchSize;
	}
	
	@Override
    protected void updateWeightVector(Instance instance, int steps)
    {
    	updateGradients(instance);
		
		if (++mini_batch == batch_size)
			updateWeightVectorMiniBatch();
    }
	
	protected void updateWeightVectorMiniBatch()
	{
		if (mini_batch > 0)
		{
			updateDiagonals();
			updateWeightVector();
			Arrays.fill(gradients.toArray(), 0);
			mini_batch = 0;
		}
	}
	
	protected void updateDiagonals()
	{
		float[] d = diagonals.toArray();
		float[] g = gradients.toArray();
		
		for (int i=0; i<d.length; i++)
			d[i] = (float)(decaying_rate*d[i] + growth_rate*MathUtils.sq(g[i]));
	}
	
	protected void updateWeightVector()
	{
		float[] w = weight_vector.toArray();
		float[] d = diagonals.toArray();
		float[] g = gradients.toArray();
 		
		for (int i=0; i<w.length; i++)
			w[i] += learning_rate / (epsilon + Math.sqrt(d[i])) * g[i];
	}
	
	protected abstract void updateGradients(Instance instance);
	
	@Override
	public String toString()
	{
		StringJoiner join = new StringJoiner(", ");
		
		join.add("average = " + isAveraged());
		join.add("learning rate = "+learning_rate);
		join.add("decaying rate = "+decaying_rate);
		join.add("batch size = "+batch_size);
		
		return "AdaDelta: "+join.toString();
	}
}
