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
package edu.emory.mathcs.nlp.component.pos;

import java.util.List;
import java.util.stream.Collectors;

import edu.emory.mathcs.nlp.component.util.AccuracyEval;
import edu.emory.mathcs.nlp.util.DSUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSState<N extends IPOSNode>
{
	private List<String> gold;
	private List<N>      nodes;
	private int          index;
	
	public POSState(List<N> nodes)
	{
		this.nodes = nodes;
		index = 0;
	}
	
	public void next()
	{
		index++;
	}
	
	public boolean isTerminate()
	{
		return index >= nodes.size();
	}
	
	public N getNode(int window)
	{
		int i = index + window;
		return DSUtils.isRange(nodes, i) ? nodes.get(i) : null;
	}
	
	public void clearGold()
	{
		gold = nodes.stream().map(n -> n.setPOSTag(null)).collect(Collectors.toList());
	}
	
	public String getGoldLabel()
	{
		return gold.get(index);
	}
	
	public void setLabel(String label)
	{
		nodes.get(index).setPOSTag(label);
	}
	
	public void evaluate(AccuracyEval eval)
	{
		int correct = 0;
		
		for (int i=0; i<nodes.size(); i++)
		{
			if (gold.get(i).equals(nodes.get(i).getPOSTag()))
				correct++;
		}
		
		eval.add(correct, nodes.size());
	}
}
