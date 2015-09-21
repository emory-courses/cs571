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
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Perceptron extends SGDClassification
{
	public Perceptron(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
	}
	
	@Override
	protected void updateBinomial(Instance instance)
	{
		Vector x = instance.getVector();
		int   yp = instance.getLabel();	
		int   yn = weight_vector.predictBest(x).getLabel();
		if (yp != yn) update(yp*2-1, x);  // yp = {0, 1} -> {-1, 1}
	}
	
	@Override
	protected void updateMultinomial(Instance instance)
	{
		Vector x = instance.getVector();
		int   yp = instance.getLabel();
		int   yn = weight_vector.predictBest(x).getLabel();
		if (yp != yn) update(yp, yn, x);
	}
	
	@Override
	protected double getGradient(int y, int xi)
	{
		return learning_rate;
	}

	@Override
	public String toString()
	{
		StringJoiner join = new StringJoiner(", ");
		
		join.add("average = "+isAveraged());
		join.add("learning rate = "+learning_rate);
		
		return "Perceptron: "+join.toString();
	}
}
