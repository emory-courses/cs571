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
package edu.emory.mathcs.nlp.component.dep;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import edu.emory.mathcs.nlp.component.util.feature.Direction;
import edu.emory.mathcs.nlp.component.util.feature.Field;

/**
 * @author Alex Lutz
 */
public class DEPNodeTestAlexLutz
{
	@Test
	public void test()
	{
		DEPNode node1 = new DEPNode(1, "Mr");
		DEPNode node2 = new DEPNode(2, "John");
		DEPNode node3 = new DEPNode(3, "loves");
		DEPNode node4 = new DEPNode(4, "to");
		DEPNode node5 = new DEPNode(5, "ride");
		DEPNode node7 = new DEPNode(7, "bike");
		DEPNode node8 = new DEPNode(8, "everywhere");
		DEPNode node9 = new DEPNode(9, "around");
		DEPNode node10 = new DEPNode(10, "town");
		
		node3.addDependent(node2, "nsubj");
		node2.addDependent(node1);
		node5.addDependent(node3, "xcomp");
		node4.addDependent(node5, "aux");
		node5.addDependent(node7, "dobj");
		node5.addDependent(node8, "advmod");
		node5.addDependent(node9, "prep");
		node9.addDependent(node10, "pobj");
		List<DEPNode> list = new ArrayList<>(Arrays.asList(node7, node8, node9));
		List<DEPNode> list2 = new ArrayList<>(Arrays.asList(node2, node10));
		List<DEPNode> list3 = new ArrayList<>(Arrays.asList(node1, node2, node3));
		Set<DEPNode> set = new HashSet<>(Arrays.asList(node4, node5, node9));
		
		assertEquals("ride", node10.getGrandHead().getWordForm());
		assertEquals(node7, node8.getLeftNearestSibling());
		assertEquals(node8, node7.getRightNearestSibling());
		assertEquals(node7, node5.getRightNearestDependent());
		assertEquals(list, node5.getRightDependentList());
		assertEquals(list2, node5.getGrandDependentList());
		assertEquals(list3, node3.getSubNodeList());
		assertEquals(1, node5.getDependentIndex(node7));
		assertEquals("<", node3.getValency(Direction.left));
		assertEquals(">>", node5.getValency(Direction.right));
		assertEquals("<->>", node5.getValency(Direction.all));
		assertEquals(">prep>advmod>dobj", node5.getRightSubcategorization(Field.dependency_label));
		assertEquals("|pobj|prep", node5.getPath(node10, Field.dependency_label));
		assertEquals(set, node10.getAncestorSet());
		assertEquals(node5, node2.getLowestCommonAncestor(node10));
		assertEquals(true, node5.containsDependent("prep", DEPNode::isLabel));
		assertEquals(null, node5.getAnyDescendantByPOSTag("NN"));
	}
}
