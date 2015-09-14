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

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AdaGrad extends StochasticGradientDescent
{
	protected final double epsilon = 0.00001;
	protected WeightVector diagonals;
	
	public AdaGrad(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
		diagonals = weightVector.createEmptyVector();
	}
	
	@Override
	protected void updateBinomial(Instance instance)
	{
		Vector x = instance.getVector();
		int yp = instance.getLabel();
		int yn = bestHingeBinomial(x);
		
		if (yp != yn)
		{
			updateDiagonals(x, yp);
			weight_vector.update(x, yp, (i,j) -> getGradient(i,j) * yp);
			if (isAveraged()) average_vector.update(x, yp, (i,j) -> getGradient(i,j) * yp * steps);
		}
	}

	@Override
	protected void updateMultinomial(Instance instance)
	{
		Vector x = instance.getVector();
		int yp = instance.getLabel();
		int yn = bestHingeMultinomial(instance);
		
		if (yp != yn)
		{
			updateDiagonals(x, yp);
			updateDiagonals(x, yn);
			
			weight_vector.update(x, yp, (i,j) ->  getGradient(i,j));
			weight_vector.update(x, yn, (i,j) -> -getGradient(i,j));
			
			if (isAveraged())
			{
				average_vector.update(x, yp, (i,j) ->  getGradient(i,j) * steps);
				average_vector.update(x, yn, (i,j) -> -getGradient(i,j) * steps);
			}
		}
	}
	
	private void updateDiagonals(Vector x, int label)
	{
		for (IndexValuePair p : x)
			diagonals.add(label, p.getIndex(), MathUtils.sq(p.getValue()));
	}
	
	private double getGradient(int label, int featureIndex)
	{
		return learning_rate / (epsilon + Math.sqrt(diagonals.get(label, featureIndex)));
	}
	
	@Override
	public String toString()
	{
		StringJoiner join = new StringJoiner(", ");
		
		join.add("average = "+isAveraged());
		join.add("learning rate = "+learning_rate);
		
		return "AdaGrad: "+join.toString();
	}
}
