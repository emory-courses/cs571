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
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.kohsuke.args4j.Option;

import edu.emory.mathcs.nlp.common.util.BinUtils;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.util.eval.Eval;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.component.util.state.NLPState;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.sgd.StochasticGradientDescent;

/**
 * Provide instances and methods for training NLP components.
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class NLPTrain<N,L,S extends NLPState<N,L>>
{
	@Option(name="-c", usage="confinguration file (required)", required=true, metaVar="<filename>")
	public String configuration_file;
	@Option(name="-t", usage="training path (required)", required=true, metaVar="<filepath>")
	public String train_path;
	@Option(name="-te", usage="training file extension (default: *)", required=false, metaVar="<string>")
	public String train_ext = "*";
	@Option(name="-d", usage="development path (required)", required=true, metaVar="<filepath>")
	public String develop_path;
	@Option(name="-de", usage="development file extension (default: *)", required=false, metaVar="<string>")
	public String develop_ext = "*";
	@Option(name="-m", usage="model filename (optional)", required=false, metaVar="<filename>")
	public String model_file = null;
	
	public NLPTrain() {};
	
	public NLPTrain(String[] args)
	{
		BinUtils.initArgs(args, this);
	}
	
	protected void iterate(TSVReader<N> reader, List<String> inputFiles, Consumer<N[]> f)
	{
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
	
	/** Collects necessary lexicons for the component. */
	public abstract void collect(TSVReader<N> reader, List<String> inputFiles, NLPComponent<N,L,S> component, NLPConfig<N> configuration);
	
	/**
	 * Collects, trains and bootstraps the statistical models.
	 * @param component the statistical models and the evaluator are already set (see {@link NLPComponent#setModels(StringModel[])} 
	 */
	public void train(TSVReader<N> reader, List<String> trainFiles, List<String> developFiles, NLPComponent<N,L,S> component, NLPConfig<N> configuration)
	{
		BinUtils.LOG.info("Collecting lexicons:\n");
		collect(reader, trainFiles, component, configuration);
		
		boolean bootstrap = configuration.isBootstrap();
		StringModel[] models = component.getModels();
		int i, size = models.length;
		float[][] prevWeight = new float[size][];
		double currScore = 0, prevScore;
		
		for (int boot=0; ; boot++)
		{
			BinUtils.LOG.info(String.format("\nBootstrapping: %d\n\n", boot));
			component.setFlag(boot == 0 ? NLPFlag.TRAIN : NLPFlag.BOOTSTRAP);
			iterate(reader, trainFiles, nodes -> component.process(nodes));
			
			component.setFlag(NLPFlag.EVALUATE);
			prevScore = currScore;
			currScore = train(reader, developFiles, component, configuration);
			if (!bootstrap) break;
			
			if (prevScore <= currScore+0.01)
			{
				for (i=0; i<size; i++)
					prevWeight[i] = models[i].getWeightVector().toArray().clone();
			}
			else
			{
				for (i=0; i<size; i++) models[i].getWeightVector().fromArray(prevWeight[i]);
				break;
			}
		}
		
//		BinUtils.LOG.info("\nCollecting instances:\n\n");
//		component.setFlag(NLPFlag.BOOTSTRAP);
//		iterate(reader, trainFiles, nodes -> component.process(nodes));
//		
//		component.setFlag(NLPFlag.EVALUATE);
//		train(reader, developFiles, component, configuration);
	}
	
	public double train(TSVReader<N> reader, List<String> developFiles, NLPComponent<N,?,?> component, NLPConfig<N> configuration)
	{
		Eval eval = component.getEval();
		StringModel[] models = component.getModels();
		StochasticGradientDescent[] learners = configuration.getLearners(models);

		final double delta = 0;
		int epoch, i, size = models.length;
		float[][] prevWeight = new float[size][];
		double[] prevScore = new double[size], currScore = new double[size];
		
		for (i=0; i<size; i++)
		{
			BinUtils.LOG.info(learners[i].toString()+", bias = "+models[i].getBias()+"\n");
			BinUtils.LOG.info(models[i].trainInfo()+"\n");
		}
		
		for (epoch=1; ;epoch++)
		{
			for (i=0; i<size; i++)
			{
				if (currScore[i] < 0) continue;
				eval.clear();
				learners[i].train(models[i].getInstanceList());
				iterate(reader, developFiles, nodes -> component.process(nodes));
				
				prevScore[i] = currScore[i];
				currScore[i] = eval.score();
				
				if (currScore[i]+delta > prevScore[i])
				{
					prevWeight[i] = models[i].getWeightVector().toArray().clone();
				}
				else
				{
					models[i].getWeightVector().fromArray(prevWeight[i]);
					currScore[i] = -1;
				}
				
				BinUtils.LOG.info(String.format("%4d.%d: %5.2f\n", epoch, i, eval.score()));				
			}
			
			if (Arrays.stream(currScore).allMatch(d -> d < 0)) break; 
		}
		
		double best = Arrays.stream(prevScore).max().getAsDouble();
		BinUtils.LOG.info(String.format("Best: %5.2f\n", best));
		return best;
	}
}
