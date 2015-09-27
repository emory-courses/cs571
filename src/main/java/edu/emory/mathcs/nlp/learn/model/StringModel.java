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
import java.util.Collection;
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
	private List<Instance>        instance_list;
	private LabelMap              label_map;
	private FeatureMap            feature_map;
	private WeightVector          weight_vector;
	private float                 bias;
	
	public StringModel(WeightVector vector)
	{
		instance_deque = new ArrayDeque<>();
		label_map      = new LabelMap();
		feature_map    = new FeatureMap();
		weight_vector  = vector;
	}
	
	public float getBias()
	{
		return bias;
	}
	
	public void setBias(float bias)
	{
		this.bias = bias;
	}
	
	public void addInstance(StringInstance instance)
	{
		label_map.add(instance.getLabel());
		instance.getVector().forEach(e -> feature_map.add(e.getType(), e.getValue()));
		instance_deque.add(instance);
	}
	
	public void addInstances(Collection<StringInstance> instances)
	{
		for (StringInstance instance : instances)
			addInstance(instance);
	}
	
	public Deque<StringInstance> getStringInstanceDeque()
	{
		return instance_deque;
	}
	
	public List<Instance> getInstanceList()
	{
		return instance_list;
	}
	
	public void vectorize(int labelCutoff, int featureCutoff, boolean reset)
	{
		instance_list = new ArrayList<>();
		StringInstance instance;
		int labelIndex;
		
		// filtering
		if (reset)
		{
			label_map  .initIndices();
			feature_map.initIndices();
		}
		
		label_map  .expand(labelCutoff);
		feature_map.expand(featureCutoff);
		
		if (reset)	weight_vector.init  (label_map.size(), feature_map.size());
		else		weight_vector.expand(label_map.size(), feature_map.size());
		
		// vectorizing
		while (!instance_deque.isEmpty())
		{
			instance   = instance_deque.poll();
			labelIndex = label_map.indexOf(instance.getLabel());
			
			if (labelIndex >= 0)
				instance_list.add(new Instance(labelIndex, toSparseVector(instance.getVector())));
		}
		
		instance_deque = new ArrayDeque<>();
	}
	
	public SparseVector toSparseVector(StringVector vector)
	{
		SparseVector x = new SparseVector();
		int index;
		
		if (bias > 0)	// bias
			x.add(new IndexValuePair(0, bias));
		
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
		Prediction p = weight_vector.predictBest(toSparseVector(x));
		return new StringPrediction(label_map.getLabel(p.getLabel()), p.getScore());
	}
	
	public String trainInfo()
	{
		StringBuilder build = new StringBuilder();
		
		build.append("- # of instances: "+instance_list.size()+"\n");
		build.append("- # of labels   : "+label_map.size()+"\n");
		build.append("- # of features : "+feature_map.size());
		
		return build.toString();
	}
}
