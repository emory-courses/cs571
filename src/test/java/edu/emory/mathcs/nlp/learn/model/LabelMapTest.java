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
package edu.emory.mathcs.nlp.learn.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class LabelMapTest
{
	@Test
	public void test()
	{
		LabelMap map = new LabelMap();
		
		map.add("A");	map.add("B");	map.add("C");
		map.add("A");	map.add("A");	map.add("C");
		map.expand(1);
		
		System.out.println(map.getLabelList().toString());
		
		assertEquals("A", map.getLabel(map.indexOf("A")));
		assertEquals("C", map.getLabel(map.indexOf("C")));

		assertEquals(-1, map.indexOf("B"));
		assertEquals( 0, map.indexOf(map.getLabel(0)));
		assertEquals( 1, map.indexOf(map.getLabel(1)));
		
		map.add("B");
		map.expand(0);
		
		assertEquals("B", map.getLabel(map.indexOf("B")));
		assertEquals(  2, map.indexOf(map.getLabel(2)));
	}
}
