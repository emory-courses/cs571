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
package edu.emory.mathcs.nlp.deeplearning.activation;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SigmoidFunction implements ActivationFunction
{
	private final float[] table;
	private final float   floor, ceiling;
	private final float   table_multiply;
	private final int      table_adjust;
	
	/** Calls {@link #SigmoidFunction(int, float, float)}, where size = 3500, floor = -6, ceiling = 6. */
	public SigmoidFunction()
	{
		this(3500, -6, 6);
	}
	
	/**
	 * @param size size of the sigmoid table (10,000 being the highest recommendation).
	 * @param floor lower convergence.
	 * @param ceiling upper convergence.
	 */
	public SigmoidFunction(int size, float floor, float ceiling)
	{
		this.floor   = floor;
		this.ceiling = ceiling;
		this.table   = new float[size];
		
		float range = ceiling - floor;
		
		table_adjust   = (int)(0.5 - floor * (size - 1) / range);
		table_multiply = (float)(size - 1) / range;
		
        for (int i=0; i<size; ++i)
            table[i] = (float)(1d / (1d + Math.exp(6d * (floor + ceiling - 2d * (floor + range * i / (size - 1))) / range)));
	}
	
	public final double get(double d)
	{
		return (d <= floor) ? 0 : (d >= ceiling) ? 1 : table[(int)(d*table_multiply) + table_adjust];
	}
	
	@Override
	public void transform(double[] scores)
	{
		for (int i=0; i<scores.length; i++)
			scores[i] = get(scores[i]);
	}
}
