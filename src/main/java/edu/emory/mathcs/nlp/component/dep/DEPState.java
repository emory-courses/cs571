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
package edu.emory.mathcs.nlp.component.dep;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.Arrays;

import edu.emory.mathcs.nlp.component.util.eval.Eval;
import edu.emory.mathcs.nlp.component.util.state.NLPState;
import edu.emory.mathcs.nlp.learn.util.StringPrediction;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DEPState<N extends DEPNode> extends NLPState<N>
{
	static public final String LEFT_ARC  = "LA-";
	static public final String RIGHT_ARC = "RA-";
	static public final String SHIFT     = "S";
	static public final String REDUCE    = "R";
	
	private DEPArc[]     oracle;
	private IntArrayList stack;
	private int          input;
	
	public DEPState(N[] nodes)
	{
		super(nodes);
		stack = new IntArrayList();
		input = 0;
		stack.push(input++);
	}
	
//	====================================== ORACLE ======================================

	@Override
	public void saveOracle()
	{
		oracle = Arrays.stream(nodes).map(n -> n.clearDependencies()).toArray(DEPArc[]::new);
	}
	
	@Override
	public String getOraclePrediction()
	{
		// left-arc: input is the head of stack
		DEPArc o = oracle[stack.topInt()];
		
		if (o.isNode(getInput(0)))
			return LEFT_ARC + o.getLabel();
		
		// right-arc: stack is the head of input
		o = oracle[input];
		
		if (o.isNode(getStack(0)))
			return RIGHT_ARC + o.getLabel();
		
		// reduce: stack has the head
		if (getStack(0).hasHead())
			return REDUCE;
		
		return SHIFT;
	}
	
//	====================================== TRANSITION ======================================
	
	@Override
	public void next(StringPrediction prediction)
	{
		String label = prediction.getLabel();
		String transition = label.length() < 2 ? label : label.substring(0,2);
		
		if (label.equals(LEFT_ARC))
		{
			DEPNode s = getStack(0);
			DEPNode i = getInput(0);
			
			if (!i.isDescendantOf(s))
			{
				s.setHead(i, label.substring(3));
				transition = REDUCE;
			}
			else
				transition = SHIFT;
		}
		else if (label.equals(RIGHT_ARC))
		{
			DEPNode s = getStack(0);
			DEPNode i = getInput(0);
			
			if (!s.isDescendantOf(i))
				i.setHead(s, label.substring(3));

			transition = SHIFT;
		}
		
		switch (transition)
		{
		case SHIFT : stack.push(input++); break;
		case REDUCE: stack.pop(); break;
		}
	}
	
	@Override
	public boolean isTerminate()
	{
		return input >= nodes.length;
	}
	
	/**
	 * @return the window'th top of the stack if exists; otherwise, -1.
	 * @param window 0: top, 1: 2nd-top, so one.
	 */
	public N peekStack(int window)
	{
		return window < stack.size() ? nodes[stack.peekInt(window)] : null;
	}
	
	public N getStack(int window)
	{
		return getNode(stack.topInt(), window);
	}
	
	public N getInput(int window)
	{
		return getNode(input, window);
	}
	
//	====================================== EVALUATE ======================================

	@Override
	public void evaluate(Eval eval)
	{
		int las = 0, uas = 0;
		DEPNode node;
		DEPArc  gold;
		
		for (int i=1; i<nodes.length; i++)
		{
			node = nodes [i];
			gold = oracle[i];
			
			if (gold.isNode(node.getHead()))
			{
				uas++;
				if (gold.isLabel(node.getLabel())) las++;
			}
		}

		((DEPEval)eval).add(las, uas, nodes.length-1);
	}
	
//	============================== UTILITIES ==============================
	
	public boolean isFirst(N node)
	{
		return nodes[1] == node;
	}
	
	public boolean isLast(N node)
	{
		return nodes[nodes.length-1] == node;
	}
}
