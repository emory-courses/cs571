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

import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.vector.Vector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class MultinomialAdaDelta extends AdaDelta
{
    public MultinomialAdaDelta(WeightVector weightVector, boolean average, double learningRate, double decayingRate, int batchSize)
    {
        super(weightVector, average, learningRate, decayingRate, batchSize);
    }

    @Override
    protected void updateGradients(Instance instance)
    {
    	Vector x = instance.getVector();
		int yp = instance.getLabel();
		int yn = bestMultinomialLabelHinge(instance);
		
		if (yp != yn)
		{
			gradients.update(x, yp,  1);
			gradients.update(x, yn, -1);
		}
    }
}