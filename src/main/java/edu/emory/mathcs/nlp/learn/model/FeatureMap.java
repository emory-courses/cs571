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

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import edu.emory.mathcs.nlp.common.util.DSUtils;
import edu.emory.mathcs.nlp.common.util.FastUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class FeatureMap implements Serializable
{
	private static final long serialVersionUID = 5979391420237494905L;
	private Int2ObjectMap<Object2IntMap<String>> count_map;
	private List<Object2IntMap<String>> index_map;
	private int feature_size;
	
	public FeatureMap()
	{
		count_map = new Int2ObjectOpenHashMap<>();
		initIndices();
	}
	
	public void initIndices()
	{
		index_map = new ArrayList<>();
		feature_size = 1;
	}
	
	/**
	 * @param cutoff discards features whose frequencies are less than or equal to this cutoff.
	 * @return the total number of features.
	 */
	public int expand(int cutoff)
	{
		Object2IntMap<String> countMap;
		int type;
		
		for (Entry<Integer,Object2IntMap<String>> e : count_map.entrySet())
		{
			type = e.getKey();
			countMap = e.getValue();
			
			expandTypes(type);
			expandFeatures(countMap, index_map.get(type), cutoff);
		}
		
		count_map = new Int2ObjectOpenHashMap<>();
		return feature_size;
	}
	
	/** Called by {@link #expand(int)}. */
	private void expandTypes(int type)
	{
		if (index_map.size() <= type)
		{
			for (int i=index_map.size(); i<=type; i++)
				index_map.add(new Object2IntOpenHashMap<>());
		}
	}
	
	/** Called by {@link #expand(int)}. */
	private void expandFeatures(Object2IntMap<String> countMap, Object2IntMap<String> indexMap, int cutoff)
	{
		String feature;
		int    count;
		
		for (Entry<String,Integer> e : countMap.entrySet())
		{
			feature = e.getKey();
			count = e.getValue();
			
			if (!indexMap.containsKey(feature) && count > cutoff)
				indexMap.put(feature, feature_size++);
		}
	}
	
	public void add(int type, String value)
	{
		FastUtils.increment(count_map.computeIfAbsent(type, k -> new Object2IntOpenHashMap<String>()), value);
	}

	/** @return the index of the specific feature given the specific type if exists; otherwise, {@code -1}. */
	public int indexOf(int type, String feature)
	{
		return DSUtils.isRange(index_map, type) ? index_map.get(type).getOrDefault(feature, -1) : -1;
	}
	
	public int size()
	{
		return feature_size;
	}
	
	@Override
	public String toString()
	{
		return index_map.toString();
	}
}