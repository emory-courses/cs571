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

import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class SGDClassification extends StochasticGradientDescent
{
	public SGDClassification(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
	}

	protected void update(int y, Vector x)
	{
		double g;
		
		for (IndexValuePair xi : x)
		{
			g = y * getGradient(y, xi.getIndex()) * xi.getValue();
			weight_vector.add(y, xi.getIndex(), g);
			if (isAveraged()) average_vector.add(y, xi.getIndex(), g * steps);
		}
	}
	
	protected void update(int yp, int yn, Vector x)
	{
		double gp, gn;
		
		for (IndexValuePair xi : x)
		{
			gp =  getGradient(yp, xi.getIndex()) * xi.getValue();
			gn = -getGradient(yn, xi.getIndex()) * xi.getValue();
			
			weight_vector.add(yp, xi.getIndex(), gp);
			weight_vector.add(yn, xi.getIndex(), gn);
							
			if (isAveraged())
			{
				average_vector.add(yp, xi.getIndex(), gp * steps);
				average_vector.add(yn, xi.getIndex(), gn * steps);
			}
		}
	}
	
	protected abstract double getGradient(int y, int xi);
}
