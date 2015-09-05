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
package edu.emory.mathcs.nlp.component.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import edu.emory.mathcs.nlp.component.eval.Eval;
import edu.emory.mathcs.nlp.learn.model.StringModel;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class NLPComponent<N> implements Serializable
{
	private static final long serialVersionUID = 4546728532759275929L;
	protected StringModel[] models;
	protected StringModel   model;
	protected NLPFlag       flag;
	protected Eval          eval;
	
//	============================== CONSTRUCTORS ==============================
	
	public NLPComponent(NLPFlag flag, StringModel model)
	{
		setModel(model);
		setFlag(flag);
	}
	
	public NLPComponent(NLPFlag flag, StringModel[] models)
	{
		setModels(models);
		setFlag(flag);
	}
	
//	============================== SERIALIZE ==============================
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		models = (StringModel[])in.readObject();
		model  = (StringModel  )in.readObject();
		readLexicons(in);
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.writeObject(models);
		out.writeObject(model);
		writeLexicons(out);
	}
	
	protected abstract void readLexicons (ObjectInputStream in) throws IOException, ClassNotFoundException;
	protected abstract void writeLexicons(ObjectOutputStream out) throws IOException;
	
//	============================== MODELS ==============================
	
	public StringModel getModel()
	{
		return model;
	}
	
	public void setModel(StringModel model)
	{
		this.model = model;
	}
	
	public StringModel[] getModels()
	{
		return models;
	}
	
	public void setModels(StringModel[] model)
	{
		this.models = model;
	}
	
//	============================== FLAG ==============================
	
	public NLPFlag getFlag()
	{
		return flag;
	}
	
	public void setFlag(NLPFlag flag)
	{
		this.flag = flag;
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
	
//	============================== EVALUATOR ==============================

	public Eval getEval()
	{
		return eval;
	}
	
	public void setEval(Eval eval)
	{
		this.eval = eval;
	}
	
//	============================== PROCESS ==============================
	
	public abstract void process(N[] nodes);
}
