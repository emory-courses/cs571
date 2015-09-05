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
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import edu.emory.mathcs.nlp.common.collection.node.NLPNode;
import edu.emory.mathcs.nlp.common.util.FastUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DocumentFrequencyMap implements Serializable
{
	private static final long serialVersionUID = 7752242285067642929L;
	private final int DOCUMENT_SIZE; // the max number of sentences
	private Object2IntMap<String> document_frequency;
	private Set<String> bag_of_words;
	private int sentence_count;
	
	public DocumentFrequencyMap(int documentSize)
	{
		DOCUMENT_SIZE = documentSize;
		
		document_frequency = new Object2IntOpenHashMap<>();
		bag_of_words       = new HashSet<>();
		sentence_count     = 0;
	}
	
	public <N extends NLPNode>void add(N[] nodes)
	{
		for (N node : nodes) bag_of_words.add(toKey(node));
		if (DOCUMENT_SIZE <= ++sentence_count) populate();
	}
	
	public Set<String> create(int cutoff)
	{
		if (!bag_of_words.isEmpty()) populate();
		return document_frequency.entrySet().stream().filter(e -> e.getValue() > cutoff).map(e -> e.getKey()).collect(Collectors.toSet());
	}
	
	private void populate()
	{
		bag_of_words.forEach(w -> FastUtils.increment(document_frequency, w));
		bag_of_words.clear();
		sentence_count = 0;
	}
	
	private String toKey(NLPNode node)
	{
		return node.getLowerSimplifiedWordForm();
	}
}
