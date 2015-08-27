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
package edu.emory.mathcs.nlp.benchmark;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class BenchmarkTest
{
	@Test
	public void test() throws Exception
	{
		int i, size = 1000000;
		long st, et;
//		FloatArrayList fast = new FloatArrayList();
//		for (i=0; i<size; i++) fast.add(i);
//		
//		st = System.currentTimeMillis();
//		for (i=0; i<100000; i+= 10) fast.add(i, 0f);
//		et = System.currentTimeMillis();
//		System.out.println(et-st);
		
		float[] array = new float[size];
		Arrays.fill(array, 0f);
		
		st = System.currentTimeMillis();
		float[] copy = new float[size+10000];
		for (i=0; i<size; i++) copy[i] = array[i];
		for (i=size; i<copy.length; i++) copy[i] = 0f;
		et = System.currentTimeMillis();
		System.out.println(et-st);
	}
}
