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
package edu.emory.mathcs.nlp.bin;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import edu.emory.mathcs.nlp.common.util.BinUtils;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.eval.Eval;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.component.util.NLPConfig;
import edu.emory.mathcs.nlp.component.util.NLPFlag;
import edu.emory.mathcs.nlp.component.util.NLPNode;
import edu.emory.mathcs.nlp.component.util.TSVIndex;
import edu.emory.mathcs.nlp.component.util.TSVReader;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;
import edu.emory.mathcs.nlp.learn.weight.WeightVector;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class NLPTrain<N extends NLPNode>
{
	protected String train_path;
	protected String train_ext;
	protected String develop_path;
	protected String develop_ext;
	
	protected TSVIndex<N> tsv_index;
	
	protected void iterate(List<String> inputFiles, Consumer<N[]> f)
	{
		TSVReader<N> reader = new TSVReader<>(tsv_index);
		N[] nodes;
		
		for (String inputFile : inputFiles)
		{
			reader.open(IOUtils.createFileInputStream(inputFile));
			
			try
			{
				while ((nodes = reader.next()) != null)
					f.accept(nodes);
			}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	/**
	 * Collects, trains and bootstraps the statistical models.
	 * @param component this NLP component contains the statistical model(s) and the evaluator. 
	 */
	public void train(List<String> trainFiles, List<String> developFiles, NLPComponent<N> component, NLPConfig<N> configuration) throws IOException
	{
		BinUtils.LOG.info("Collecting lexicons:\n");
		collect(trainFiles, component, configuration);
		
		BinUtils.LOG.info("Collecting instances:\n");
		iterate(trainFiles, nodes -> component.process(nodes));
		BinUtils.LOG.info(component.getModel().trainInfo()+"\n");
		
		BinUtils.LOG.info("Trainig:\n");
		train(developFiles, component, configuration);
	}
	
	public void train(List<String> developFiles, NLPComponent<N> component, NLPConfig<N> configuration) throws IOException
	{
		Eval eval = component.getEval();
		StringModel model = component.getModel();
		WeightVector vector = model.getWeightVector();
		
		StochasticGradientDescent sgd = configuration.getTrainer(model);
		component.setFlag(NLPFlag.EVALUATE);
		
		Double prevScore, currScore = 0d;
		float[] backup = null;
		
		for (int i=1; ; i++)
		{
			eval.clear();
			sgd.train(model.getInstanceList());
			iterate(developFiles, nodes -> component.process(nodes));
			
			prevScore = currScore;
			currScore = eval.score();
			
			if (currScore > prevScore)
			{
				backup = vector.toArray().clone();
			}
			else
			{
				vector.fromArray(backup);
				break;
			}
			
			BinUtils.LOG.info(String.format("- %3d: %5.2f\n", i, eval.score()));	
		}
	}
	
	/** Collects necessary lexicons for the component. */
	public abstract void collect(List<String> inputFile, NLPComponent<N> component, NLPConfig<N> configuration);
}
