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
public class StringFeatureItem implements Serializable, Comparable<StringFeatureItem>
{
	private static final long serialVersionUID = 8474553410837185491L;
	private short  type;
	private String value;
	private double weight;
	
	public StringFeatureItem(short type, String value)
	{
		set(type, value, 1d);
	}
	
	public StringFeatureItem(short type, String value, double weight)
	{
		set(type, value, weight);
	}
	
	public short getType()
	{
		return type;
	}

	public String getValue()
	{
		return value;
	}
	
	public double getWeight()
	{
		return weight;
	}
	
	public void setType(short type)
	{
		this.type = type;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public void setWeight(double weight)
	{
		this.weight = weight;
	}
	
	public void set(short type, String value, double weight)
	{
		setType(type);
		setValue(value);
		setWeight(weight);
	}
	
	@Override
	public int compareTo(StringFeatureItem o)
	{
		return value.compareTo(o.value);
	}

	@Override
	public String toString()
	{
		return type+":"+value+":"+weight;
	}
}
