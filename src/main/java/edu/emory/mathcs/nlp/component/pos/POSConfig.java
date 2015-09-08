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

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.io.InputStream;

import org.w3c.dom.Element;

import edu.emory.mathcs.nlp.common.util.XMLUtils;
import edu.emory.mathcs.nlp.component.util.NLPConfig;
import edu.emory.mathcs.nlp.component.util.NLPMode;
import edu.emory.mathcs.nlp.component.util.TSVReader;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSConfig extends NLPConfig<POSNode>
{
	public POSConfig()
	{
		super(NLPMode.pos);
	}
	
	public POSConfig(InputStream in)
	{
		super(NLPMode.pos, in);
	}

	public TSVReader<POSNode> getReader()
	{
		Element eReader = XMLUtils.getFirstElementByTagName(getModeElement(), READER);
		Object2IntMap<String> map = getFieldMap(eReader);
		
		int form  = map.get(FIELD_FORM)  - 1;
		int pos   = map.get(FIELD_POS)   - 1;
		int feats = map.get(FIELD_FEATS) - 1;
		
		return new TSVReader<>(new POSIndex(form, pos, feats));
	}
	
	public int getDocumentSize()
	{
		return 0;
	}
	
	public int getDocumentFrequencyCutoff()
	{
		return 0;
	}
	
	public double getAmbiguityClassThreshold()
	{
		return 0d;
	}
}
