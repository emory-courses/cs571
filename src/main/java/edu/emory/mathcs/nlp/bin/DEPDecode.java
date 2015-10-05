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

import java.io.ObjectInputStream;
import java.io.PrintStream;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.Joiner;
import edu.emory.mathcs.nlp.component.dep.DEPIndex;
import edu.emory.mathcs.nlp.component.dep.DEPNode;
import edu.emory.mathcs.nlp.component.dep.DEPParser;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DEPDecode
{
	@SuppressWarnings("unchecked")
	static public void main(String[] args) throws Exception
	{
		String modelFile  = args[0];
		String testFile   = args[1];
		String outputFile = args[2];
		ObjectInputStream in = IOUtils.createObjectXZBufferedInputStream(modelFile);
		DEPParser<DEPNode> parser = (DEPParser<DEPNode>)in.readObject();
		PrintStream out = IOUtils.createBufferedPrintStream(outputFile);
	
		DEPIndex index = new DEPIndex(0, 1, 2, 3);
		TSVReader<DEPNode> reader = new TSVReader<DEPNode>(index);
		reader.open(IOUtils.createFileInputStream(testFile));
		long st, et, sum = 0;
		DEPNode[] nodes;
		
		while ((nodes = reader.next()) != null)
		{
			st = System.currentTimeMillis();
			parser.process(nodes);
			et = System.currentTimeMillis();
			sum = et - st;
			out.println(Joiner.join(nodes, "\n", 1)+"\n");
		}
			
		System.out.println(sum);
		out.close();
	}
}
