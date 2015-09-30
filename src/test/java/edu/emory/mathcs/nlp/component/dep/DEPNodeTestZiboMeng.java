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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

import org.junit.Test;

import edu.emory.mathcs.nlp.component.util.feature.Direction;
import edu.emory.mathcs.nlp.component.util.feature.Field;

/**
 * @author Zibo Meng
 */
public class DEPNodeTestZiboMeng
{
	@Test
	public void testGetters()
	{
		DEPNode node0 = new DEPNode();
		DEPNode node1 = new DEPNode(1, "He");
		DEPNode node2 = new DEPNode(2, "bought");
		DEPNode node3 = new DEPNode(3, "a");
		DEPNode node4 = new DEPNode(4, "car");
		
		node0.addDependent(node2, "root");
		node2.addDependent(node4, "dobj");
		node2.addDependent(node1, "nsubj");
		node4.addDependent(node3, "det");
		
		assertEquals("root", node2.getLabel());
		assertEquals(node0, node2.getHead());
		assertEquals(node2, node4.getHead());
		assertEquals(node0, node4.getGrandHead());
		
		assertEquals(node1, node2.getDependent(0));
		assertEquals(node4, node2.getDependent(1));
		
		assertEquals(0, node2.getDependentIndex(node1));
		assertEquals(1, node2.getDependentIndex(node4));
		assertEquals(-1, node2.getDependentIndex(node0));
		
		assertEquals(2, node2.getDependentSize());

		// confusing
		assertEquals(null , node1.getLeftNearestSibling());
		assertEquals(null , node1.getLeftNearestSibling(1));
		assertEquals(node4, node1.getRightNearestSibling());
		assertEquals(null , node1.getRightNearestSibling(1));

		// confusing
		assertEquals(node1, node4.getLeftNearestSibling());
		assertEquals(null , node4.getLeftNearestSibling(1));
		assertEquals(null , node4.getRightNearestSibling());
		assertEquals(null , node4.getRightNearestSibling(1));

		assertEquals(node1, node2.getLeftMostDependent());
		assertEquals(node1, node2.getLeftNearestDependent());
		assertEquals(null , node2.getLeftMostDependent(1));
		assertEquals(null , node2.getLeftNearestDependent(1));
		
		assertEquals(node4, node2.getRightMostDependent());
		assertEquals(node4, node2.getRightNearestDependent());
		assertEquals(null, node2.getRightMostDependent(1));
		assertEquals(null, node2.getRightNearestDependent(1));
		
		BiPredicate<DEPNode,String> biPredicate = (a, b) -> a.dependency_label.equals(b);  
		assertEquals(node1, node2.getFirstDependent("nsubj", biPredicate));
		Pattern pattern = Pattern.compile("[a-z]*");
		assertEquals(node1, node2.getFirstDependentByLabel(pattern));
		
		assertEquals(node1, node2.getDependentListByLabel("nsubj").get(0));
		Set<String> labels = new HashSet<>();
		labels.add("nsubj");
		labels.add("dobj");
		List<DEPNode> tmp = node2.getDependentListByLabel(labels);
		assertEquals(node1, tmp.get(0));
		assertEquals(node4, tmp.get(1));
		List<DEPNode> tmp1 = node2.getDependentListByLabel(pattern);
		assertEquals(tmp1.get(0), tmp.get(0));
		assertEquals(tmp1.get(1), tmp.get(1));
		
		assertEquals(1, node2.getLeftDependentList().size());
		assertEquals(node1, node2.getLeftDependentList().get(0));
		
		assertEquals(2, node0.getGrandDependentList().size());
		assertEquals(node1, node0.getGrandDependentList().get(0));
		assertEquals(node4, node0.getGrandDependentList().get(1));

		assertEquals(3, node0.getDescendantList(2).size());
		assertEquals(4, node0.getDescendantList(3).size());
		
		assertEquals(4, node2.getSubNodeList().size());
		assertEquals(node2, node2.getSubNodeList().get(1));
		assertEquals(node1, node2.getSubNodeList().get(0));
		assertEquals(node3, node2.getSubNodeList().get(2));


		assertEquals(1, node2.getSubNodeIDSortedArray()[0]);
		assertEquals(2, node2.getSubNodeIDSortedArray()[1]);
		
		assertEquals("<", node2.getValency(Direction.left));
		assertEquals(">", node2.getValency(Direction.right));
		assertEquals(null, node2.getValency(Direction.up));
		assertEquals(null, node2.getValency(Direction.down));
		assertEquals("<->", node2.getValency(Direction.all));
		
		assertEquals("<He", node2.getSubcategorization(Direction.left, Field.word_form));
		assertEquals(">car", node2.getSubcategorization(Direction.right, Field.word_form));
		assertEquals("<He>car", node2.getSubcategorization(Direction.all, Field.word_form));
		
		assertEquals("|He|bought", node2.getPath(node1, Field.word_form));
		assertEquals("^car^bought|He", node4.getPath(node1, Field.word_form));
		
		assertEquals(node2, node1.getLowestCommonAncestor(node4));
		assertEquals(node2, node2.getLowestCommonAncestor(node4));
	}

	@Test
	public void testSetterBooleans()
	{
		DEPNode node0 = new DEPNode();
		DEPNode node1 = new DEPNode(1, "He");
		DEPNode node2 = new DEPNode(2, "bought");
		DEPNode node3 = new DEPNode(3, "a");
		DEPNode node4 = new DEPNode(4, "car");
		
		node0.addDependent(node2, "root");
		node2.addDependent(node4, "dobj");
		node2.addDependent(node1, "nsubj");
		node4.addDependent(node3, "det");
		
		assertEquals("root", node2.dependency_label);
		node2.setLabel("test");
		assertEquals("test", node2.dependency_label);
		node2.setLabel("root");
		
		assertEquals(node2, node4.dependency_head);
		node4.setHead(node0);
		assertEquals(node0, node4.dependency_head);
		node4.setHead(node2);
		
		assertEquals(true, node4.hasHead());
		assertEquals(true, node4.containsDependent(node3));
		assertEquals(true, node4.isLabel("dobj"));
		assertEquals(true, node4.isDependentOf(node2));		
		assertEquals(true, node4.isSiblingOf(node1));		
		
		node4.clearDependencies();

		assertEquals(false, node4.hasHead());
		assertEquals(false, node4.containsDependent(node3));
		assertEquals(false, node4.isLabel("dobj"));
		assertEquals(false, node4.isDependentOf(node2));		
		assertEquals(false, node4.isSiblingOf(node1));		
	}
}
