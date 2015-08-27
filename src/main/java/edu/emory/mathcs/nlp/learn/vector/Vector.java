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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.emory.mathcs.nlp.common.Joiner;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class Vector<T extends Comparable<T>> implements Serializable, Iterable<T>
{
	private static final long serialVersionUID = -4918193483553121004L;
	protected List<T> vector;
	
	public Vector()
	{
		vector = new ArrayList<>();
	}
	
	public void add(T item)
	{
		vector.add(item);
	}
	
	public T get(int index)
	{
		return vector.get(index);
	}
	
	public int size()
	{
		return vector.size();
	}
	
	public void sort()
	{
		Collections.sort(vector);
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return vector.iterator();
	}

	@Override
	public String toString()
	{
		return Joiner.join(vector, " ");
	}
}
