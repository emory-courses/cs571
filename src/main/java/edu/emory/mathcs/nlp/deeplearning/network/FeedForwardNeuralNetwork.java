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
package edu.emory.mathcs.nlp.deeplearning.network;

import java.io.Serializable;

import edu.emory.mathcs.nlp.common.util.DSUtils;
import edu.emory.mathcs.nlp.deeplearning.activation.ActivationFunction;
import edu.emory.mathcs.nlp.learn.util.Prediction;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class FeedForwardNeuralNetwork implements Serializable
{
	private static final long serialVersionUID = -6902794736542104875L;
	private ActivationFunction activation_function;
	private float[][] weight_vectors;
	private int[] dimensions;
	
	/** Calls {@link #init(int, int, int...)}. */
	public FeedForwardNeuralNetwork(ActivationFunction function, int input, int output, int... hidden)
	{
		init(function, input, output, hidden);
	}
	
	/**
	 * Initializes this network.
	 * @param input  dimension of the input layer (required).
	 * @param output dimension of the output layer (required).
	 * @param hidden dimensions of the hidden layers (optional).
	 */
	public void init(ActivationFunction function, int input, int output, int... hidden)
	{
		// initialize dimensions
		int hsize = hidden.length;
		dimensions = new int[hsize+2];
		dimensions[0] = input;
		dimensions[hsize+1] = output;
		if (hsize > 0) System.arraycopy(hidden, 0, dimensions, 1, hsize);
		
		// initialize weight vectors
		weight_vectors = new float[hsize+1][];
		
		for (int i=1; i<dimensions.length; i++)
			weight_vectors[i-1] = new float[dimensions[i-1] * dimensions[i]];
		
		// initialize activation function
		activation_function = function;
	}
	
//	============================== GETTERS / SETTERS ==============================
	
	public float[][] getWeightVectors()
	{
		return weight_vectors;
	}
	
	public void setWeightVectors(float[][] weightVectors)
	{
		weight_vectors = weightVectors;
	}
	
	public float[] getWeightVector(int index)
	{
		return weight_vectors[index];
	}
	
	public void setWeightVector(int index, float[] weightVector)
	{
		weight_vectors[index] = weightVector;
	}
	
	public ActivationFunction getActivationFunction()
	{
		return activation_function;
	}
	
	public void setActivationFunction(ActivationFunction function)
	{
		activation_function = function;
	}
	
//	============================== SCORES ==============================
	
	public double[] scores(Vector x)
	{
		double[] scores = scoresFirst(x);
		
		for (int i=1; i<dimensions.length-1; i++)
			scores = scoresRest(scores, i);
	
		return scores;
	}
	
	private double[] scoresFirst(Vector x)
	{
		int l, index, features = dimensions[0], labels = dimensions[1];
		float[] weightVector = weight_vectors[0];
		double[] scores = new double[labels];
		
		for (IndexValuePair p : x)
		{
			if (p.getIndex() < features)
			{
				index = p.getIndex() * labels;
				
				for (l=0; l<labels; l++)
					scores[l] += weightVector[index+l] * p.getValue();	
			}
		}
		
		activation_function.transform(scores);
		return scores;
	}
	
	private double[] scoresRest(double[] input, int layer)
	{
		int f, l, index, features = dimensions[layer], labels = dimensions[layer+1];
		float[] weightVector = weight_vectors[layer];
		double[] scores = new double[labels];
		
		for (f=0; f<features; f++)
		{
			index = f * labels;
			
			for (l=0; l<labels; l++)
				scores[l] += weightVector[index+l] * input[f];
		}
		
		activation_function.transform(scores);
		return scores;
	}
	
	public Prediction predictBest(Vector x)
	{
		double[] scores = scores(x);
		int      label  = DSUtils.maxIndex(scores);
		return new Prediction(label, scores[label]);
	}

//	============================== OPERATIONS ==============================
	
	public String toString()
	{
		return "Feedforward Neural Network";
	}
}
