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


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSNode implements IPOSNode
{
	private static final long serialVersionUID = -8563108117037742010L;
	protected String word_form;
	protected String pos_tag;
	
	public POSNode(String form)
	{
		setWordForm(form);
	}
	
	public POSNode(String form, String tag)
	{
		setWordForm(form);
		setPOSTag(tag);
	}
	
	@Override
	public String getWordForm()
	{
		return word_form;
	}

	@Override
	public String getPOSTag()
	{
		return pos_tag;
	}

	@Override
	public String setWordForm(String form)
	{
		String t  = word_form;
		word_form = form;
		return t;
	}
	
	@Override
	public String setPOSTag(String tag)
	{
		String t = pos_tag;
		pos_tag  = tag;
		return t;
	}
}
