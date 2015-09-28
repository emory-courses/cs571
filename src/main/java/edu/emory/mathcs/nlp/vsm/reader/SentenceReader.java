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
package edu.emory.mathcs.nlp.vsm.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.Splitter;
import edu.emory.mathcs.nlp.vsm.util.Vocabulary;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SentenceReader extends Reader<String>
{
	private BufferedReader reader;
	
	public SentenceReader() {}
	
	public SentenceReader(InputStream in)
	{
		open(in);
	}
	
	@Override
	public void open(InputStream in)
	{
		reader = IOUtils.createBufferedReader(in);
	}

	@Override
	public void close()
	{
		try
		{
			reader.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}
	
	@Override
	public void add(Vocabulary vocab, String[] nodes)
	{
		for (String node : nodes) vocab.add(node);
	}

	/**
	 * @return the next sentence if exists; otherwise, null.
	 * Words are delimited by ' ' and sentences are delimited by '\n'.
	 */
	@Override
	public String[] next()
	{
		String sen = null;
		
		try
		{
			sen = reader.readLine();
			while (sen != null && sen.isEmpty()) sen = reader.readLine();
		}
		catch (IOException e) {e.printStackTrace();}
		
		return (sen != null) ? Splitter.splitSpace(sen) : null;
	}
}
