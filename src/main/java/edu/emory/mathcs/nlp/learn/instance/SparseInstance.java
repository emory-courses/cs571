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
package edu.emory.mathcs.nlp.learn.instance;

import java.io.Serializable;

import edu.emory.mathcs.nlp.learn.vector.SparseVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SparseInstance implements Serializable
{
	private static final long serialVersionUID = 8175869181443119424L;
	private SparseVector vector;
	private int label;
	
	public SparseInstance(int label, SparseVector vector)
	{
		set(label, vector);
	}
	
	public int getLabel()
	{
		return label;
	}
	
	public SparseVector getVector()
	{
		return vector;
	}

	public void setLabel(int label)
	{
		this.label = label;
	}

	public void setVector(SparseVector vector)
	{
		this.vector = vector;
	}

	public void set(int label, SparseVector vector)
	{
		setLabel(label);
		setVector(vector);
	}
	
	@Override
	public String toString()
	{
		return label+" "+vector.toString();
	}
}
