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

/**
 * @author Congzheng Song
 */
import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import edu.emory.mathcs.nlp.component.util.feature.Field;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;

public class DEPNodeTestCongzhengSong {
    public DEPNode[] getExample() throws Exception{
        /*
            1	Mr.	mr.	NNP	_	2	nn
            2	Vinken	vinken	NNP	_	3	nsubj
            3	is	be	VBZ	_	0	root
            4	chairman	chairman	NN	syn=PRD	3	attr
            5	of	of	IN	_	4	prep
            6	Elsevier	elsevier	NNP	_	7	nn
            7	N.V.	n.v.	NNP	_	5	pobj
            8	,	,	,	_	7	punct
            9	the	the	DT	_	12	det
            10	Dutch	dutch	NNP	_	12	nn
            11	publishing	publish	VBG	_	12	amod
            12	group	group	NN	_	7	appos
            13	.	.	.	_	3	punct
         */
        TSVReader<DEPNode> reader = new TSVReader<>(new DEPIndex(1, 2, 3, 4, 5, 6));
        reader.open(new FileInputStream("src/main/resources/dat/wsj_0001.dep"));
        reader.next();
        return reader.next();
    }

    @Test
    public void getSiblingTest() throws Exception{
        DEPNode node1 = new DEPNode(1, "He");
        DEPNode node2 = new DEPNode(2, "bought");
        DEPNode node3 = new DEPNode(3, "a");
        DEPNode node4 = new DEPNode(4, "car");

        node2.addDependent(node4, "dobj");
        node2.addDependent(node1, "nsubj");
        node4.addDependent(node3, "det");
        assertEquals(node4, node1.getRightNearestSibling());
        assertEquals(null, node1.getLeftNearestSibling());
//         Print the siblings for another example
        DEPNode[] nodes = getExample();
        System.out.println(nodes.length);
//        String word;
//        System.out.println("Sibling Test");
//        for(DEPNode node: nodes) {
//            word = node.getWordForm();
//            System.out.println(word + " left sibling 0: " + node.getLeftNearestSibling());
//            System.out.println(word + " left sibling 1: " + node.getLeftNearestSibling(1));
//            System.out.println(word + " right sibling 0: " + node.getRightNearestSibling());
//            System.out.println(word + " right sibling 1: " + node.getRightNearestSibling(1));
//        }
    }

    @Test
    public void getDependentTest(){
        DEPNode node1 = new DEPNode(1, "He");
        DEPNode node2 = new DEPNode(2, "bought");
        DEPNode node3 = new DEPNode(3, "a");
        DEPNode node4 = new DEPNode(4, "car");

        node2.addDependent(node4, "dobj");
        node2.addDependent(node1, "nsubj");
        node4.addDependent(node3, "det");

        assertEquals(node4, node2.getRightMostDependent());
        assertEquals(node1, node2.getLeftMostDependent());
        //by index
        assertEquals(node4, node2.getDependent(1));
        assertEquals(node1, node2.getDependent(0));
        //get list
        List<DEPNode> nodeList = new ArrayList<>();
        nodeList.add(node1);
        nodeList.add(node4);
        assertEquals(nodeList, node2.getDependentList());
        nodeList = new ArrayList<>();
        nodeList.add(node3);
        assertEquals(nodeList, node2.getGrandDependentList());
        assertEquals(nodeList, node4.getLeftDependentList());
    }

    @Test
    public void getDescendantTest(){
        DEPNode node1 = new DEPNode(1, "He");
        DEPNode node2 = new DEPNode(2, "bought");
        DEPNode node3 = new DEPNode(3, "a");
        DEPNode node4 = new DEPNode(4, "car");

        node2.addDependent(node4, "dobj");
        node2.addDependent(node1, "nsubj");
        node4.addDependent(node3, "det");

        List<DEPNode> nodeList = new ArrayList<>();
        nodeList.add(node1);
        nodeList.add(node4);
        assertEquals(nodeList, node2.getDescendantList(1));
        nodeList.add(node3);
        assertEquals(nodeList, node2.getDescendantList(2));
    }


    @Test
    public void otherTest(){
        DEPNode node1 = new DEPNode(1, "He");
        DEPNode node2 = new DEPNode(2, "bought");
        DEPNode node3 = new DEPNode(3, "a");
        DEPNode node4 = new DEPNode(4, "car");

        node2.addDependent(node4, "dobj");
        node2.addDependent(node1, "nsubj");
        node4.addDependent(node3, "det");
        //ancestor
        assertEquals(node2, node1.getLowestCommonAncestor(node4));
        //sub node list
        List<DEPNode> nodeList = new ArrayList<>();
        nodeList.add(node1);
        nodeList.add(node2);
        nodeList.add(node4);
        nodeList.add(node3);
        Collections.sort(nodeList);
        //sub node set
        Set<DEPNode> nodeSet = new HashSet<>();
        nodeSet.add(node1);
        nodeSet.add(node2);
        nodeSet.add(node3);
        nodeSet.add(node4);
        assertEquals(nodeSet, node2.getSubNodeSet());
        //sub categorization test
        assertEquals(">car", node2.getRightSubcategorization(Field.word_form));
        assertEquals("<He", node2.getLeftSubcategorization(Field.word_form));

    }
}