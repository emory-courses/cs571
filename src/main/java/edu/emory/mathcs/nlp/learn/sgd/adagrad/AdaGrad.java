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
package edu.emory.mathcs.nlp.learn.sgd.adagrad;

import java.util.StringJoiner;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AdaGrad extends StochasticGradientDescent
{
	protected final double epsilon = 0.00001;
	protected WeightVector diagonals;
	
	public AdaGrad(WeightVector weightVector, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
		diagonals = weightVector.createEmptyVector();
	}
	
	protected void updateWeightVectorMiniBatch() {}
	
	protected void updateDiagonals(Vector x, int label)
	{
		for (IndexValuePair p : x)
			diagonals.add(label, p.getIndex(), MathUtils.sq(p.getValue()));
	}
	
	protected double getGradient(int label, int featureIndex)
	{
		return learning_rate / (epsilon + Math.sqrt(diagonals.get(label, featureIndex)));
	}
	
	public String toString()
	{
		StringJoiner join = new StringJoiner(", ");
		
		join.add("average = "+isAveraged());
		join.add("learning rate = "+learning_rate);
		
		return "AdaGrad: "+join.toString();
	}
}
