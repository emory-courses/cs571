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
package edu.emory.mathcs.nlp.learn.optimization.sgd;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

import java.util.StringJoiner;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AdaGradTrunc extends SGDClassification
{
	protected final double epsilon = 0.00001;
	protected WeightVector diagonals;
	protected WeightVector possiblePenalty;
	protected WeightVector penaltySum;
	protected double l1 = 0.00001;


	public AdaGradTrunc(WeightVector weightVector, boolean average, double learningRate, double l1)
	{
		super(weightVector, average, learningRate);
		diagonals = weightVector.createEmptyVector();
		possiblePenalty = weightVector.createEmptyVector();
		penaltySum = weightVector.createEmptyVector();
		this.l1 = l1;
	}

	@Override
	protected void updateBinomial(Instance instance)
	{
		Vector x = instance.getVector();
		int   yp = instance.getLabel();
		int   yn = binomialBestHingeLoss(instance);

		if (yp != yn)
		{
			yp *= 2 - 1; // yp = {0, 1} -> {-1, 1}
			updateDiagonals(yp, x);
			update(yp, x);
		}
	}

	@Override
	protected void updateMultinomial(Instance instance)
	{
		Vector x = instance.getVector();
		int   yp = instance.getLabel();
		int   yn = multinomialBestHingeLoss(instance);

		if (yp != yn)
		{
			updateDiagonals(yp, x);
			updateDiagonals(yn, x);

			for (IndexValuePair p : x) {
				possiblePenalty.toArray()[weight_vector.labelSize()*p.getIndex() + yn] += l1 *getGradient(yp, p.getIndex());
				possiblePenalty.toArray()[weight_vector.labelSize()*p.getIndex() + yp] += l1 *getGradient(yp, p.getIndex());
			}
			update(yp, yn, x);
		}


		if (Math.random() < 0.3)
			updateWeight(instance);

	}

	private void updateDiagonals(int y, Vector x)
	{
		for (IndexValuePair p : x)
			diagonals.add(y, p.getIndex(), MathUtils.sq(p.getValue()));
	}

	@Override
	protected double getGradient(int y, int xi)
	{
		return learning_rate / (epsilon + Math.sqrt(diagonals.get(y, xi)));
	}

	@Override
	public String toString()
	{
		StringJoiner join = new StringJoiner(", ");

		join.add("average = "+isAveraged());
		join.add("learning rate = "+learning_rate);

		return "AdaGradTrunc: "+join.toString();
	}

	public void updateWeight(Instance instance) {

		Vector x = instance.getVector();
		int   yp = instance.getLabel();
		int   yn = multinomialBestHingeLoss(instance);

		if (yp != yn)
		{

			updateDiagonals(yp, x);
			updateDiagonals(yn, x);
			update(yp, yn, x);

			for (IndexValuePair xi : x)
			{
				double gp = getGradient(yp, xi.getIndex()) * xi.getValue();
				double gn = -getGradient(yn, xi.getIndex()) * xi.getValue();
				weight_vector.add(yp, xi.getIndex(), gp);
				weight_vector.add(yn, xi.getIndex(), gn);
				applyPenalty(weight_vector.labelSize()*xi.getIndex()+yp);
				applyPenalty(weight_vector.labelSize()*xi.getIndex()+yn);
			}

		}

	}

	private void applyPenalty(int index) {
		float z = weight_vector.toArray()[index];
		float qi = penaltySum.toArray()[index];
		float u = possiblePenalty.toArray()[index];
		if (z > 0)
			weight_vector.toArray()[index] = Math.max(0, z-(u+qi));
		else if (z < 0)
			weight_vector.toArray()[index] = Math.min(0, z+(u-qi));
		penaltySum.toArray()[index] += weight_vector.toArray()[index]-z;

	}
}
