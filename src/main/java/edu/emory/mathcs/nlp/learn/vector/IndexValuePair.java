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
package edu.emory.mathcs.nlp.learn.vector;

import java.io.Serializable;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class IndexValuePair implements Serializable, Comparable<IndexValuePair>
{
	private static final long serialVersionUID = -8933673050278448784L;
	private int   index;
	private float value;
	
	public IndexValuePair(int index)
	{
		set(index, 1f);
	}
	
	public IndexValuePair(int index, float value)
	{
		set(index, value);
	}
	
	public int getIndex()
	{
		return index;
	}

	public float getValue()
	{
		return value;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}

	public void setValue(float value)
	{
		this.value = value;
	}
	
	public void set(int index, float value)
	{
		setIndex(index);
		setValue(value);
	}
	
	@Override
	public int compareTo(IndexValuePair o)
	{
		return index - o.index;
	}
	
	@Override
	public String toString()
	{
		return index+":"+value;
	}
}
