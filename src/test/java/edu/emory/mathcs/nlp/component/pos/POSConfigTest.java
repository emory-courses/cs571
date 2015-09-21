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

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.Language;
import edu.emory.mathcs.nlp.component.util.train.Aggregation;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSConfigTest
{
	@Test
	public void test()
	{
		String filename = "src/test/resources/configuration/config_train_pos.xml";
		POSConfig config = new POSConfig(IOUtils.createFileInputStream(filename));
		POSIndex index = (POSIndex)config.getTSVIndex();

		assertEquals( 1, index.form);
		assertEquals( 3, index.pos);
		assertEquals(-1, index.lemma);
		assertEquals(-1, index.feats);
		
		assertEquals(0.4, config.getAmbiguityClassThreshold(), 0);
		assertEquals(Language.ENGLISH, config.getLanguage());
		
		Aggregation agg = config.getAggregation();
		assertEquals(0.01, agg.getToleranceDelta(), 0);
		assertEquals(5   , agg.getMaxTolerance());
	}
}
