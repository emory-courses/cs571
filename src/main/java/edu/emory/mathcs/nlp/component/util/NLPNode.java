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
package edu.emory.mathcs.nlp.component.util;

import java.io.Serializable;

import edu.emory.mathcs.nlp.common.util.StringUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NLPNode implements Serializable
{
	private static final long serialVersionUID = 5522467283393796925L;
	protected String simplified_word_form;
	protected String word_form;
	
	public NLPNode(String form)
	{
		setWordForm(form);
	}
	
	public String getWordForm()
	{
		return word_form;
	}
	
	public String getSimplifiedWordForm()
	{
		return simplified_word_form;
	}
	
	public String getLowerSimplifiedWordForm()
	{
		return StringUtils.toLowerCase(simplified_word_form);
	}
	
	/**
	 * Get the word shape of the simplified word-form of the node.
	 * @param maxRepetitions the max count of repetition of a word shape in sequence.
	 * @return the word shape of a node's simplified word-form.
	 */
	public String getWordShape(int maxRepetitions)
	{
		return StringUtils.getShape(simplified_word_form, maxRepetitions);
	}

	public String setWordForm(String form)
	{
		simplified_word_form = StringUtils.toSimplifiedForm(form);
		String t = word_form;
		word_form = form;
		return t;
	}
}
