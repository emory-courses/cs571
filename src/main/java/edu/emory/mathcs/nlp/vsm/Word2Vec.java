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
package edu.emory.mathcs.nlp.vsm;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.kohsuke.args4j.Option;

import edu.emory.mathcs.nlp.common.random.XORShiftRandom;
import edu.emory.mathcs.nlp.common.util.BinUtils;
import edu.emory.mathcs.nlp.common.util.FileUtils;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.common.util.Sigmoid;
import edu.emory.mathcs.nlp.vsm.optimizer.HierarchicalSoftmax;
import edu.emory.mathcs.nlp.vsm.optimizer.NegativeSampling;
import edu.emory.mathcs.nlp.vsm.optimizer.Optimizer;
import edu.emory.mathcs.nlp.vsm.reader.SentenceReader;
import edu.emory.mathcs.nlp.vsm.util.Vocabulary;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 * http://arxiv.org/pdf/1301.3781.pdf
 * http://www-personal.umich.edu/~ronxin/pdf/w2vexp.pdf
 */
public class Word2Vec
{
	@Option(name="-train", usage="path to the training file or the directory containig the training files.", required=true, metaVar="<filepath>")
	String train_path = null;
	@Option(name="-output", usage="output file to save the resulting word vectors.", required=true, metaVar="<filename>")
	String output_file = null;
	@Option(name="-ext", usage="extension of the training files (default: \"*\").", required=false, metaVar="<string>")
	String train_ext = "*";
	@Option(name="-size", usage="size of word vectors (default: 100).", required=false, metaVar="<int>")
	int vector_size = 100;
	@Option(name="-window", usage="max-window of contextual words (default: 5).", required=false, metaVar="<int>")
	int max_skip_window = 5;
	@Option(name="-sample", usage="threshold for occurrence of words (default: 1e-3). Those that appear with higher frequency in the training data will be randomly down-sampled.", required=false, metaVar="<double>")
	double subsample_threshold = 1e-3;
	@Option(name="-negative", usage="number of negative examples (default: 5; common values are 3 - 10). If negative = 0, use Hierarchical Softmax instead of Negative Sampling.", required=false, metaVar="<int>")
	int negative_size = 5;
	@Option(name="-threads", usage="number of threads (default: 12).", required=false, metaVar="<int>")
	int thread_size = 12;
	@Option(name="-iter", usage="number of training iterations (default: 5).", required=false, metaVar="<int>")
	int train_iteration = 5;
	@Option(name="-min-count", usage="min-count of words (default: 5). This will discard words that appear less than <int> times.", required=false, metaVar="<int>")
	int min_count = 5;
	@Option(name="-alpha", usage="initial learning rate (default: 0.025 for skip-gram; use 0.05 for CBOW).", required=false, metaVar="<double>")
	double alpha_init = 0.025;
	@Option(name="-binary", usage="If set, save the resulting vectors in binary moded.", required=false, metaVar="<boolean>")
	boolean binary = false;
	@Option(name="-cbow", usage="If set, use the continuous bag-of-words model instead of the skip-gram model.", required=false, metaVar="<boolean>")
	boolean cbow = false;
	
	final double ALPHA_MIN_RATE  = 0.0001;      
	final int    MAX_CODE_LENGTH = 40;
	
	Sigmoid sigmoid;
	Vocabulary vocab;
	long word_count_train;
	double subsample_size;
	Optimizer optimizer;
	
	volatile long word_count_global;	// word count dynamically updated by all threads
	volatile double alpha_global;		// learning rate dynamically updated by all threads
	volatile public float[] W;			// weights between the input and the hidden layers
	volatile public float[] V;			// weights between the hidden and the output layers
	
	public Word2Vec(String[] args)
	{
		BinUtils.initArgs(args, this);
		sigmoid = new Sigmoid();

		try
		{
			train(FileUtils.getFileList(train_path, "*", false));
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
//	=================================== Training ===================================
	
	public void train(List<String> filenames) throws Exception
	{
		BinUtils.LOG.info("Reading vocabulary:\n");
		vocab = new Vocabulary();
		word_count_train = new SentenceReader().learn(filenames, vocab, min_count);
		BinUtils.LOG.info(String.format("- types = %d, tokens = %d\n", vocab.size(), word_count_train));
		
		BinUtils.LOG.info("Initializing neural network.\n");
		initNeuralNetwork();
		
		BinUtils.LOG.info("Initializing optimizer.\n");
		optimizer = isNegativeSampling() ? new NegativeSampling(vocab, sigmoid, vector_size, negative_size) : new HierarchicalSoftmax(vocab, sigmoid, vector_size);

		BinUtils.LOG.info("Training vectors:");
		word_count_global = 0;
		alpha_global      = alpha_init;
		subsample_size    = subsample_threshold * word_count_train;
		ExecutorService executor = Executors.newFixedThreadPool(thread_size);
		
		for (String filename : filenames)
			executor.execute(new TrainTask(filename));
		
		executor.shutdown();
		
		try
		{
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch (InterruptedException e) {e.printStackTrace();}
		
		BinUtils.LOG.info("Saving word vectors.\n");
		saveModel();
	}
	
	class TrainTask implements Runnable
	{
		private String filename;
		
		public TrainTask(String filename)
		{
			this.filename = filename;
		}
		
		@Override
		public void run()
		{
			SentenceReader reader = new SentenceReader(IOUtils.createFileInputStream(filename));
			Random  rand  = new XORShiftRandom(filename.hashCode());
			float[] neu1  = cbow ? new float[vector_size] : null;
			float[] neu1e = new float[vector_size];
			int     iter  = 0;
			int     index, window;
			int[]   words;
			
			while (true)
			{
				words = next(reader, rand);
				
				if (words == null)
				{
					BinUtils.LOG.info(String.format("%s: %d\n", FileUtils.getBaseName(filename), iter));
					if (++iter == train_iteration) break;
					adjustLearningRate();
					reader.close(); reader.open(IOUtils.createFileInputStream(filename));
					continue;
				}
				
				for (index=0; index<words.length; index++)
				{
					window = rand.nextInt() % max_skip_window;	// dynamic window size
					Arrays.fill(neu1 , 0);
					Arrays.fill(neu1e, 0);
					
					if (cbow) bagOfWords(words, index, window, rand, neu1e, neu1);
					else      skipGram  (words, index, window, rand, neu1e);
				}
			}
		}
	}
	
	void adjustLearningRate()
	{
		double rate = Math.max(ALPHA_MIN_RATE, 1 - MathUtils.divide(word_count_global, train_iteration * word_count_train + 1));
		alpha_global = alpha_init * rate;
	}
	
	void bagOfWords(int[] words, int index, int window, Random rand, float[] neu1e, float[] neu1)
	{
		int i, j, k, l, wc = 0, word = words[index];

		// input -> hidden
		for (i=-window,j=index+i; i<=window; i++,j++)
		{
			if (i == 0 || words.length <= j || j < 0) continue;
			l = words[j] * vector_size;
			for (k=0; k<vector_size; k++) neu1[k] += W[k+l];
			wc++;
		}
		
		if (wc == 0) return;
		for (k=0; k<vector_size; k++) neu1[k] /= wc;
		optimizer.learnBagOfWords(rand, word, W, neu1, neu1e, alpha_global);
		
		// hidden -> input
		for (i=-window,j=index+i; i<=window; i++,j++)
		{
			if (i == 0 || words.length <= j || j < 0) continue;
			l = words[j] * vector_size;
			for (k=0; k<vector_size; k++) W[k+l] += neu1e[k];
		}
	}
	
	void skipGram(int[] words, int index, int window, Random rand, float[] neu1e)
	{
		int i, j, k, l1, word = words[index];
		
		for (i=-window,j=index+i; i<=window; i++,j++)
		{
			if (i == 0 || words.length <= j || j < 0) continue;
			l1 = words[j] * vector_size;
			Arrays.fill(neu1e, 0);
			optimizer.learnSkipGram(rand, word, W, V, neu1e, alpha_global, l1);
			
			// hidden -> input
			for (k=0; k<vector_size; k++) W[l1+k] += neu1e[k];
		}
	}
	
//	=================================== Helper Methods ===================================

	boolean isNegativeSampling()
	{
		return negative_size > 0;
	}
	
	/** Initializes weights between the input layer to the hidden layer using random numbers between [-0.5, 0.5]. */
	void initNeuralNetwork()
	{
		int size = vocab.size() * vector_size;
		Random rand = new XORShiftRandom(1);

		W = new float[size];
		V = new float[size];
		
		for (int i=0; i<size; i++)
			W[i] = (float)((rand.nextDouble() - 0.5) / vector_size);
	}
	
	int[] next(SentenceReader reader, Random rand)
	{
		String[] words = reader.next();
		if (words == null) return null;
		int[] next = new int[words.length];
		int i, j, index, count = 0;
		double d;
		
		for (i=0,j=0; i<words.length; i++)
		{
			index = vocab.indexOf(words[i]);
			if (index < 0) continue;
			count++;
			
			// sub-sampling: randomly discards frequent words
			if (subsample_threshold > 0)
			{
				d = (Math.sqrt(MathUtils.divide(vocab.get(index).count, subsample_size)) + 1) * (subsample_size / vocab.get(index).count);
				if (d < rand.nextDouble()) continue;
			}
			
			next[j++] = index;
		}
		
		word_count_global += count;
		return (j == 0) ? next(reader, rand) : (j == words.length) ? next : Arrays.copyOf(next, j);
	}
	
	void saveModel() throws IOException
	{
		PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(output_file)));
		save(out, binary);
		out.close();
	}
	
	public void save(PrintStream out, boolean binary)
	{
		out.printf("%d %d\n", vocab.size(), vector_size);
		int i, j, l;
		
		for (i=0; i<vocab.size(); i++)
		{
			out.print(vocab.get(i).form);
			l = i * vector_size;
			
			for (j=0; j<vector_size; j++)
			{
				if (binary)	out.print(" "+Long.toBinaryString(Double.doubleToRawLongBits(W[j+l])));
				else		out.print(" "+W[j+l]);
			}
			
			out.println();
		}
	}
}
