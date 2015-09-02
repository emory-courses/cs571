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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.nlp.learn.vector.SparseVector;
import edu.emory.mathcs.nlp.util.Splitter;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class LibSVMReader
{
	private List<Instance> instance_list;
	private int max_feature_index;
	private int max_label_index;
	private int min_label_index;
	
	public LibSVMReader()
	{
		init();
	}
	
	public LibSVMReader(InputStream in)
	{
		init();
		try {read(in);}
		catch (Exception e) {e.printStackTrace();}
	}
	
	private void init()
	{
		instance_list = new ArrayList<>();
		max_feature_index = -1;
		max_label_index   = -1;
		min_label_index   = Integer.MAX_VALUE;
	}
	
	public void read(InputStream in) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		int i, label, index;
		SparseVector x;
		String[] t, f;
		float value;
		String line;
		
		while ((line = reader.readLine()) != null)
		{
			t = Splitter.splitSpace(line.trim());
			x = new SparseVector();
			label = Integer.parseInt(t[0]);
			instance_list.add(new Instance(label, x));
			min_label_index = Math.min(min_label_index, label);
			max_label_index = Math.max(max_label_index, label);
			
			for (i=1; i<t.length; i++)
			{
				f = Splitter.splitColons(t[i]);
				index = Integer.parseInt(f[0]);
				value = Float.parseFloat(f[1]);
				x.add(index, value);
				max_feature_index = Math.max(max_feature_index, index);
			}
		}
	}
	
	public List<Instance> getInstanceList()
	{
		return instance_list;
	}
	
	public int labelSize()
	{
		return max_label_index + 1;
	}
	
	public int featureSize()
	{
		return max_feature_index + 1;
	}
}
