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

import edu.emory.mathcs.nlp.component.util.node.FeatMap;
import edu.emory.mathcs.nlp.component.util.node.NLPNode;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSNode extends NLPNode
{
	private static final long serialVersionUID = -8563108117037742010L;
	protected String  pos_tag;
	protected FeatMap feat_map;
	
	public POSNode(String form)
	{
		super(form);
		set(null, new FeatMap());
	}
	
	public POSNode(String form, String tag)
	{
		super(form);
		set(tag, new FeatMap());
	}
	
	public POSNode(String form, String tag, FeatMap map)
	{
		super(form);
		set(tag, map);
	}
	
	private void set(String tag, FeatMap map)
	{
		setPOSTag(tag);
		setFeatMap(map);
	}
	
//	============================== POS Tag ==============================
	
	public String getPOSTag()
	{
		return pos_tag;
	}

	public String setPOSTag(String tag)
	{
		String t = pos_tag;
		pos_tag = tag;
		return t;
	}
	
//	============================== Feature Map ==============================
	
	public FeatMap getFeatMap()
	{
		return feat_map;
	}
	
	public void setFeatMap(FeatMap map)
	{
		feat_map = map;
	}
	
	public String getFeat(String key)
	{
		return feat_map.get(key);
	}
	
	public void putFeat(String key, String value)
	{
		feat_map.put(key, value);
	}
	
	public String removeFeat(String key)
	{
		return feat_map.remove(key);
	}
}
