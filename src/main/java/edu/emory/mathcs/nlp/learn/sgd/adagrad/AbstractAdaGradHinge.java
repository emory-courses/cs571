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

import edu.emory.mathcs.nlp.common.MathUtils;
import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractAdaGradHinge extends StochasticGradientDescent
{
	protected WeightVector diagonals;
	protected float ridge;
	
	public AbstractAdaGradHinge(WeightVector weightVector, boolean average, float learningRate, float ridge)
	{
		super(weightVector, average, learningRate);
		diagonals = weightVector.createEmptyVector();
		this.ridge = ridge;
	}
	
	protected void updateDiagonals(Vector x, int label)
	{
		for (IndexValuePair p : x)
			diagonals.add(label, p.getIndex(), (float)MathUtils.sq(p.getValue()));
	}
	
	protected float getGradient(int label, int featureIndex)
	{
		return learning_rate / (ridge + (float)Math.sqrt(diagonals.get(label, featureIndex)));
	}
}
