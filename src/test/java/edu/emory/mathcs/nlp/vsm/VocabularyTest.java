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
package edu.emory.mathcs.nlp.vsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import edu.emory.mathcs.nlp.common.constant.StringConst;
import edu.emory.mathcs.nlp.vsm.util.Vocabulary;
import edu.emory.mathcs.nlp.vsm.util.Word;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class VocabularyTest
{
	@Test
	public void testVocabulary()
	{
		Vocabulary vocab = new Vocabulary();
		Word w, w0, w1, w2;
		
		w0 = vocab.add("A");
		w1 = vocab.add("B"); vocab.add("B");
		w2 = vocab.add("C");
		
		assertEquals(w0, vocab.get(0));
		assertEquals(w1, vocab.get(1));
		assertEquals(w2, vocab.get(2));
		assertEquals(0, vocab.indexOf("A"));
		assertEquals(1, vocab.indexOf("B"));
		assertEquals(2, vocab.indexOf("C"));
		assertEquals("A:1 B:2 C:1", vocab.toString());
		
		vocab.sort(0);
		assertEquals("B:2 A:1 C:1", vocab.toString());
		
		vocab.add("C"); vocab.add("D");
		assertEquals(1, vocab.indexOf("A"));
		assertEquals(0, vocab.indexOf("B"));
		assertEquals(2, vocab.indexOf("C"));
		assertEquals(3, vocab.indexOf("D"));
		assertEquals("B:2 A:1 C:2 D:1", vocab.toString());
		
		vocab.reduce();
		assertTrue(vocab.indexOf("A") < 0);
		assertEquals(0, vocab.indexOf("B"));
		assertEquals(1, vocab.indexOf("C"));
		assertEquals("B:2 C:2", vocab.toString());
		
		vocab.add("A"); vocab.add("D");
		assertEquals("B:2 C:2 A:1 D:1", vocab.toString());
		
//		      6
//		     / \
//		    0   5
//		       / \
//		      4   1
//		     / \
//		    3   2
		vocab.generateHuffmanCodes();
		w = vocab.get(0);	assertEquals("[0]"      , Arrays.toString(w.code));	assertEquals("[2]"      , Arrays.toString(w.point));
		w = vocab.get(1);	assertEquals("[1, 1]"   , Arrays.toString(w.code));	assertEquals("[2, 1]"   , Arrays.toString(w.point));
		w = vocab.get(2);	assertEquals("[1, 0, 1]", Arrays.toString(w.code));	assertEquals("[2, 1, 0]", Arrays.toString(w.point));
		w = vocab.get(3);	assertEquals("[1, 0, 0]", Arrays.toString(w.code));	assertEquals("[2, 1, 0]", Arrays.toString(w.point));
		
		vocab.add("C"); vocab.add("D"); vocab.add("E"); vocab.add("D");
		assertEquals("B:2 C:3 A:1 D:3 E:1", vocab.toString());
		
		vocab.sort(3);
		assertEquals("C:3 D:3", vocab.toString());
		
		vocab.reduce();
		assertEquals("C:3 D:3", vocab.toString());

		vocab.reduce();
		assertEquals(StringConst.EMPTY, vocab.toString());
		
//		assertEquals(0, vocab.indexOf("B"));
//		assertEquals(1, vocab.indexOf("A"));
//		assertEquals(Vocabulary.OOV, vocab.indexOf("C"));
//		assertEquals("B:2 C:3 A:1 D:3 E:1", vocab.toString());
//		
//		vocab.add("A"); vocab.add("C");
//		assertEquals(1, vocab.indexOf("A"));
//		assertEquals(0, vocab.indexOf("B"));
//		assertEquals(2, vocab.indexOf("C"));
//		assertEquals("B:2 A:3 C:1", vocab.toString());
//
//		vocab.reduce();
//		assertEquals(1, vocab.indexOf("A"));
//		assertEquals(0, vocab.indexOf("B"));
//		assertEquals(Vocabulary.OOV, vocab.indexOf("C"));
//		assertEquals("B:2 A:3", vocab.toString());
//		
//		vocab.reduce();
//		assertEquals("A:3", vocab.toString());
	}
}
