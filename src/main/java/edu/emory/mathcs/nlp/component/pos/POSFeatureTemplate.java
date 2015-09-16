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

import java.util.Arrays;

import edu.emory.mathcs.nlp.common.util.StringUtils;
import edu.emory.mathcs.nlp.component.util.feature.FeatureItem;
import edu.emory.mathcs.nlp.component.util.feature.FeatureTemplate;
import edu.emory.mathcs.nlp.component.util.feature.Field;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSFeatureTemplate<N extends POSNode> extends FeatureTemplate<N,POSState<N>>
{
	private static final long serialVersionUID = -243334323533999837L;
	
	public POSFeatureTemplate()	
	{
		init();
	}
	
	protected void init()
	{
		// 1-gram features 
		add(new FeatureItem<>(-2, Field.simplified_word_form));
		add(new FeatureItem<>(-1, Field.simplified_word_form));
		add(new FeatureItem<>( 0, Field.simplified_word_form));
		add(new FeatureItem<>( 1, Field.simplified_word_form));
		add(new FeatureItem<>( 2, Field.simplified_word_form));

		add(new FeatureItem<>(-1, Field.word_shape));
		add(new FeatureItem<>( 0, Field.word_shape));
		add(new FeatureItem<>( 1, Field.word_shape));

		add(new FeatureItem<>(-3, Field.pos_tag));
		add(new FeatureItem<>(-2, Field.pos_tag));
		add(new FeatureItem<>(-1, Field.pos_tag));
		add(new FeatureItem<>( 0, Field.ambiguity_class));
		add(new FeatureItem<>( 1, Field.ambiguity_class));
		add(new FeatureItem<>( 2, Field.ambiguity_class));
		add(new FeatureItem<>( 3, Field.ambiguity_class));

		// 2-gram features
		add(new FeatureItem<>(-2, Field.uncapitalized_simplified_word_form), new FeatureItem<>(-1, Field.uncapitalized_simplified_word_form));
		add(new FeatureItem<>(-1, Field.uncapitalized_simplified_word_form), new FeatureItem<>( 0, Field.uncapitalized_simplified_word_form));
		add(new FeatureItem<>( 0, Field.uncapitalized_simplified_word_form), new FeatureItem<>( 1, Field.uncapitalized_simplified_word_form));
		add(new FeatureItem<>( 1, Field.uncapitalized_simplified_word_form), new FeatureItem<>( 2, Field.uncapitalized_simplified_word_form));
		add(new FeatureItem<>(-1, Field.uncapitalized_simplified_word_form), new FeatureItem<>(+1, Field.uncapitalized_simplified_word_form));

		add(new FeatureItem<>(-2, Field.pos_tag)        , new FeatureItem<>(-1, Field.pos_tag));
		add(new FeatureItem<>(-1, Field.pos_tag)        , new FeatureItem<>( 1, Field.ambiguity_class));
		add(new FeatureItem<>( 1, Field.ambiguity_class), new FeatureItem<>( 2, Field.ambiguity_class));

		// 3-gram features
		add(new FeatureItem<>(-2, Field.pos_tag), new FeatureItem<>(-1, Field.pos_tag)        , new FeatureItem<>(0, Field.ambiguity_class));
		add(new FeatureItem<>(-2, Field.pos_tag), new FeatureItem<>(-1, Field.pos_tag)        , new FeatureItem<>(1, Field.ambiguity_class));
		add(new FeatureItem<>(-1, Field.pos_tag), new FeatureItem<>( 0, Field.ambiguity_class), new FeatureItem<>(1, Field.ambiguity_class));
		add(new FeatureItem<>(-1, Field.pos_tag), new FeatureItem<>( 1, Field.ambiguity_class), new FeatureItem<>(2, Field.ambiguity_class));

		// affix features
		add(new FeatureItem<>(0, Field.prefix, 2));
		add(new FeatureItem<>(0, Field.prefix, 3));
		add(new FeatureItem<>(0, Field.suffix, 1));
		add(new FeatureItem<>(0, Field.suffix, 2));
		add(new FeatureItem<>(0, Field.suffix, 3));
		add(new FeatureItem<>(0, Field.suffix, 4));
		
		// orthographic features
		addSet(new FeatureItem<>(0, Field.orthographic));
		
		// boolean features
		addSet(new FeatureItem<>(0, Field.binary));
	}
	
//	========================= FEATURE EXTRACTORS =========================
	
	@Override
	protected String getFeature(FeatureItem<?> item)
	{
		N node = state.getNode(item.window);
		if (node == null) return null;
		
		switch (item.field)
		{
		case word_form: return node.getWordForm();
		case simplified_word_form: return node.getSimplifiedWordForm();
		case uncapitalized_simplified_word_form: return StringUtils.toLowerCase(node.getSimplifiedWordForm());
		case word_shape: return node.getWordShape(2);
		case pos_tag: return node.getPOSTag();
		case ambiguity_class: return state.getAmbiguityClass(node);
		case prefix: return getPrefix(node, (Integer)item.value);
		case suffix: return getSuffix(node, (Integer)item.value);
		default: throw new IllegalArgumentException("Unsupported feature: "+item.field);
		}		
	}
	
	@Override
	protected String[] getFeatures(FeatureItem<?> item)
	{
		N node = state.getNode(item.window);
		if (node == null) return null;
		
		switch (item.field)
		{
		case orthographic: return getOrthographicFeatures(node);
		case binary: return getBinaryFeatures(node);
		default: throw new IllegalArgumentException("Unsupported feature: "+item.field);
		}
	}
	
	/** The prefix cannot be the entire word (e.g., getPrefix("abc", 3) -> null). */
	protected String getPrefix(N node, int n)
	{
		String s = node.getSimplifiedWordForm();
		return (n < s.length()) ? StringUtils.toLowerCase(s.substring(0, n)) : null;
	}
	
	/** The suffix cannot be the entire word (e.g., getSuffix("abc", 3) -> null). */
	protected String getSuffix(N node, int n)
	{
		String s = node.getSimplifiedWordForm();
		return (n < s.length()) ? StringUtils.toLowerCase(s.substring(s.length()-n)) : null;
	}
	
	protected String[] getOrthographicFeatures(N node)
	{
		String[] t = node.getOrthographic(state.isFirst(node));
		return t.length == 0 ? null : t;
	}
	
	protected String[] getBinaryFeatures(N node)
	{
		String[] values = new String[2];
		int index = 0;
		
		if (state.isFirst(node)) values[index++] = "0";
		if (state.isLast (node)) values[index++] = "1";
		
		return (index == 0) ? null : (index == values.length) ? values : Arrays.copyOf(values, index);
	}
}
