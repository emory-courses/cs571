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
package edu.emory.mathcs.nlp.learn.vector;

import java.util.Arrays;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class BinomialWeightVector extends WeightVector
{
	public BinomialWeightVector(int featureSize)
	{
		super(2, featureSize);
		weight_vector = new float[featureSize];
	}
	
	@Override
	public boolean expand(int labelSize, int featureSize)
	{
		if (feature_size < featureSize)
		{
			weight_vector = Arrays.copyOf(weight_vector, featureSize);
			feature_size  = featureSize; 
			return true;
		}
			
		return false;
	}

	@Override
	public int indexOf(int labelIndex, int featureIndex)
	{
		return featureIndex;
	}

	@Override
	public double[] scores(SparseVector vector)
	{
		double score = 0;
		
		for (SparseVectorItem p : vector)
			score += weight_vector[p.getIndex()] * p.getValue();
		
		return new double[]{score, -score};
	}
}
