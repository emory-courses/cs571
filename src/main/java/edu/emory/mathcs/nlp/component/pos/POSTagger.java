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

import edu.emory.mathcs.nlp.component.NLPComponent;
import edu.emory.mathcs.nlp.component.NLPFlag;
import edu.emory.mathcs.nlp.component.util.AccuracyEval;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.util.StringInstance;
import edu.emory.mathcs.nlp.learn.vector.StringVector;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSTagger<N extends IPOSNode> extends NLPComponent<N>
{
	protected NLPFlag      flag;
	protected StringModel  model; 
	protected AccuracyEval eval;
	
	/** For training. */
	public POSTagger(float biasWeight)
	{
		flag  = NLPFlag.TRAIN;
		model = new StringModel(new MultinomialWeightVector(), biasWeight);
	}
	
	public POSTagger(StringModel model, AccuracyEval eval)
	{
		flag = NLPFlag.EVALUATE;
		this.model = model;
		this.eval  = eval;
	}
	
	public void setEval(AccuracyEval eval)
	{
		flag = NLPFlag.EVALUATE;
		this.eval = eval;
	}
	
	public void process(List<N> nodes)
	{
		POSState<N> state = new POSState<>(nodes);
		if (!isDecode()) state.clearGold();
		
		while (!state.isTerminate())
		{
			StringVector x = extractFeatures(state);
			String label = getLabel(state, x);
			state.setLabel(label);
			state.next();
			
			if (isTrain())
				model.addInstance(new StringInstance(label, x));
		}
		
		if (isEvaluate()) state.evaluate(eval);
	}
	
	private String getLabel(POSState<N> state, StringVector x)
	{
		return isTrain() ? state.getGoldLabel() : model.predictBest(x).getLabel();
	}
	
	public boolean isTrain()
	{
		return flag == NLPFlag.TRAIN;
	}
	
	public boolean isDecode()
	{
		return flag == NLPFlag.DECODE;
	}
	
	public boolean isEvaluate()
	{
		return flag == NLPFlag.EVALUATE;
	}
	
	public StringModel getModel()
	{
		return model;
	}
	
	protected StringVector extractFeatures(POSState<N> state)
	{
		StringVector x = new StringVector();
		N node; int type = 0;
		
		node = state.getNode(0);
		if (node != null) x.add(type++, node.getWordForm());
		
		node = state.getNode(-1);
		if (node != null) x.add(type++, node.getWordForm());
		
		node = state.getNode(1);
		if (node != null) x.add(type++, node.getWordForm());
		
		node = state.getNode(-1);
		if (node != null) x.add(type++, node.getPOSTag());
		
		return x;
	}
}
