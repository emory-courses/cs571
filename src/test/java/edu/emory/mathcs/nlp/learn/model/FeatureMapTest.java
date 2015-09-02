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


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class FeatureMapTest
{
//	@Test
	public void test()
	{
		FeatureMap map = new FeatureMap();
		
		map.add(0, "A");
		map.add(0, "A");
		map.add(0, "B");
		map.add(0, "C");
		map.add(0, "C");
		
		map.add(1, "A");
		map.add(1, "B");
		map.add(1, "B");
		map.add(1, "C");
		map.add(1, "C");
		
		map.expand(1);
		System.out.println(map.toString());
		
		map.add(0, "B");
		map.add(0, "B");
		map.add(2, "B");
		
		map.expand(1);
		System.out.println(map.toString());
	}
}
