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
package edu.emory.mathcs.nlp.learn.model;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import edu.emory.mathcs.nlp.common.util.FastUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class LabelMap implements Serializable
{
	private static final long serialVersionUID = -2352189443060761041L;
	private Object2IntMap<String> count_map;
	private Object2IntMap<String> index_map;
	private List<String> list;
	
	public LabelMap()
	{
		count_map = new Object2IntOpenHashMap<>();
		initIndices();
	}
	
	public void initIndices()
	{
		index_map = new Object2IntOpenHashMap<>();
		list = new ArrayList<>();		
	}
	
	/**
	 * @param cutoff discards labels whose frequencies are less than or equal to this cutoff.
	 * @return the total number of labels.
	 */
	public int expand(int cutoff)
	{
		String label;
		int    count;
		
		for (Entry<String,Integer> e : count_map.entrySet())
		{
			label = e.getKey();
			count = e.getValue();
			
			if (!index_map.containsKey(label) && count > cutoff)
			{
				list.add(label);
				index_map.put(label, index_map.size());
			}
		}

		count_map = new Object2IntOpenHashMap<>();
		return list.size();
	}
	
	public void add(String label)
	{
		FastUtils.increment(count_map, label);
	}
	
	public int indexOf(String label)
	{
		return index_map.getOrDefault(label, -1);
	}
	
	public String getLabel(int index)
	{
		return list.get(index);
	}
	
	public List<String> getLabelList()
	{
		return list;
	}
	
	public int size()
	{
		return list.size();
	}
	
	@Override
	public String toString()
	{
		return list.toString();
	}
}