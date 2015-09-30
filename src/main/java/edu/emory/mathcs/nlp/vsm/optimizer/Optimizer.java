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
package edu.emory.mathcs.nlp.vsm.optimizer;

import java.util.Random;

import edu.emory.mathcs.nlp.common.util.Sigmoid;
import edu.emory.mathcs.nlp.vsm.util.Vocabulary;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class Optimizer
{
	protected Sigmoid sigmoid;
	protected Vocabulary vocab;
	protected int vector_size;
	
	public Optimizer(Vocabulary vocab, Sigmoid sigmoid, int vectorSize)
	{
		this.vocab   = vocab;
		this.sigmoid = sigmoid;
		vector_size  = vectorSize;
	}
	
	public abstract void learnBagOfWords(Random rand, int word, float[] syn1, float[] neu1, float[] neu1e, double alpha);
	public abstract void learnSkipGram  (Random rand, int word, float[] syn0, float[] syn1, float[] neu1e, double alpha, int l1);
	
	protected void learnBagOfWords(int label, int word, float[] syn1, float[] neu1, float[] neu1e, double alpha)
	{
		int l = word * vector_size, k;
		double score = 0, gradient;
		
		// hidden -> output
		for (k=0; k<vector_size; k++) score += neu1[k] * syn1[k+l];
		gradient = (label - sigmoid.get(score)) * alpha;
		
		if (gradient != 0)
		{
			// output -> hidden
			for (k=0; k<vector_size; k++) neu1e[k] += syn1[k+l] * gradient;
			// hidden -> output
			for (k=0; k<vector_size; k++) syn1[k+l] += neu1[k] * gradient;
		}
	}
	
	protected void learnSkipGram(int label, int word, float[] syn0, float[] syn1, float[] neu1e, double alpha, int l1)
	{
		int l2 = word * vector_size, k;
		double score = 0, gradient;
		
		// input -> output
		for (k=0; k<vector_size; k++) score += syn0[k+l1] * syn1[k+l2];
		gradient = (label - sigmoid.get(score)) * alpha;
		
		if (gradient != 0)
		{
			// output -> hidden
			for (k=0; k<vector_size; k++) neu1e[k] += syn1[k+l2] * gradient;
			// input -> output
			for (k=0; k<vector_size; k++) syn1[k+l2] += syn0[k+l1] * gradient;
		}
	}
}
