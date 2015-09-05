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
package edu.emory.mathcs.nlp.component.state;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import edu.emory.mathcs.nlp.component.eval.AccuracyEval;

/**
 * Left-to-right tagging state.
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class L2RState<N> extends NLPState<N>
{
	protected BiFunction<N,String,String> setter;
	protected Function<N,String> getter;
	protected String[] gold;
	protected int index;
	
	public L2RState(N[] nodes, Function<N,String> getter, BiFunction<N,String,String> setter)
	{
		super(nodes);
		this.getter = getter;
		this.setter = setter;
		index = 0;
	}
	
	@Override
	public void clearGold()
	{
		gold = Arrays.stream(nodes).map(n -> setter.apply(n, null)).toArray(String[]::new);
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
	
	public N getNode(int window)
	{
		return getNode(index, window);
	}
	
	public String getGoldLabel()
	{
		return gold[index];
	}
	
	public void setLabel(String label)
	{
		setter.apply(nodes[index], label);
	}
	
	public void evaluateTokens(AccuracyEval eval)
	{
		int correct = 0;
		
		for (int i=0; i<nodes.length; i++)
			if (gold[i].equals(getter.apply(nodes[i])))
				correct++;
		
		eval.add(correct, nodes.length);
	}
}
