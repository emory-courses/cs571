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

import edu.emory.mathcs.nlp.deeplearning.activation.SigmoidFunction;
import edu.emory.mathcs.nlp.vsm.util.Vocabulary;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class HierarchicalSoftmax extends Optimizer
{
	public HierarchicalSoftmax(Vocabulary vocab, SigmoidFunction sigmoid, int vectorSize)
	{
		super(vocab, sigmoid, vectorSize);
		vocab.generateHuffmanCodes();
	}
	
	@Override
	public void learnBagOfWords(Random rand, int word, float[] syn1, float[] neu1, float[] neu1e, double alpha)
	{
		byte[] code  = vocab.get(word).code;
		int [] point = vocab.get(word).point;
		
		for (int i=0; i<code.length; i++)
			learnBagOfWords(code[i], point[i], syn1, neu1, neu1e, alpha);
	}
	
	@Override
	public void learnSkipGram(Random rand, int word, float[] syn0, float[] syn1, float[] neu1e, double alpha, int l1)
	{
		byte[] code  = vocab.get(word).code;
		int[]  point = vocab.get(word).point;
		
		for (int i=0; i<code.length; i++)
			learnSkipGram(code[i], point[i], syn0, syn1, neu1e, alpha, l1);
	}
}
