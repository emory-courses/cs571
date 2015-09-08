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
public class Benchmark
{
	@Test
	public void speed()
	{
		final int warm = 100, iter = 10000000;
		String[] array = {"A","B","C","D","E","A","B","C","D","E","A","B","C","D","E","A","B","C","D","E"};
		long st, et;
		
		for (int i=0; i<warm; i++)
//			Arrays.stream(array).forEach(e -> call(e));
			for (String e : array) call(e);
		
		st = System.currentTimeMillis();
		for (int i=0; i<iter; i++)
//			Arrays.stream(array).forEach(e -> call(e));
			for (String e : array) call(e);
		et = System.currentTimeMillis();
		System.out.println(et-st);
	}

	@Test
	public void test()
	{
		float[] f = {1,2,3};
		float[] g = f.clone();
		f[0] = 0;
		System.out.println(Arrays.toString(f));
		System.out.println(Arrays.toString(g));
	}
	
	void call(String s) {}
}
