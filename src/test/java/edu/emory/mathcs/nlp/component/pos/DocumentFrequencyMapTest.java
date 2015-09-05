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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import edu.emory.mathcs.nlp.common.collection.node.NLPNode;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DocumentFrequencyMapTest
{
	@Test
	public void test()
	{
		DocumentFrequencyMap map = new DocumentFrequencyMap(2);
		NLPNode A = new NLPNode("A");
		NLPNode B = new NLPNode("B");
		NLPNode C = new NLPNode("C");
		
		map.add(new NLPNode[]{A});
		map.add(new NLPNode[]{B});
		map.add(new NLPNode[]{A,B,C});
		
		Set<String> set = map.create(1);
		System.out.println(set.toString());
		
		assertTrue (set.contains("a"));
		assertTrue (set.contains("b"));
		assertFalse(set.contains("c"));
	}
}
