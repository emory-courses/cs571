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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.emory.mathcs.nlp.component.eval.AccuracyEval;
import edu.emory.mathcs.nlp.component.state.L2RState;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.component.util.NLPFlag;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.util.StringInstance;
import edu.emory.mathcs.nlp.learn.vector.StringVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSTagger<N extends POSNode> extends NLPComponent<N>
{
	private static final long serialVersionUID = -7926217238116337203L;
	private AmbiguityClassMap ambiguity_class_map;

	public POSTagger(NLPFlag flag, StringModel model)
	{
		super(flag, model);
	}
	
//	============================== LEXICONS ==============================

	@Override
	protected void readLexicons(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		ambiguity_class_map = (AmbiguityClassMap)in.readObject();
	}

	@Override
	protected void writeLexicons(ObjectOutputStream out) throws IOException
	{
		out.writeObject(ambiguity_class_map);
	}
	
	public AmbiguityClassMap getAmbiguityClassMap()
	{
		return ambiguity_class_map;
	}
	
	public void setAmbiguityClassMap(AmbiguityClassMap map)
	{
		ambiguity_class_map = map;
	}
	
//	============================== PROCESS ==============================
	
	public void process(N[] nodes)
	{
		L2RState<N> state = new POSState<>(nodes);
		if (!isDecode()) state.clearGoldLabels();
		
		while (!state.isTerminate())
		{
			StringVector x = extractFeatures(state);
			String label = getLabel(state, x);
			state.setLabel(label);
			state.next();
			
			if (isTrain()) model.addInstance(new StringInstance(label, x));
		}
		
		if (isEvaluate()) state.evaluateTokens((AccuracyEval)eval);
	}
	
	private String getLabel(L2RState<N> state, StringVector x)
	{
		return isTrain() ? state.getGoldLabel() : model.predictBest(x).getLabel();
	}
	
	protected StringVector extractFeatures(L2RState<N> state)
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
		
		node = state.getNode(0);
		if (node != null) x.add(type++, ambiguity_class_map.get(node));
		
		return x;
	}
}
