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

import java.util.Arrays;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.util.Prediction;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class BinomialWeightVector extends WeightVector
{
	private static final long serialVersionUID = 7868307353161553611L;
	
	public BinomialWeightVector()
	{
		super(2, 0);
	}
	
	public BinomialWeightVector(int featureSize)
	{
		super(2, featureSize);
	}
	
	@Override
	public void init(int labelSize, int featureSize)
	{
		weight_vector = new float[featureSize];
		label_size    = 2;
		feature_size  = featureSize;
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
	public int indexOf(int y, int xi)
	{
		return xi;
	}
	
	public double score(Vector x)
	{
		double score = 0;
		
		for (IndexValuePair p : x)
		{
			if (p.getIndex() < feature_size)
				score += weight_vector[p.getIndex()] * p.getValue();
		}
		
		return isRegression() ? MathUtils.sigmoid(score) : score;
	}
	
	@Override
	public double[] scores(Vector x)
	{
		double score = score(x);
		return new double[]{score, score};
	}
	
	@Override
	public Prediction predictBest(Vector x)
	{
		double score = score(x);
		double bound = 0;
		double upper = 0;
		int    label = 1;
		
		if (isRegression())
		{
			bound = 0.5;
			upper = 1;
		}
		
		if (score < bound)
		{
			label = 0;
			score = upper - score;
		}
		
		return new Prediction(label, score);
	}
}
