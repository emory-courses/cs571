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
package edu.emory.mathcs.nlp.learn.weight;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.BiFunction;

import edu.emory.mathcs.nlp.common.collection.tuple.Pair;
import edu.emory.mathcs.nlp.learn.util.Prediction;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class WeightVector implements Serializable
{
	private static final long serialVersionUID = 5876902100282177639L;
	protected float[] weight_vector;
	protected int     label_size;
	protected int     feature_size;
	
	public WeightVector(int labelSize, int featureSize)
	{
		init(labelSize, featureSize);
	}
	
	/** @return a vector whose size is the same as this vector but the values are initialized to 0. */
	public WeightVector createEmptyVector()
	{
		return isBinomial() ? new BinomialWeightVector(feature_size) : new MultinomialWeightVector(label_size, feature_size);
	}
	
	/** @return true if the vector space follows binomial distribution. */
	public boolean isBinomial()
	{
		return weight_vector.length < label_size * feature_size;
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
	
	public void fromArray(float[] array)
	{
		weight_vector = array;
	}
	
	public float get(int label, int featureIndex)
	{
		return weight_vector[indexOf(label, featureIndex)];
	}
	
	/** Adds the value to the weight in {@link #indexOf(int, int)}. */
	public void add(int label, int featureIndex, double value)
	{
		weight_vector[indexOf(label, featureIndex)] += value;
	}
	
	/** weight_vector[indexOf(label, featureIndex)] *= multiplier + value. */
	public void multiplyAdd(int label, int featureIndex, double multiplier, double value) 
	{
		weight_vector[indexOf(label, featureIndex)] *= multiplier + value;
	}
	public void multiply(int label, int featureIndex, double multiplier)
	{
		weight_vector[indexOf(label, featureIndex)] *= multiplier;
	}
	public void update(Vector x, int label, double gradient)
	{
		for (IndexValuePair p : x)
			add(label, p.getIndex(), gradient * p.getValue());
	}
	
	/** @param gradient takes the (label, featureIndex) and returns the gradient to update. */
	public void update(Vector x, int label, BiFunction<Integer,Integer,Double> gradient)
	{
		for (IndexValuePair p : x)
			add(label, p.getIndex(), gradient.apply(label, p.getIndex()) * p.getValue());
	}
	
	/** Fills this weight vector with the specific value. */
	public void fill(float value)
	{
		Arrays.fill(weight_vector, value);
	}
	
	@Override
	public String toString()
	{
		return Arrays.toString(weight_vector);
	}
	
	public abstract void init(int labelSize, int featureSize);
	
	/**
	 * Expands the size of this weight vector.
	 * @param labelSize ignored for binomial, must be greater or equal to {@link #label_size} for multinomial.
	 * @param featureSize must be greater or equal to {@link #feature_size}.
	 * @return true if the weight vector is expanded; otherwise, false.
	 */
	public abstract boolean expand(int labelSize, int featureSize);

	/**
	 * @return the index of the weight vector with respect to the (labelIndex, featureIndex).
	 * For binomial distribution, it just returns featureIndex.
	 */
	public abstract int indexOf(int label, int featureIndex);

	/** @return the scores of all labels with respect to x. */
	public abstract double[] scores(Vector x);
	
	/** @return the best predicated label with respect to x. */
	public abstract Prediction predictBest(Vector x);

	/** @return the top 2 predicated labels with respect to x. */
	public abstract Pair<Prediction,Prediction> predictTop2(Vector x);
	
	/** @return the list of all predictions (not sorted). */
	public abstract Prediction[] predictAll(Vector x);
}
