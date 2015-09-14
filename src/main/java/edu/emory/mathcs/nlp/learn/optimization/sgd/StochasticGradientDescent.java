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
package edu.emory.mathcs.nlp.learn.optimization.sgd;

import java.util.List;

import edu.emory.mathcs.nlp.learn.optimization.OnlineOptimizer;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class StochasticGradientDescent extends OnlineOptimizer
{
	public StochasticGradientDescent(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
	}

	@Override
	public void train(List<Instance> instances, int epochs)
	{
		steps = 1;
		
		for (; epochs>0; epochs--)
		{
			shuffle(instances);
			
			for (Instance instance : instances)
			{
				update(instance);
				steps++;
			}
		}
		
		if (isAveraged() && steps > 0) average();
	}
	
	private void average()
	{
		float[] w = weight_vector .toArray();
		float[] a = average_vector.toArray();
		float   n = 1f / steps;
		
		for (int i=0; i<w.length; i++)
			w[i] -= a[i] * n;
		
		average_vector.fill(0);
	}
}
