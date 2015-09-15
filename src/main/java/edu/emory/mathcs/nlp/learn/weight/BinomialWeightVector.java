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

import edu.emory.mathcs.nlp.common.collection.tuple.Pair;
import edu.emory.mathcs.nlp.learn.util.BinomialLabel;
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
	
	public double score(Vector x)
	{
		double score = 0;
		
		for (IndexValuePair p : x)
		{
			if (p.getIndex() < feature_size)
				score += weight_vector[p.getIndex()] * p.getValue();
		}
		
		return score;
	}
	
	@Override
	public int indexOf(int label, int featureIndex)
	{
		return featureIndex;
	}
	
	@Override
	public double[] scores(Vector x)
	{
		double score = score(x);
		return new double[]{score, -score};
	}

	@Override
	public Prediction predictBest(Vector x)
	{
		double score = score(x);
		int    label = (score >= 0) ? BinomialLabel.POSITIVE : BinomialLabel.NEGATIVE;
		return new Prediction(label, score);
	}

	@Override
	public Pair<Prediction,Prediction> predictTop2(Vector x)
	{
		Prediction fst = predictBest(x);
		Prediction snd = new Prediction(-fst.getLabel(), -fst.getScore());
		return new Pair<>(fst, snd);
	}

	@Override
	public Prediction[] predictAll(Vector x)
	{
		Pair<Prediction,Prediction> top2 = predictTop2(x);
		return new Prediction[]{top2.o1, top2.o2};
	}
}
