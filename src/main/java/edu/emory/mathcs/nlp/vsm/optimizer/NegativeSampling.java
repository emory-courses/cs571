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

import java.util.Arrays;
import java.util.Random;

import edu.emory.mathcs.nlp.common.util.Sigmoid;
import edu.emory.mathcs.nlp.vsm.util.Vocabulary;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NegativeSampling extends Optimizer
{
	final double DIST_POWER = 0.75;
	int[] dist_table;
	int   sample_size;
	
	public NegativeSampling(Vocabulary vocab, Sigmoid sigmoid, int vectorSize, int sampleSize)
	{
		super(vocab, sigmoid, vectorSize);
		sample_size = sampleSize;
		initDistributionTable();
	}
	
	private void initDistributionTable()
	{
		double d, Z = vocab.list().stream().mapToDouble(v -> nextDistribution(v.count)).sum();
		int bIdx, eIdx = 0, size = vocab.size() * sample_size * 10;
		dist_table = new int[size];
		
		for (int i=0; i<vocab.size(); i++)
		{
			d = nextDistribution(vocab.get(i).count) / Z;
			bIdx  = eIdx;
			eIdx += (int)(d * size);
			Arrays.fill(dist_table, bIdx, eIdx, i);
		}
		
		if (eIdx < size) dist_table = Arrays.copyOf(dist_table, eIdx);
	}
	
	/** Called by {@link #initDistributionTable()}. */
	private double nextDistribution(long count)
	{
		return Math.pow(count, DIST_POWER);
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
			target = dist_table[rand.nextInt(Integer.MAX_VALUE) % dist_table.length];
			if (target != word) set.add(target);
		}
		
		return set.toIntArray();
	}
}