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
package edu.emory.mathcs.nlp.learn.optimization.minibatch;

import java.util.List;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.optimization.OnlineOptimizer;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AdaptiveMiniBatch extends OnlineOptimizer
{
	protected final double epsilon = 0.00001;
	protected final double batch_ratio;
	protected WeightVector diagonals;
	protected WeightVector gradients;
	protected int batch_size;
	
	public AdaptiveMiniBatch(WeightVector weightVector, double batchRatio, boolean average, double learningRate)
	{
		super(weightVector, average, learningRate);
		diagonals   = weightVector.createEmptyVector();
		gradients   = weightVector.createEmptyVector();
		batch_ratio = batchRatio;
		batch_size  = 0;
	}
	
	@Override
	public void train(List<Instance> instances, int epochs)
	{
		int max = (int)Math.round(instances.size() * batch_ratio);
		steps = 0;

		for (; epochs>0; epochs--)
		{
			shuffle(instances);
			
			for (Instance instance : instances)
			{
				update(instance);
				
				if (++batch_size >= max)
				{
					updateMiniBatch();
					steps++;
				}
			}
		}
		
		if (batch_size > 0)
		{
			updateMiniBatch();
			steps++;
		}
		
		if (isAveraged() && steps > 0) average();
	}
	
	private void average()
	{
		float[] w = weight_vector .toArray();
		float[] a = average_vector.toArray();
		
		for (int i=0; i<w.length; i++)
			w[i] = a[i] / steps;
		
		average_vector.fill(0);
	}
	
//	============================== UPDATE ==============================
	
	@Override
	protected void updateBinomial(Instance instance)
	{
		updateGradientsHingeBinomial(instance);
	}

	@Override
	protected void updateMultinomial(Instance instance)
	{
		updateGradientsHingeMultinomial(instance);
	}
	
	protected void updateMiniBatch()
	{
		normalizeGraidents();
		updateDiagonals();
		updateWeightVector();
		if (isAveraged()) updateAverageVector();

		batch_size = 0;
		gradients.fill(0);
	}
	
	private void normalizeGraidents()
	{
		gradients.multiply(MathUtils.reciprocal(batch_size));
	}
	
	private void updateDiagonals()
	{
		float[] d = diagonals.toArray();
		float[] g = gradients.toArray();
		
		for (int i=0; i<d.length; i++)
			d[i] = getDiagonal(d[i], g[i]);
	}
	
	private void updateWeightVector()
	{
		float[] w = weight_vector.toArray();
		float[] d = diagonals.toArray();
		float[] g = gradients.toArray();
 		
		for (int i=0; i<w.length; i++)
			w[i] += learning_rate / (epsilon + Math.sqrt(d[i])) * g[i];
	}
	
	private void updateAverageVector()
	{
		average_vector.add(weight_vector);
	}
	
	protected abstract float getDiagonal(float previousDiagonal, float gradient);
	
//	============================== GRADIENTS ==============================
	
	 protected void updateGradientsHingeBinomial(Instance instance)
	 {
		 Vector x = instance.getVector();
		 int yp = instance.getLabel();
		 int yn = bestHingeBinomial(x);

		 if (yp != yn)
			 gradients.update(x, yp, yp);
	 }
	
	 protected void updateGradientsHingeMultinomial(Instance instance)
	 {
		 Vector x = instance.getVector();
		 int yp = instance.getLabel();
		 int yn = bestHingeMultinomial(instance);

		 if (yp != yn)
		 {
			 gradients.update(x, yp,  1);
			 gradients.update(x, yn, -1);
		 }
	 }
}
