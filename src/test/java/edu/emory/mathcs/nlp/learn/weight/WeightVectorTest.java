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
package edu.emory.mathcs.nlp.learn.weight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.emory.mathcs.nlp.common.collection.tuple.Pair;
import edu.emory.mathcs.nlp.learn.util.BinomialLabel;
import edu.emory.mathcs.nlp.learn.util.Prediction;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.SparseVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class WeightVectorTest
{
	@Test
	public void testBinomialWeightVector()
	{
		WeightVector w = new BinomialWeightVector(5);
		
		SparseVector x = new SparseVector();
		x.add(new IndexValuePair(0, 4));
		x.add(new IndexValuePair(2, 6));

		SparseVector y = new SparseVector();
		y.add(new IndexValuePair(3, 2));
		y.add(new IndexValuePair(4, 2));
		
		w.add(-1, 1, 1);
		w.update(x, -1, 0.5f);
		w.update(y, -1, (i,j) -> (float)-j);
		assertEquals(-6, w.get(-1, 3), 0);
		assertEquals("[2.0, 1.0, 3.0, -6.0, -8.0]", w.toString());
		
		Prediction p = w.predictBest(x);
		assertEquals(BinomialLabel.POSITIVE, p.getLabel());
		assertEquals(26, p.getScore(), 0);
		
		p = w.predictBest(y);
		assertEquals(BinomialLabel.NEGATIVE, p.getLabel());
		assertEquals(-28, p.getScore(), 0);

		assertTrue (w.isBinomial());
		assertFalse(w.expand(-1, 3));
		assertTrue (w.expand(-1, 6));
		assertEquals("[2.0, 1.0, 3.0, -6.0, -8.0, 0.0]", w.toString());
	}
	
	@Test
	public void testMultinomialWeightVector()
	{
		WeightVector w = new MultinomialWeightVector(3,4);
		
		SparseVector x = new SparseVector();
		x.add(new IndexValuePair(0, 4));
		x.add(new IndexValuePair(2, 6));

		SparseVector y = new SparseVector();
		y.add(new IndexValuePair(1, 2));
		y.add(new IndexValuePair(3, 2));
		
		w.add(0, 1, 1);
		w.update(x, 1, 0.5f);
		w.update(y, 2, (i,j) -> (float)-j);
		assertEquals(3, w.get(1, 2), 0);
		assertEquals("[0.0, 2.0, 0.0, 1.0, 0.0, -2.0, 0.0, 3.0, 0.0, 0.0, 0.0, -6.0]", w.toString());
		
		Prediction p = w.predictBest(x);
		assertEquals( 1, p.getLabel());
		assertEquals(26, p.getScore(), 0);
		
		Pair<Prediction,Prediction> p2 = w.predictTop2(y);
		assertEquals(0, p2.o1.getLabel());
		assertEquals(2, p2.o1.getScore(), 0);
		assertEquals(1, p2.o2.getLabel());
		assertEquals(0, p2.o2.getScore(), 0);
		
		Prediction[] ps = w.predictAll(y);
		assertEquals(  0, ps[0].getLabel());
		assertEquals(  2, ps[0].getScore(), 0);
		assertEquals(  1, ps[1].getLabel());
		assertEquals(  0, ps[1].getScore(), 0);
		assertEquals(  2, ps[2].getLabel());
		assertEquals(-16, ps[2].getScore(), 0);

		assertFalse(w.isBinomial());
		assertFalse(w.expand(2, 5));
		assertFalse(w.expand(4, 3));

		w = new MultinomialWeightVector(3, 2);
		w.fill(1);
		assertTrue(w.expand(3, 4));
		assertEquals("[1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]", w.toString());
		
		w = new MultinomialWeightVector(3, 2);
		w.fill(1);
		assertTrue(w.expand(5, 2));
		assertEquals("[1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0]", w.toString());
		
		w = new MultinomialWeightVector(3, 2);
		w.fill(1);
		assertTrue(w.expand(4, 3));
		assertEquals("[1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0]", w.toString());
	}
}
