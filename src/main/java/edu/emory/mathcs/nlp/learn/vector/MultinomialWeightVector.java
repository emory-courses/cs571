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
public class MultinomialWeightVector extends WeightVector
{
	public MultinomialWeightVector(int labelSize, int featureSize)
	{
		super(labelSize, featureSize);
		weight_vector = new float[labelSize * featureSize];
	}

	@Override
	public boolean expand(int labelSize, int featureSize)
	{
		if (labelSize < label_size || featureSize < feature_size || (labelSize == label_size && featureSize == feature_size)) return false;
		int i, j, diff = labelSize - label_size;
		float[] vector;
		
		if (diff > 0)
		{
			vector = new float[labelSize * featureSize];
			int size = label_size * feature_size;
			
			for (i=0,j=0; i<size; i++,j++)
			{
				if (i%label_size == 0) j += diff;
				vector[j] = weight_vector[i];
			}
		}
		else
			vector = Arrays.copyOf(weight_vector, labelSize * featureSize);
		
		weight_vector = vector;
		label_size    = labelSize;
		feature_size  = featureSize;
		
		return true;
	}
	
	@Override
	public int indexOf(int labelIndex, int featureIndex)
	{
		return labelIndex + indexOf(featureIndex);
	}
	
	private int indexOf(int featureIndex)
	{
		return featureIndex * label_size;
	}

	@Override
	public double[] scores(SparseVector vector)
	{
		double[] scores = new double[label_size];
		int i, index;
		
		for (SparseVectorItem p : vector)
		{
			index = indexOf(p.getIndex());
			
			for (i=0; i<label_size; i++)
				scores[i] += weight_vector[index+i];
		}
		
		return scores;
	}
}
