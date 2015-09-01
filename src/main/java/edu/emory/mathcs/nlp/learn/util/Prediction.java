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

import edu.emory.mathcs.nlp.common.MathUtils;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Prediction implements Serializable, Comparable<Prediction>
{
	private static final long serialVersionUID = -2873195048974695284L;
	private int    label;
	private double score;
	
	public Prediction(int label, double score)
	{
		set(label, score);
	}
	
	public int getLabel()
	{
		return label;
	}

	public void setLabel(int label)
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
	
	public void set(int label, double score)
	{
		setLabel(label);
		setScore(score);
	}
	
	public void copy(Prediction p)
	{
		set(p.label, p.score);
	}

	@Override
	public int compareTo(Prediction o)
	{
		double diff = score - o.score;
		return (diff == 0) ? label - o.label : MathUtils.signum(diff);
	}
	
	@Override
	public String toString()
	{
		return label+":"+score;
	}
}
