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

import edu.emory.mathcs.nlp.learn.util.BinomialLabel;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class Optimizer
{
	protected WeightVector weight_vector;
	
	public Optimizer(WeightVector weightVector)
	{
		weight_vector = weightVector;
	}
	
	 protected byte[] getBinaryLabels(List<Instance> instances, int currLabel)
	 {
		 int i, size = instances.size();
		 byte[] y = new byte[size];

		 for (i=0; i<size; i++)
			 y[i] = instances.get(i).isLabel(currLabel) ? BinomialLabel.POSITIVE  : BinomialLabel.NEGATIVE;

		 return y;
	 }
}
