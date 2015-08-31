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

import edu.emory.mathcs.nlp.common.DSUtils;
import edu.emory.mathcs.nlp.common.collection.tuple.Pair;
import edu.emory.mathcs.nlp.learn.instance.Prediction;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class MultinomialWeightVector extends WeightVector
{
	private static final long serialVersionUID = 2190946158451118027L;

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
	public int indexOf(int label, int featureIndex)
	{
		return label + indexOf(featureIndex);
	}
	
	private int indexOf(int featureIndex)
	{
		return featureIndex * label_size;
	}

	@Override
	public double[] scores(Vector x)
	{
		double[] scores = new double[label_size];
		int i, index;
		
		for (IndexValuePair p : x)
		{
			index = indexOf(p.getIndex());
			
			for (i=0; i<label_size; i++)
				scores[i] += weight_vector[index+i];
		}
		
		return scores;
	}

	@Override
	public Prediction predictBest(Vector x)
	{
		double[] scores = scores(x);
		int      label  = DSUtils.maxIndex(scores);
		return new Prediction(label, scores[label]);
	}

	@Override
	public Pair<Prediction,Prediction> predictTop2(Vector x)
	{
		double[] scores = scores(x);
		Prediction fst, snd;
		
		if (scores[0] < scores[1])
		{
			fst = new Prediction(1, scores[1]);
			snd = new Prediction(0, scores[0]);
		}
		else
		{
			fst = new Prediction(0, scores[0]);			
			snd = new Prediction(1, scores[1]);
		}
		
		for (int i=2; i<label_size; i++)
		{
			if (fst.getScore() < scores[i])
			{
				snd.copy(fst);
				fst.set(i, scores[i]);
			}
			else if (snd.getScore() < scores[i])
				snd.set(i, scores[i]);
		}
		
		return new Pair<Prediction,Prediction>(fst, snd);
	}

	@Override
	public Prediction[] predictAll(Vector x)
	{
		double[] scores = scores(x);
		Prediction[] ps = new Prediction[label_size];
		
		for (int i=0; i<label_size; i++)
			ps[i] = new Prediction(i, scores[i]);
		
		return ps;
	}
}
