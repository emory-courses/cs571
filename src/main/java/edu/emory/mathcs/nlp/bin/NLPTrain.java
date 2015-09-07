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

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.util.NLPNode;
import edu.emory.mathcs.nlp.component.util.TSVIndex;
import edu.emory.mathcs.nlp.component.util.TSVReader;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NLPTrain<N extends NLPNode>
{
	protected String train_path;
	protected String train_ext;
	protected String develop_path;
	protected String develop_ext;

	
	protected TSVIndex<N> tsv_index;
	
	protected void iterate(List<String> inputFiles, Consumer<N[]> f) throws IOException
	{
		TSVReader<N> reader = new TSVReader<>(tsv_index);
		N[] nodes;
		
		for (String inputFile : inputFiles)
		{
			reader.open(IOUtils.createFileInputStream(inputFile));
			
			while ((nodes = reader.next()) != null)
				f.accept(nodes);
		}
	}
}
