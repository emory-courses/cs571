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
package edu.emory.mathcs.nlp.util;

import edu.emory.mathcs.nlp.util.MathUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Sigmoid
{
	private final int    TABLE_SIZE = 1000;
	private final int    MAX_EXP    = 6;
	private final double NORM       = TABLE_SIZE / MAX_EXP / 2; 

	private float[] table;
	
	public Sigmoid()
	{
		table = new float[TABLE_SIZE];
		
		for (int i=0; i<TABLE_SIZE; i++)
		{
			table[i]  = (float)Math.exp((MathUtils.divide(i, TABLE_SIZE) * 2 - 1) * MAX_EXP);
			table[i] /= (table[i] + 1);
		}
	}
	
	public double get(double d) 
	{
		return (d > MAX_EXP) ? 1 : (d < -MAX_EXP) ? 0 : table[(int)((d + MAX_EXP) * NORM)];
	}
}
