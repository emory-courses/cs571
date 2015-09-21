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

import java.util.StringJoiner;

import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SoftmaxRegression extends StochasticGradientDescent
{
	public SoftmaxRegression(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
	}
	
	@Override
	protected void updateBinomial(Instance instance)
	{
		Vector x = instance.getVector();
		int    y = instance.getLabel();
		double d = learning_rate * (y - weight_vector.scores(x)[0]), g;
		
		for (IndexValuePair xi : x)
		{
			g = d * xi.getValue();
			weight_vector.add(y, xi.getIndex(), g);
			if (isAveraged()) average_vector.add(y, xi.getIndex(), g * steps);
		}
	}
	
	@Override
	protected void updateMultinomial(Instance instance)
	{
		int      i, size = weight_vector.labelSize();
		Vector   x = instance.getVector();
		double[] d = weight_vector.scores(x);
		double   g;
		
		for (i=0; i<size; i++)
		{
			g = instance.isLabel(i) ? 1 - d[i] : -d[i];
			d[i] = learning_rate * g;
		}
		
		for (IndexValuePair xi : x)
		{
			for (i=0; i<size; i++)
			{
				g = d[i] * xi.getValue();
				weight_vector.add(i, xi.getIndex(), g);
				if (isAveraged()) average_vector.add(i, xi.getIndex(), g * steps);
			}
		}
	}

	@Override
	public String toString()
	{
		StringJoiner join = new StringJoiner(", ");
		
		join.add("average = "+isAveraged());
		join.add("learning rate = "+learning_rate);
		
		return "Logistic regression: "+join.toString();
	}
}
