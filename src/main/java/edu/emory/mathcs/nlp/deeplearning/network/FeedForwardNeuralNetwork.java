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
import java.util.List;
import java.util.Random;

import edu.emory.mathcs.nlp.common.random.XORShiftRandom;
import edu.emory.mathcs.nlp.common.util.DSUtils;
import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.deeplearning.activation.ActivationFunction;
import edu.emory.mathcs.nlp.deeplearning.activation.SigmoidFunction;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.util.Prediction;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class FeedForwardNeuralNetwork implements Serializable
{
	private static final long serialVersionUID = -6902794736542104875L;
	private int input_size, output_size, hidden_size;
	private ActivationFunction activation_function;
	private double learning_rate;
	private float[] i2h, h2o;
	
	/** Calls {@link #init(int, int..., int)}. */
	public FeedForwardNeuralNetwork(double learningRate, int input, int output, int hidden)
	{
		activation_function = new SigmoidFunction();
		learning_rate = learningRate; 
		init(input, output, hidden);
	}
	
	/**
	 * Initializes this network.
	 * @param input  dimension of the input layer (required).
	 * @param output dimension of the output layer (required).
	 * @param hidden dimensions of the hidden layers (optional).
	 */
	public void init(int input, int output, int hidden)
	{
		input_size  = input;
		output_size = output;
		hidden_size = hidden;
		
		i2h = new float[input  * hidden];
		h2o = new float[hidden * output];
		
		Random rand = new XORShiftRandom(1);
		
		for (int i=0; i<i2h.length; i++)
			i2h[i] = (float)((rand.nextDouble() - 0.5) / hidden_size);
	}
	
//	============================== SCORES ==============================
	
	public double[][] scores(Vector x)
	{
		double[] hidden = scoresI2H(x, hidden_size);
		double[] output = scoresH2O(hidden);
		return new double[][]{output, hidden};
	}
	
	private double[] scoresI2H(Vector x, int hiddenSize)
	{
		double[] scores = new double[hiddenSize];
		int l, index;
		
		// column major
		for (IndexValuePair p : x)
		{
			if (p.getIndex() < input_size)
			{
				index = p.getIndex() * hiddenSize;
				
				for (l=0; l<hiddenSize; l++)
					scores[l] += i2h[index+l] * p.getValue();	
			}
		}
		
		activation_function.transform(scores);
		return scores;
	}
	
	private double[] scoresH2O(double[] hidden)
	{
		int l, f, index, hiddenSize = hidden.length;
		double[] scores = new double[output_size];
		
		// row major
		for (l=0; l<output_size; l++)
		{
			index = l * hiddenSize;
			
			for (f=0; f<hiddenSize; f++)
				scores[l] += h2o[index+f] * hidden[f];
		}
		
		MathUtils.softmax(scores);
		return scores;
	}
	
	public Prediction predictBest(Vector x)
	{
		double[] scores = scores(x)[0];
		int      label  = DSUtils.maxIndex(scores);
		return new Prediction(label, scores[label]);
	}

//	============================== OPERATIONS ==============================
	
	public void train(List<Instance> instances)
	{
		for (Instance instance : instances)
			train(instance);
	}
	
	public void train(Instance instance)
	{
		double[][] scores = scores(instance.getVector());
		double[] errors = trainO2H(instance, scores[1], scores[0]);
		trainH2I(instance.getVector(), errors);
	}
	
	private double[] trainO2H(Instance instance, double[] hidden, double[] output)
	{
		int f, l, y, index, hiddenSize = hidden.length;
		double[] errors = new double[hiddenSize];  
		double gradient;
		
		for (l=0; l<output_size; l++)
		{
			index = l * hiddenSize;
			y = instance.isLabel(l) ? 1 : 0;
			gradient = learning_rate * (y - output[l]);
			
			for (f=0; f<hiddenSize; f++)
				errors[f] += gradient * h2o[index+f];
			
			for (f=0; f<hiddenSize; f++)
				h2o[index+f] += gradient * hidden[f];
		}
		
		return errors;
	}
	
	private void trainH2I(Vector x, double[] errors)
	{
		int l, index, hiddenSize = errors.length;
		
		for (IndexValuePair p : x)
		{
			index = p.getIndex() * hiddenSize;
			
			for (l=0; l<hiddenSize; l++)						
				i2h[index+l] += errors[l] * p.getValue();
		}
	}
	
	public String toString()
	{
		return "Feedforward Neural Network";
	}
}
