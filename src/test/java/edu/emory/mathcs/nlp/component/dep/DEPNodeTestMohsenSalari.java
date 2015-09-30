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

import org.junit.Test;

/**
 * @author Mohsen Salari
 */
public class DEPNodeTestMohsenSalari
{
	@Test
	public void test() throws Exception
	{
		DEPNode node1 = new DEPNode(1,"Mohsen");
		DEPNode node2 = new DEPNode(2,"is");
		DEPNode node3 = new DEPNode(3,"spending");
		DEPNode node4 = new DEPNode(4,"so");
		DEPNode node5 = new DEPNode(5,"much");
		DEPNode node6 = new DEPNode(6,"time");
		DEPNode node7 = new DEPNode(7,"on");
		DEPNode node8 = new DEPNode(8,"NLP");
		
		node3.addDependent(node1,"nsubj");
		node3.addDependent(node2,"aux");
		node3.addDependent(node6,"dobj");
		node3.addDependent(node8,"nmod:on");
		node5.addDependent(node4,"advmod");
		node6.addDependent(node5,"dep");
		node8.addDependent(node7,"case");
		
		
		assertEquals(node5, node4.getHead());
		assertEquals(node6,node5.getHead());
		assertEquals(node6,node4.getGrandHead());
		
		Set<DEPNode> node4Ancestors = node4.getAncestorSet();
		Set<DEPNode> node4AncestorsOracle = new HashSet<>();
		node4AncestorsOracle.add(node3);
		node4AncestorsOracle.add(node5);
		node4AncestorsOracle.add(node6);
		assertEquals(node4Ancestors, node4AncestorsOracle);

		List<DEPNode> node3LeftDependantList = node3.getLeftDependentList();
		assertEquals(node1, node3LeftDependantList.get(0));
		assertEquals(node2, node3LeftDependantList.get(1));

		List<DEPNode> node3RightDependentList = node3.getRightDependentList();
		assertEquals(node6, node3RightDependentList.get(0));
		assertEquals(node8, node3RightDependentList.get(1));
		
		
		List<DEPNode> node3GrandDependentList = node3.getGrandDependentList();
		assertEquals(node5, node3GrandDependentList.get(0));
		assertEquals(node7, node3GrandDependentList.get(1));
		
		assertEquals(node3, node2.getLowestCommonAncestor(node5));
		
		List<DEPNode> node3SubNodeList = node3.getSubNodeList();
		assertEquals(node1, node3SubNodeList.get(0));
		assertEquals(node2, node3SubNodeList.get(1));
		assertEquals(node3, node3SubNodeList.get(2));
		assertEquals(node4, node3SubNodeList.get(3));
		assertEquals(node5, node3SubNodeList.get(4));
		assertEquals(node6, node3SubNodeList.get(5));
		assertEquals(node7, node3SubNodeList.get(6));
		assertEquals(node8, node3SubNodeList.get(7));
	}

}
