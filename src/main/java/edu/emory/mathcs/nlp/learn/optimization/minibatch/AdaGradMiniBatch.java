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
package edu.emory.mathcs.nlp.learn.optimization.minibatch;

import java.util.StringJoiner;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AdaGradMiniBatch extends AdaptiveMiniBatch
{
	public AdaGradMiniBatch(WeightVector weightVector, double batchRatio, boolean average, double learningRate)
	{
		super(weightVector, batchRatio, average, learningRate);
	}
	
	@Override
	protected float getDiagonal(float previousDiagonal, float gradient)
	{
		return (float)(previousDiagonal + MathUtils.sq(gradient));
	}
	
	public String toString()
	{
		StringJoiner join = new StringJoiner(", ");
		
		join.add("batch ratio = "+batch_ratio);
		join.add("average = " + isAveraged());
		join.add("learning rate = "+learning_rate);
		
		return "AdaGradTrunc: "+join.toString();
	}
}
