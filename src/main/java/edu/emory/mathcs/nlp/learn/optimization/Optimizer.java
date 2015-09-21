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
package edu.emory.mathcs.nlp.learn.optimization;

import java.util.List;

import edu.emory.mathcs.nlp.common.util.DSUtils;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class Optimizer
{
	protected WeightVector weight_vector;
	private OptimizerType type;
	
	public Optimizer(WeightVector weightVector, OptimizerType type)
	{
		weight_vector = weightVector;
		this.type = type;
	}
	
	public OptimizerType getType()
	{
		return type;
	}
	
	public abstract void train(List<Instance> instances);

	protected int binomialBestHingeLoss(Instance instance)
	{
		double score = weight_vector.scores(instance.getVector())[0];
		int    label = (score < 0) ? 0 : 1;
		return Math.abs(score) < 0.5 ? (label+1)%2 : label;
	}
	
	protected int multinomialBestHingeLoss(Instance instance)
	{
		double[] scores = weight_vector.scores(instance.getVector());
		scores[instance.getLabel()] -= 1;
		return DSUtils.maxIndex(scores);
	}
}
