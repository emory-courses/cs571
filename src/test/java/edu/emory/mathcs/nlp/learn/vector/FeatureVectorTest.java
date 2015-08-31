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
package edu.emory.mathcs.nlp.learn.vector;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class FeatureVectorTest extends TestCase
{
	@Test
	public void testSparseFeatureVector()
	{
		SparseVector vector = new SparseVector();
		
		vector.add(2);
		vector.add(1, 0.2f);
		vector.add(4, 0.3f);
		vector.add(3);
		
		assertEquals(4, vector.size());
		assertEquals("4:0.3", vector.get(2).toString());
		assertEquals("2:1.0 1:0.2 4:0.3 3:1.0", vector.toString());
		
		vector.sort();
		assertEquals("1:0.2 2:1.0 3:1.0 4:0.3", vector.toString());
	}
	
//	@Test
//	public void testStringFeatureVector()
//	{
//		StringFeatureVector vector = new StringFeatureVector();
//
//		vector.add((short)2, "B");
//		vector.add((short)1, "A", 0.2);
//		vector.add((short)4, "D", 0.3);
//		vector.add((short)3, "C");
//		
//		assertEquals("2:B:1.0 1:A:0.2 4:D:0.3 3:C:1.0", vector.toString());
//		
//		vector.sort();
//		assertEquals("1:A:0.2 2:B:1.0 3:C:1.0 4:D:0.3", vector.toString());
//	}
}
