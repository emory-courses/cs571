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

import java.util.ArrayList;
import java.util.Arrays;

import edu.emory.mathcs.nlp.common.util.StringUtils;
import edu.emory.mathcs.nlp.component.util.feature.FeatureItem;
import edu.emory.mathcs.nlp.component.util.feature.FeatureTemplate;

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
	
	@SuppressWarnings("unchecked")
	protected void init()
	{
		feature_list = new ArrayList<>();
		feature_set  = new ArrayList<>();
		
		// 1-gram features 
		add(new FeatureItem<>(-2, N::getSimplifiedWordForm));
		add(new FeatureItem<>(-1, N::getSimplifiedWordForm));
		add(new FeatureItem<>( 0, N::getSimplifiedWordForm));
		add(new FeatureItem<>( 1, N::getSimplifiedWordForm));
		add(new FeatureItem<>( 2, N::getSimplifiedWordForm));

		add(new FeatureItem<>(-1, this::getWordShape));
		add(new FeatureItem<>( 0, this::getWordShape));
		add(new FeatureItem<>( 1, this::getWordShape));

		add(new FeatureItem<>(-3, N::getPOSTag));
		add(new FeatureItem<>(-2, N::getPOSTag));
		add(new FeatureItem<>(-1, N::getPOSTag));
		add(new FeatureItem<>( 0, this::getAmbiguityClass));
		add(new FeatureItem<>( 1, this::getAmbiguityClass));
		add(new FeatureItem<>( 2, this::getAmbiguityClass));
		add(new FeatureItem<>( 3, this::getAmbiguityClass));

		// 2-gram features
		add(new FeatureItem<>(-2, this::getUncapitalizedSimplifiedWordForm), new FeatureItem<>(-1, this::getUncapitalizedSimplifiedWordForm));
		add(new FeatureItem<>(-1, this::getUncapitalizedSimplifiedWordForm), new FeatureItem<>( 0, this::getUncapitalizedSimplifiedWordForm));
		add(new FeatureItem<>( 0, this::getUncapitalizedSimplifiedWordForm), new FeatureItem<>( 1, this::getUncapitalizedSimplifiedWordForm));
		add(new FeatureItem<>( 1, this::getUncapitalizedSimplifiedWordForm), new FeatureItem<>( 2, this::getUncapitalizedSimplifiedWordForm));
		add(new FeatureItem<>(-1, this::getUncapitalizedSimplifiedWordForm), new FeatureItem<>(+1, this::getUncapitalizedSimplifiedWordForm));

		add(new FeatureItem<>(-2, N::getPOSTag)           , new FeatureItem<>(-1, N::getPOSTag));
		add(new FeatureItem<>(-1, N::getPOSTag)           , new FeatureItem<>( 1, this::getAmbiguityClass));
		add(new FeatureItem<>( 1, this::getAmbiguityClass), new FeatureItem<>( 2, this::getAmbiguityClass));

		// 3-gram features
		add(new FeatureItem<>(-2, N::getPOSTag), new FeatureItem<>(-1, N::getPOSTag)           , new FeatureItem<>(0, this::getAmbiguityClass));
		add(new FeatureItem<>(-2, N::getPOSTag), new FeatureItem<>(-1, N::getPOSTag)           , new FeatureItem<>(1, this::getAmbiguityClass));
		add(new FeatureItem<>(-1, N::getPOSTag), new FeatureItem<>( 0, this::getAmbiguityClass), new FeatureItem<>(1, this::getAmbiguityClass));
		add(new FeatureItem<>(-1, N::getPOSTag), new FeatureItem<>( 1, this::getAmbiguityClass), new FeatureItem<>(2, this::getAmbiguityClass));

		// affix features
		add(new FeatureItem<>(0, n -> getPrefix(n, 2)));
		add(new FeatureItem<>(0, n -> getPrefix(n, 3)));
		add(new FeatureItem<>(0, n -> getSuffix(n, 1)));
		add(new FeatureItem<>(0, n -> getSuffix(n, 2)));
		add(new FeatureItem<>(0, n -> getSuffix(n, 3)));
		add(new FeatureItem<>(0, n -> getSuffix(n, 4)));
		
		// orthographic features
		addSet(new FeatureItem<>(0, this::getOrthographicFeatures));
		
		// boolean features
		addSet(new FeatureItem<>(0, this::getBooleanFeatures));
	}
	
	@Override
	protected N getNode(FeatureItem<N,?> item)
	{
		return state.getNode(item.window);
	}
	
//	========================= FEATURE EXTRACTORS =========================

	protected String getUncapitalizedSimplifiedWordForm(N node)
	{
		return StringUtils.toLowerCase(node.getSimplifiedWordForm());
	}
	
	protected String getWordShape(N node)
	{
		return node.getWordShape(2);
	}
	
	protected String getAmbiguityClass(N node)
	{
		return state.getAmbiguityClass(node);
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
	
	protected String[] getBooleanFeatures(N node)
	{
		String[] values = new String[2];
		int index = 0;
		
		if (state.isFirst(node)) values[index++] = "0";
		if (state.isLast (node)) values[index++] = "1";
		
		return (index == 0) ? null : (index == values.length) ? values : Arrays.copyOf(values, index);
	}
}
