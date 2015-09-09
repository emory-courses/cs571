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
import java.util.Set;

import edu.emory.mathcs.nlp.common.util.StringUtils;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.util.StringInstance;
import edu.emory.mathcs.nlp.learn.vector.StringVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSTagger<N extends POSNode> extends NLPComponent<N,String,POSState<N>>
{
	private static final long serialVersionUID = -7926217238116337203L;
	private AmbiguityClassMap ambiguity_class_map;
	private Set<String> train_word_set;
	
	public POSTagger(StringModel model)
	{
		super(new StringModel[]{model});
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
	
	public Set<String> getTrainWordSet()
	{
		return train_word_set;
	}

	public void setTrainWordSet(Set<String> set)
	{
		train_word_set = set;
	}
	
//	============================== PROCESS ==============================
	
	@Override
	protected POSState<N> createState(N[] nodes)
	{
		return new POSState<>(nodes);
	}

	@Override
	protected String getLabel(POSState<N> state, StringVector vector)
	{
		return isTrain() ? state.getGoldLabel() : models[0].predictBest(vector).getLabel();
	}

	@Override
	protected void addInstance(String label, StringVector vector)
	{
		models[0].addInstance(new StringInstance(label, vector));
	}

	@Override
	protected StringVector extractFeatures(POSState<N> state)
	{
		StringVector x = new StringVector();
		int type = 0;
		
		addWordForm      (x, state,  0, type++);
		addWordForm      (x, state,  1, type++);
		addWordForm      (x, state, -1, type++);
		addPOSTag        (x, state, -1, type++);
		addAmbiguityClass(x, state,  0, type++);
		
		return x;
	}
	
	protected void addWordForm(StringVector x, POSState<N> state, int window, int type)
	{
		N node = state.getNode(window);
		if (node != null && includeWordForm(node)) x.add(type, node.getWordForm());
	}
	
	protected void addPOSTag(StringVector x, POSState<N> state, int window, int type)
	{
		N node = state.getNode(window);
		if (node != null) x.add(type, node.getPOSTag());
	}
	
	protected void addAmbiguityClass(StringVector x, POSState<N> state, int window, int type)
	{
		N node = state.getNode(window);
		if (node != null) x.add(type, ambiguity_class_map.get(node));
	}
	
	private boolean includeWordForm(N node)
	{
		return train_word_set == null || train_word_set.contains(StringUtils.toLowerCase(node.getSimplifiedWordForm()));
	}
}
