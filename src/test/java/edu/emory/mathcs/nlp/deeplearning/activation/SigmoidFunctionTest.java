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
package edu.emory.mathcs.nlp.deeplearning.activation;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SigmoidFunctionTest
{
//	@Test
	public void test()
	{
		SigmoidFunction s1 = new SigmoidFunction(3500, -6, 6);
		double d1, d2, diff, sum = 0; int t = 0;
		
		for (double d=-3; d<=3; d+=0.01)
		{
			d1 = s1.get(d);
			d2 = 1d / (1d + Math.exp(-d));
			diff = Math.abs(d1 - d2);
			sum += diff; t++;
			System.out.printf("%10.4f: %5.4f %5.4f %5.4f\n", d, diff, d2, d1);
		}
		
		System.out.println(s1.get(0));
		System.out.println(sum/t);
	}
}
