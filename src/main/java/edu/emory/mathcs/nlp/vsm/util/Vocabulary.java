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
package edu.emory.mathcs.nlp.vsm.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.emory.mathcs.nlp.common.util.Joiner;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Vocabulary implements Serializable
{
	private static final long serialVersionUID = 5406441768049538210L;
	public static final int MAX_CODE_LENGTH = 40;
	
	private Object2IntMap<String> index_map;
	private List<Word> word_list;
	private int min_reduce;
	
	public Vocabulary()
	{
		index_map  = new Object2IntOpenHashMap<>();
		word_list  = new ArrayList<>();
		min_reduce = 1;
	}
	
	/**
	 * Adds the word to the vocabulary if absent, and increments its count by 1. 
	 * @return the word object either already existing or newly introduced.
	 */
	public Word add(String word)
	{
		int index = index_map.computeIfAbsent(word, k -> size());
		Word w;
		
		if (index < size())
		{
			w = get(index);
			w.increment(1);
		}
		else
		{
			w = new Word(word, 1);
			word_list.add(w);
		}
		
		return w;
	}
	
	public Word get(int index)
	{
		return word_list.get(index);
	}
	
	/** @return index of the word if exists; otherwise, -1. */
	public int indexOf(String word)
	{
		return index_map.getOrDefault(word, -1);
	}
	
	public int size()
	{
		return word_list.size();
	}
	
	public List<Word> list()
	{
		return word_list;
	}
	
	/**
	 * Sorts {@link #word_list} by count in descending order.  
	 * @param minCount words whose counts are less than the minimum count will be discarded.
	 * @return total number of word counts after sorting.
	 */
	public long sort(int minCount)
	{
		return reduce(minCount, true);
	}
	
	/**
	 * Reduces the vocabulary by removing infrequent words.
	 * @return total number of word counts after reducing.
	 */
	public long reduce()
	{
		return reduce(++min_reduce, false);
	}
	
	long reduce(int minCount, boolean sort)
	{
		ArrayList<Word> list = new ArrayList<>(size());
		long count = 0;
		
		for (Word w : word_list)
		{
			if (w.count >= minCount)
			{
				count += w.count;
				list.add(w);
			}
		}
		
		if (sort) Collections.sort(list, Collections.reverseOrder());
		list.trimToSize(); word_list = list;
		
		index_map = new Object2IntOpenHashMap<>(size());
		for (int i=0; i<size(); i++) index_map.put(get(i).word, i);
		return count;
	}
	
	/**
	 * Assigns the Huffman code to each word using its count.
	 * PRE: {@link #word_list} is already sorted by count in descending order.
	 */
	public void generateHuffmanCodes()
	{
		int i, j, len, pos1, pos2, min1, min2;
		final int treeSize  = size() * 2 - 1;
		long[] count  = new long[treeSize];
		byte[] binary = new byte[treeSize];
		int [] parent = new int [treeSize];
		
		for (i=0     ; i<size()  ; i++) count[i] = get(i).count;
		for (i=size(); i<treeSize; i++)	count[i] = Long.MAX_VALUE;
		pos1 = size() - 1;
		pos2 = pos1 + 1;
		
		// create binary Huffman tree
		for (i=size(); i<treeSize; i++)
		{
			min1 = (pos1 < 0) ? pos2++ : (count[pos1] < count[pos2]) ? pos1-- : pos2++;
			min2 = (pos1 < 0) ? pos2++ : (count[pos1] < count[pos2]) ? pos1-- : pos2++;
			
			count[i] = count[min1] + count[min2];
			parent[min1] = i;
			parent[min2] = i;
			binary[min2] = 1;
		}
		
		byte[] code  = new byte[MAX_CODE_LENGTH];
		int [] point = new int [MAX_CODE_LENGTH];
		Word w;
		
		// assign binary code to each word
		for (i=0; i<size(); i++)
		{
			len = 0;
			j = i;
			
			do
			{
				code [len] = binary[j];
				point[len] = j;
				j = parent[j];
				len++;
			}
			while (j != treeSize - 1);
			
			w = get(i);
			w.code  = new byte[len];
			w.point = new int [len];
			w.point[0] = size() - 2; // = treeSize - size() - 1
			
			for (j=0; j<len; j++)
			{
				w.code[len-j-1] = code[j];
				if (j > 0) w.point[len-j] = point[j] - size();
			}
		}
	}
	
	@Override
	public String toString()
	{
		return Joiner.join(word_list, " ");
	}
}