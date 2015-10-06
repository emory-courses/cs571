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
import java.util.Arrays;

import org.kohsuke.args4j.Option;

import edu.emory.mathcs.nlp.common.util.BinUtils;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.Joiner;
import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.component.dep.DEPIndex;
import edu.emory.mathcs.nlp.component.util.NLPComponent;
import edu.emory.mathcs.nlp.component.util.node.NLPNode;
import edu.emory.mathcs.nlp.component.util.reader.TSVIndex;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;
import edu.emory.mathcs.nlp.component.util.state.NLPState;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SpeedTest<N extends NLPNode,S extends NLPState<N>>
{
	@Option(name="-m", usage="model file (required)", required=true, metaVar="<filename>")
	public String model_file;
	@Option(name="-i", usage="input file (required)", required=true, metaVar="<filename>")
	public String input_file;
	@Option(name="-o", usage="output file (optional)", required=false, metaVar="<filename>")
	public String output_file;
	
	@SuppressWarnings("unchecked")
	public SpeedTest(String[] args) throws Exception
	{
		BinUtils.initArgs(args, this);
		
		ObjectInputStream obj = IOUtils.createObjectXZBufferedInputStream(model_file);
		NLPComponent<N,S> component = (NLPComponent<N,S>)obj.readObject();
		obj.close();
		
		PrintStream out = (output_file != null) ? IOUtils.createBufferedPrintStream(output_file) : null;
		TSVReader<N> reader = new TSVReader<N>(createTSVIndex());
		reader.open(IOUtils.createFileInputStream(input_file));
		SpeedEval speed = new SpeedEval(component, 10);
		N[] nodes;
		
		while ((nodes = reader.next()) != null)
		{
			speed.measure(nodes);
			if (out != null) out.println(Joiner.join(nodes, "\n", startIndex(nodes))+"\n");
		}
		
		out.close();
		reader.close();
		System.out.println(speed.toString());
	}
	
	@SuppressWarnings("unchecked")
	public TSVIndex<N> createTSVIndex()
	{
		return (TSVIndex<N>)new DEPIndex(0, 1, 2, 3);
	}
	
	private int startIndex(N[] nodes)
	{
		return nodes[0].getID() == 0 ? 1 : 0;
	}
	
	class SpeedEval
	{
		NLPComponent<N,S> component;
		long   sentence_count , token_count , total_time;
		long[] sentence_counts, token_counts, total_times;
		
		public SpeedEval(NLPComponent<N,S> component, int beans)
		{
			this.component  = component;
			sentence_counts = new long[beans];
			token_counts    = new long[beans];
			total_times     = new long[beans];
		}
		
		public void reset()
		{
			sentence_count = token_count = total_time = 0;
			Arrays.fill(sentence_counts, 0);
			Arrays.fill(token_counts   , 0);
			Arrays.fill(total_times    , 0);
		}
		
		public void measure(N[] nodes)
		{
			long st, et, t;
			
			st = System.currentTimeMillis();
			component.process(nodes);
			et = System.currentTimeMillis();
			t = et - st;

			int wc = nodes.length - startIndex(nodes);
			sentence_count++;
			token_count += wc;
			total_time += t;
			
			int idx = (wc - 1) / total_times.length;
			
			if (idx < total_times.length)
			{
				sentence_counts[idx]++;
				token_counts[idx] += wc;
				total_times[idx] += t;
			}
		}
		
		@Override
		public String toString()
		{
			StringBuilder build = new StringBuilder();
			
			build.append(String.format("Sentence count: %d\n", sentence_count));
			build.append(String.format("Sentences/Sec.: %f\n", MathUtils.divide(sentence_count, 0.001 * total_time)));
			for (int i=0; i<total_times.length; i++)
				build.append(String.format("%3: %f\n", MathUtils.divide(sentence_counts[i], 0.001 * total_times[i])));

			build.append("\n");
			
			build.append(String.format("Token count: %d\n", token_count));
			build.append(String.format("Tokens/Sec.: %f\n", MathUtils.divide(token_count, 0.001 * total_time)));
			for (int i=0; i<total_times.length; i++)
				build.append(String.format("%3: %f\n", MathUtils.divide(token_counts[i], 0.001 * total_times[i])));
			
			return build.toString();
		}
	}
	
	static public void main(String[] args) throws Exception
	{
		new SpeedTest<>(args);
	}
}
