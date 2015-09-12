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
package edu.emory.mathcs.nlp.learn.sgd.adadelta;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class MultinomialAdaDelta extends AdaDelta
{
    protected int batchSize;
    protected WeightVector gradient;
    protected List<Instance> instances;
    public MultinomialAdaDelta(WeightVector weightVector, boolean average, double learningRate, double decayingRate, int batchSize)
    {
        super(weightVector, average, learningRate, decayingRate);
        this.batchSize = batchSize;
        gradient = weightVector.createEmptyVector();
        instances = new ArrayList<>();
    }

    @Override
    protected void updateWeightVector(Instance instance, int steps)
    {
        int yp = instance.getLabel(), yn = bestMultinomialLabelHinge(instance);

        if (yp != yn)
        {
            instances.add(instance);
        }

        if (steps%batchSize==0)
        {
            updateGradient();
            System.out.println(steps);
            updateDiagonals(gradient);

            for (int i=0;i<diagonals.toArray().length;i++)
                weight_vector.toArray()[i] += learning_rate/Math.sqrt(epsilon + diagonals.toArray()[i])*gradient.toArray()[i];
            gradient.fromArray(new float[gradient.toArray().length]);
        }
    }
    private void updateGradient() {
        for (Instance i : instances) {
            int yp = i.getLabel(), yn = bestMultinomialLabelHinge(i);
            if (yp != yn)
            {
                gradient.update(i.getVector(), yp, 1);
                gradient.update(i.getVector(), yn, -1);
            }
        }
    }
}