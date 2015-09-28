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

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.Arrays;
import java.util.Random;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.deeplearning.activation.SigmoidFunction;
import edu.emory.mathcs.nlp.vsm.util.Vocabulary;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NegativeSampling extends Optimizer
{
	final double DIST_POWER = 0.75;
	int[] dist_table;
	int sample_size;
	
	public NegativeSampling(Vocabulary vocab, SigmoidFunction sigmoid, int vectorSize, int sampleSize)
	{
		super(vocab, sigmoid, vectorSize);
		sample_size = sampleSize;
		initDistributionTable();
	}
	
	private void initDistributionTable()
	{
		double d, Z = vocab.list().stream().mapToDouble(v -> Math.pow(v.count, DIST_POWER)).sum();
		dist_table = new int[vocab.size() * sample_size * 10];
		int i = 0, j, size = dist_table.length;
		
		d = nextDistribution(i, Z);
		
		for (j=0; j<size && i<vocab.size(); j++)
		{
			dist_table[j] = i;
			
			if (MathUtils.divide(j, size) > d)
				d += nextDistribution(++i, Z);
		}
		
		if (j < size)
			Arrays.fill(dist_table, j, size, vocab.size()-1);
	}
	
	/** Called by {@link #initDistributionTable()}. */
	private double nextDistribution(int index, double Z)
	{
		return Math.pow(vocab.get(index).count, DIST_POWER) / Z;
	}
	
	@Override
	public void learnBagOfWords(Random rand, int word, float[] syn1, float[] neu1, float[] neu1e, double alpha)
	{
		learnBagOfWords(1, word, syn1, neu1, neu1e, alpha);
		
		for (int sample : getNegativeSamples(rand, word))
			learnBagOfWords(0, sample, syn1, neu1, neu1e, alpha);
	}
	
	@Override
	public void learnSkipGram(Random rand, int word, float[] syn0, float[] syn1, float[] neu1e, double alpha, int l1)
	{
		learnSkipGram(1, word, syn0, syn1, neu1e, alpha, l1);
		
		for (int sample : getNegativeSamples(rand, word))
			learnSkipGram(0, sample, syn0, syn1, neu1e, alpha, l1);
	}
	
	private int[] getNegativeSamples(Random rand, int word)
	{
		IntSet set = new IntOpenHashSet();
		int target;

		while (set.size() < sample_size)
		{
			target = dist_table[rand.nextInt() % dist_table.length];
			if (target != word) set.add(target);
		}
		
		return set.toIntArray();
	}
}