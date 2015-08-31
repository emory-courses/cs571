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

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DenseVector implements Vector
{
	private static final long serialVersionUID = 4610874141457525275L;
	private float[] vector;
	
	public DenseVector(int size)
	{
		vector = new float[size];
	}
	
	public float get(int index)
	{
		return vector[index];
	}
	
	public void set(int index, float value)
	{
		vector[index] = value;
	}
	
	public int size()
	{
		return vector.length;
	}
	
	@Override
	public Iterator<IndexValuePair> iterator()
	{
		Iterator<IndexValuePair> it = new Iterator<IndexValuePair>()
		{
			private int index = 0;
			
			@Override
			public boolean hasNext()
			{
				return index < vector.length;
			}
			
			@Override
			public IndexValuePair next()
			{
				return new IndexValuePair(index, vector[index++]);
			}
			
			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
		
		return it;
	}

	@Override
	public String toString()
	{
		return Arrays.toString(vector);
	}
}
