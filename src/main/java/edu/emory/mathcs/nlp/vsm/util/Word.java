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

import java.io.Serializable;

import edu.emory.mathcs.nlp.common.util.MathUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Word implements Serializable, Comparable<Word>
{
	private static final long serialVersionUID = -8359884564426852692L;
	public String form;
	public long   count;
	public byte[] code;		// binary codes from Huffman tree
	public int[]  point;	// pointers to the ancestors
	
	public Word(String form)
	{
		set(form, 0);
	}
	
	public Word(String form, int count)
	{
		set(form, count);
	}
	
	public void set(String form, int count)
	{
		this.form  = form;
		this.count = count;
	}
	
	public void increment(int count)
	{
		this.count += count;
	}
	
	public int codeLength()
	{
		return code.length;
	}
	
	@Override
	public int compareTo(Word o)
	{
		return MathUtils.signum(count - o.count);
	}
	
	@Override
	public String toString()
	{
		return form+":"+count;
	}
}