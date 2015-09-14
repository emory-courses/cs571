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
	
//	============================== PROCESS ==============================
	
	@Override
	protected POSState<N> createState(N[] nodes)
	{
		return new POSState<>(nodes, ambiguity_class_map);
	}

	@Override
	protected String getLabel(POSState<N> state, StringVector vector)
	{
		return isTrain() ? state.getGoldLabel() : models[0].predictBest(vector).getLabel();
	}

	@Override
	protected void addInstance(String label, StringVector vector)
	{
		System.out.println(label+" "+vector.toString());
		models[0].addInstance(new StringInstance(label, vector));
	}
}
