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

import java.util.List;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.dep.DEPConfig;
import edu.emory.mathcs.nlp.component.dep.DEPNode;
import edu.emory.mathcs.nlp.component.dep.DEPParser;
import edu.emory.mathcs.nlp.component.dep.DEPState;
import edu.emory.mathcs.nlp.component.dep.feature.DEPFeatureTemplate0;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.component.util.config.NLPConfig;
import edu.emory.mathcs.nlp.component.util.eval.AccuracyEval;
import edu.emory.mathcs.nlp.component.util.eval.Eval;
import edu.emory.mathcs.nlp.component.util.feature.FeatureTemplate;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.component.util.train.NLPTrain;
import edu.emory.mathcs.nlp.learn.model.StringModel;
import edu.emory.mathcs.nlp.learn.weight.MultinomialWeightVector;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DEPTrain extends NLPTrain<DEPNode,DEPState<DEPNode>>
{
	public DEPTrain(String[] args)
	{
		super(args);
	}

	@Override
	protected NLPConfig<DEPNode> createConfiguration(String filename)
	{
		return new DEPConfig(IOUtils.createFileInputStream(filename));
	}
	
	@Override
	protected Eval createEvaluator()
	{
		return new AccuracyEval();
	}
	
	@Override
	protected NLPComponent<DEPNode,DEPState<DEPNode>> createComponent()
	{
		return new DEPParser<>(new StringModel(new MultinomialWeightVector()));
	}
	
	@Override
	protected FeatureTemplate<DEPNode,DEPState<DEPNode>> createFeatureTemplate()
	{
		switch (feature_template)
		{
		case 0: return new DEPFeatureTemplate0();
		default: throw new IllegalArgumentException("Unknown feature template: "+feature_template);
		}
	}
	
	@Override
	public void collect(TSVReader<DEPNode> reader, List<String> inputFiles, NLPComponent<DEPNode,DEPState<DEPNode>> component, NLPConfig<DEPNode> configuration) {}
	
	static public void main(String[] args)
	{
		new DEPTrain(args).train();
	}
}
