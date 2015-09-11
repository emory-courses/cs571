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
package edu.emory.mathcs.nlp.component.pos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.emory.mathcs.nlp.component.util.node.NLPNode;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AmbiguityClassMapTest
{
	@Test
	public void test()
	{
		AmbiguityClassMap map = new AmbiguityClassMap();
		
		map.add(new POSNode("A", "a1"));
		map.add(new POSNode("A", "a2"));
		map.add(new POSNode("B", "b1"));
		map.add(new POSNode("B", "b2"));
		map.add(new POSNode("B", "b2"));
		
		map.expand(0.4);
		
		assertEquals("a2_a1", map.get(new NLPNode("A")));
		assertEquals("b2"   , map.get(new NLPNode("B")));
		assertEquals(null   , map.get(new NLPNode("C")));
		
		map.add(new POSNode("C", "c1"));
		map.add(new POSNode("C", "c1"));
		map.add(new POSNode("C", "c2"));
		map.expand(0.3);

		assertEquals("c2_c1", map.get(new NLPNode("C")));
	}
}
