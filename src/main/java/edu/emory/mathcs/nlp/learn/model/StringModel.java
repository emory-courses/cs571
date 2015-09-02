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
package edu.emory.mathcs.nlp.learn.model;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import edu.emory.mathcs.nlp.learn.util.Instance;
import edu.emory.mathcs.nlp.learn.util.Prediction;
import edu.emory.mathcs.nlp.learn.util.StringInstance;
import edu.emory.mathcs.nlp.learn.util.StringPrediction;
import edu.emory.mathcs.nlp.learn.vector.IndexValuePair;
import edu.emory.mathcs.nlp.learn.vector.SparseVector;
import edu.emory.mathcs.nlp.learn.vector.StringItem;
import edu.emory.mathcs.nlp.learn.vector.StringVector;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class StringModel implements Serializable
{
	private static final long serialVersionUID = 6610292514588323072L;
	private Deque<StringInstance> instance_deque;
	private LabelMap              label_map;
	private FeatureMap            feature_map;
	private WeightVector          weight_vector;
	private float                 bias_weight;
	
	public StringModel(WeightVector vector, float biasWeight)
	{
		instance_deque = new ArrayDeque<>();
		label_map      = new LabelMap();
		feature_map    = new FeatureMap();
		weight_vector  = vector;
		bias_weight    = biasWeight;
	}
	
	public void addInstance(StringInstance instance)
	{
		label_map.add(instance.getLabel());
		instance.getVector().forEach(e -> feature_map.add(e.getType(), e.getValue()));
		instance_deque.add(instance);
	}
	
	public List<Instance> vectorize(int labelCutoff, int featureCutoff)
	{
		List<Instance> list = new ArrayList<>();
		StringInstance instance;
		int labelIndex;
		
		// filtering
		label_map  .expand(labelCutoff);
		feature_map.expand(featureCutoff);
		weight_vector.expand(label_map.size(), feature_map.size());
		
		// vectorizing
		while (!instance_deque.isEmpty())
		{
			instance   = instance_deque.poll();
			labelIndex = label_map.indexOf(instance.getLabel());
			
			if (labelIndex >= 0)
				list.add(new Instance(getLabel(labelIndex), toSparseVector(instance.getVector(), bias_weight)));
		}
		
		return list;
	}
	
	private int getLabel(int index)
	{
		return weight_vector.isBinomial() ? index*2 - 1 : index;
	}
	
	public SparseVector toSparseVector(StringVector vector, float biasWeight)
	{
		SparseVector x = new SparseVector();
		int index;
		
		x.add(new IndexValuePair(0, biasWeight));	// bias
		
		for (StringItem e : vector)
		{
			index = feature_map.indexOf(e.getType(), e.getValue());
			if (index > 0) x.add(index, e.getWeight());
		}
		
		x.sort();
		return x;
	}
	
	public WeightVector getWeightVector()
	{
		return weight_vector;
	}
	
	public StringPrediction predictBest(StringVector x)
	{
		Prediction p = weight_vector.predictBest(toSparseVector(x, bias_weight));
		return new StringPrediction(label_map.getLabel(p.getLabel()), p.getScore());
	}
}
