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

import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class BinomialAdaGradHinge extends AbstractAdaGradHinge
{
	public BinomialAdaGradHinge(WeightVector weightVector, boolean average, double learningRate, double ridge)
	{
		super(weightVector, average, learningRate, ridge);
	}

	@Override
	protected void updateWeightVector(Instance instance, int steps)
	{
		Vector x = instance.getVector();
		int y = instance.getLabel(), yhat = bestBinomialLabelHinge(x);
		
		if (y != yhat)
		{
			updateDiagonals(x, y);
			weight_vector.update(x, y, (i,j) -> getGradient(i,j) * y);
			if (isAveraged()) weight_vector.update(x, y, (i,j) -> getGradient(i,j) * y * steps);
		}
	}
}
