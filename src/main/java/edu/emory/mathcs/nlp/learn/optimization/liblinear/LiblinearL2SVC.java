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
package edu.emory.mathcs.nlp.learn.optimization.liblinear;

import java.util.List;
import java.util.Random;

import edu.emory.mathcs.nlp.common.util.BinUtils;
import edu.emory.mathcs.nlp.common.util.DSUtils;
import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.optimization.OneVsAllOptimizer;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class LiblinearL2SVC extends OneVsAllOptimizer
{
	private final int MAX_EPOCHS = 1000;
	private double cost;
	private double tolerance;
	private int    loss_type;
	
	public LiblinearL2SVC(WeightVector weightVector, double cost, double tolerance, int lossType)
	{
		super(weightVector);
	}
	
	@Override
	public void update(List<Instance> instances, int label)
	{
		final float diagonal = (loss_type == 1) ? 0f   : (float)(0.5/cost);
		final double upper   = (loss_type == 1) ? cost : Double.POSITIVE_INFINITY;
		final byte[] y = getBinaryLabels(instances, label);
		final Random rand = new Random(5);
		final int N = instances.size();
		
		float[] weight = weight_vector.getWeights(label);
		float[] alpha  = new float[N];
		float[] QD     = new float[N];
		int  [] index  = DSUtils.range(N);
		
		int active_size = N, epochs = 0, i, s;
		double G, d, alpha_old;
		Instance instance;
		
		// PG: projected gradient, for shrinking and stopping
		double PGmax_old = Double.POSITIVE_INFINITY;
		double PGmin_old = Double.NEGATIVE_INFINITY;
		double PGmax_new, PGmin_new;
		double PG;
		
		for (i=0; i<N; i++)
		{
			QD[i] = diagonal;
			
			for (IndexValuePair p : instances.get(i).getVector())
				QD[i] += MathUtils.sq(p.getValue());
		}
		
		for (epochs=0; epochs<MAX_EPOCHS; epochs++)
		{
			PGmax_new = Double.NEGATIVE_INFINITY;
			PGmin_new = Double.POSITIVE_INFINITY;
			DSUtils.shuffle(index, rand, active_size);
			
			for (s=0; s<active_size; s++)
			{
				i = index[s];
				instance = instances.get(i);
				
				// hinge loss				
				G = getScore(instance.getVector(), weight) * y[i] - 1;
				G += alpha[i] * diagonal;
				PG = 0;
				
				if (alpha[i] == 0)
				{
					if (G > PGmax_old)
					{
						active_size--;
						DSUtils.swap(index, s, active_size);
						s--;
						continue;
					}
					else if (G < 0)
					{
						PG = G;
					}
				}
				else if (alpha[i] == upper)
				{
					if (G < PGmin_old)
					{
						active_size--;
						DSUtils.swap(index, s, active_size);
						s--;
						continue;
					}
					else if (G > 0)
					{
						PG = G;
					}
				}
				else
				{
					PG = G;
				}
				
				PGmax_new = Math.max(PGmax_new, PG);
				PGmin_new = Math.min(PGmin_new, PG);
				
				if (Math.abs(PG) > 1.0e-12)
				{
					alpha_old = alpha[i];
					alpha[i] = (float)Math.min(Math.max(alpha[i] - G / QD[i], 0.0), upper);
					d = (alpha[i] - alpha_old) * y[i];
					
					if (d != 0)
					{
						for (IndexValuePair xi : instance.getVector())
							weight[xi.getIndex()] += d * xi.getValue();
					}
				}
			}
			
			if (PGmax_new - PGmin_new <= tolerance)
			{
				if (active_size == N)
					break;
				else
				{
					active_size = N;
					PGmax_old = Double.POSITIVE_INFINITY;
					PGmin_old = Double.NEGATIVE_INFINITY;
					continue;
				}
			}
			
			PGmax_old = PGmax_new;
			PGmin_old = PGmin_new;
			if (PGmax_old <= 0) PGmax_old = Double.POSITIVE_INFINITY;
			if (PGmin_old >= 0) PGmin_old = Double.NEGATIVE_INFINITY;
		}
		
		weight_vector.setWeights(label, weight);
		BinUtils.LOG.info(String.format("- label = %d, epochs = %d\n", label, epochs));
    }
}
