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
package edu.emory.mathcs.nlp.learn.online;

import java.util.Random;

import org.junit.Test;
import org.magicwerk.brownies.collections.primitive.FloatGapList;

import edu.emory.mathcs.nlp.learn.vector.SparseVector;
import edu.emory.mathcs.nlp.learn.vector.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class OnlineTrainer<V>
{
	protected WeightVector vector;

	public void train(short label, SparseVector features)
	{
		double[] scores = vector.scores(features);
	}

	
	@Test
	public void update() throws Exception
	{
		Random rand = new Random(1);
		int size = 100000000;
		long st, et;
		
//		FloatGapList list = new FloatGapList();
//		for (int i=0; i<size; i++) list.add(i);
//
//		st = System.currentTimeMillis();
//		for (int i=0; i<size; i++) list.set(rand.nextInt(size),i);
//		et = System.currentTimeMillis();
//		System.out.println(et-st);
//		
//		st = System.currentTimeMillis();
//		for (int i=0; i<size; i++) list.get(rand.nextInt(size));
//		et = System.currentTimeMillis();
//		System.out.println(et-st);
		
//		FloatArrayList list = new FloatArrayList();
//		for (int i=0; i<size; i++) list.add(i);
//		
//		st = System.currentTimeMillis();
//		for (int i=0; i<size; i++) list.set(rand.nextInt(size),i);
//		et = System.currentTimeMillis();
//		System.out.println(et-st);
//		
//		st = System.currentTimeMillis();
//		for (int i=0; i<size; i++) list.get(rand.nextInt(size));
//		et = System.currentTimeMillis();
//		System.out.println(et-st);
		
		FloatGapList list = new FloatGapList();
		for (int i=0; i<size; i++) list.add(i); list.trimToSize();
		
//		FloatArrayList list = new FloatArrayList();
//		for (int i=0; i<size; i++) list.add(i);
		
//		float[] f = new float[size];
//		for (int i=0; i<size; i++) f[i] = i;
		System.out.println("A"); System.in.read();
	}
}
