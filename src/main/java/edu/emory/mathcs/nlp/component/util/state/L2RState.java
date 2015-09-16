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
package edu.emory.mathcs.nlp.component.util.state;

import java.util.Arrays;

import edu.emory.mathcs.nlp.component.util.eval.AccuracyEval;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class L2RState<N> extends NLPState<N,String>
{
	protected String[] gold;
	protected int index = 0;
	
	public L2RState(N[] nodes)
	{
		super(nodes);
	}
	
	@Override
	public void clearGoldLabels()
	{
		gold = Arrays.stream(nodes).map(n -> setLabel(n, null)).toArray(String[]::new);
	}
	
	@Override
	public void next()
	{
		index++;
	}
	
	@Override
	public boolean isTerminate()
	{
		return index >= nodes.length;
	}
	
	@Override
	public String getGoldLabel()
	{
		return gold[index];
	}
	
	@Override
	public String setLabel(String label)
	{
		return setLabel(nodes[index], label);
	}

	protected abstract String setLabel(N node, String label);
	protected abstract String getLabel(N node);
	
	public N getNode(int window)
	{
		return getNode(index, window);
	}
	
	public boolean isFirst(N node)
	{
		return nodes[0] == node;
	}
	
	public boolean isLast(N node)
	{
		return nodes[nodes.length-1] == node;
	}
	
	public void evaluateTokens(AccuracyEval eval)
	{
		int correct = 0;
		
		for (int i=0; i<nodes.length; i++)
			if (gold[i].equals(getLabel(nodes[i])))
				correct++;
		
		eval.add(correct, nodes.length);
	}
}
