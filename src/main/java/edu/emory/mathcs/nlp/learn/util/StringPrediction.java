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
package edu.emory.mathcs.nlp.learn.util;

import java.io.Serializable;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class StringPrediction implements Serializable
{
	private static final long serialVersionUID = 4629812694101207696L;
	private String label;
	private double score;
	
	public StringPrediction(String label, double score)
	{
		set(label, score);
	}
	
	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public double getScore()
	{
		return score;
	}

	public void setScore(double score)
	{
		this.score = score;
	}
	
	public void set(String label, double score)
	{
		setLabel(label);
		setScore(score);
	}
	
	public void copy(StringPrediction p)
	{
		set(p.label, p.score);
	}

	@Override
	public String toString()
	{
		return label+":"+score;
	}
}
