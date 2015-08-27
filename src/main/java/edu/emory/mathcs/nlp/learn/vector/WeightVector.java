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



/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class WeightVector
{
	protected float[] weight_vector;
	protected int label_size;
	protected int feature_size;
	
	public WeightVector(int labelSize, int featureSize)
	{
		label_size   = labelSize;
		feature_size = featureSize;
	}
	
	/** Adds the value to the weight in {@link #indexOf(int, int)}. */
	public void add(int labelIndex, int featureIndex, float value)
	{
		weight_vector[indexOf(labelIndex, featureIndex)] += value;
	}
	
	public int labelSize()
	{
		return label_size;
	}
	
	public int featureSize()
	{
		return feature_size;
	}
	
	public float[] toArray()
	{
		return weight_vector;
	}
	
	/**
	 * Expands the size of this weight vector.
	 * @param labelSize ignored for binomial, must be greater or equal to {@link #label_size} for multinomial.
	 * @param featureSize must be greater or equal to {@link #feature_size}.
	 * @return true if the weight vector is expanded; otherwise, false.
	 */
	public abstract boolean expand(int labelSize, int featureSize);
	
	/** @return the index of the weight vector with respect to the (labelIndex, featureIndex); */
	public abstract int indexOf(int labelIndex, int featureIndex);

	public abstract double[] scores(SparseVector vector);
}
