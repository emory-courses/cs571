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

import java.util.List;

import edu.emory.mathcs.nlp.component.util.FeatMap;
import edu.emory.mathcs.nlp.component.util.TSVIndex;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSIndex implements TSVIndex<POSNode>
{
	int form;
	int pos;
	int feats;
	
	public POSIndex(int form, int pos)
	{
		set(form, pos, -1);
	}
	
	public POSIndex(int form, int pos, int feats)
	{
		set(form, pos, feats);
	}
	
	public void set(int form, int pos, int feats)
	{
		this.form  = form;
		this.pos   = pos;
		this.feats = feats;
	}

	@Override
	public POSNode[] toNodeList(List<String[]> values)
	{
		return values.stream().map(v -> create(v)).toArray(POSNode[]::new);
	}
	
	public POSNode create(String[] values)
	{
		String  f = (form  >= 0) ? values[form] : null;
		String  t = (pos   >= 0) ? values[pos] : null;
		FeatMap m = (feats >= 0) ? new FeatMap(values[feats]) : new FeatMap();
		return new POSNode(f, t, m);
	}
}
