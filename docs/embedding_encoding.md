# Embedding Encoding

## Chapters

* [N-gram Language Models](https://web.stanford.edu/~jurafsky/slp3/3.pdf), SLP: Chapter 3, Jurafsky and Martin
* [Vector Semantics and Embeddings](https://web.stanford.edu/~jurafsky/slp3/6.pdf), SLP: Chapter 6, Jurafsky and Martin


## Traditional (1/27)

### Readings

* [Indexing by Latent Semantics Analysis](https://www.aminer.org/pub/53e9a8f2b7602d97032447c4/indexing-by-latent-semantics-analysis), Deerwester et al., JASIS, 1991
* [Latent Dirichlet Allocation](http://jmlr.csail.mit.edu/papers/v3/blei03a.html), Blei et al., JMLR, 2003*

### Topics

* Text Encoding
  * Word-level: [one-hot encoding](https://en.wikipedia.org/wiki/One-hot)
  * Document-level: [bag-of-words model](https://en.wikipedia.org/wiki/Bag-of-words_model)
  * Any other level?  <!-- sense, lemma, entity, frame -->
* Similarity Measurements
  * [Euclidean distance](https://en.wikipedia.org/wiki/Euclidean_distance)
  * [Cosine similarity](https://en.wikipedia.org/wiki/Cosine_similarity)
* [Latent Semantic Analysis](https://en.wikipedia.org/wiki/Latent_semantic_analysis)
  * [Document-term matrix](https://en.wikipedia.org/wiki/Document-term_matrix)
  * [TF-IDF weighting](https://en.wikipedia.org/wiki/Tf%E2%80%93idf)
  * [Singular value decomposition](https://en.wikipedia.org/wiki/Singular_value_decomposition)
* [Language Modeling](https://en.wikipedia.org/wiki/Language_model)
  * N-gram models
  * [Smoothing](https://en.wikipedia.org/wiki/Smoothing)
  * [Maximum likelihood estimation](https://en.wikipedia.org/wiki/Maximum_likelihood_estimation)
  * [Entropy](https://en.wikipedia.org/wiki/Entropy_(information_theory))
  * [Perplexity](https://en.wikipedia.org/wiki/Perplexity)



## Modern (2/1)

### Readings

* [Distributed Representations of Words and Phrases and their Compositionality](https://papers.nips.cc/paper/5021-distributed-representations-of-words-and-phrases-and-their-compositionality.html), 
Mikolov et al., NIPS, 2013.
* [GloVe: Global Vectors for Word Representation](https://www.aclweb.org/anthology/D14-1162), Pennington et al., EMNLP, 2014*
* [Deep Contextualized Word Representations](https://aclweb.org/anthology/N18-1202), Peters et al., NAACL 2018
* [Improving Language Understanding with Unsupervised Learning](https://openai.com/blog/language-unsupervised/), Radford et al., OpenAI, 2018




## Latest (2/3)

### Readings

* [BERT: Pre-training of Deep Bidirectional Transformers for Language Understanding](https://www.aclweb.org/anthology/N19-1423/), Devlin et al., NAACL, 2019
* [RoBERTa: A Robustly Optimized BERT Pretraining Approach](https://arxiv.org/abs/1907.11692), Liu et al., arXiv, 2019
* [XLNet: Generalized Autoregressive Pretraining for Language Understanding](https://papers.nips.cc/paper/2019/hash/dc6a7e655d7e5840e66733e9ee67cc69-Abstract.html), Yang eg al., NIPS, 2019*
* [ELECTRA: Pre-training Text Encoders as Discriminators Rather Than Generators](https://openreview.net/forum?id=r1xMH1BtvB), Clark et al., ICLR 2020*


## References

* [A Neural Probabilistic Language Model](https://papers.nips.cc/paper/1839-a-neural-probabilistic-language-model.html), Bengio et al., NIPS, 2000
* [Attention is All you Need](https://papers.nips.cc/paper/2017/hash/3f5ee243547dee91fbd053c1c4a845aa-Abstract.html), Vaswani et al., NIPS, 2017
* [Enriching Word Vectors with Subword Information](http://aclweb.org/anthology/Q17-1010), Bojanowski et al., TACL, 2017 
* [Regularizing and Optimizing LSTM Language Models](https://openreview.net/pdf?id=SyyGPP0TZ), Merity et al., ICLR, 2018
* [ALBERT: A Lite BERT for Self-supervised Learning of Language Representations](https://openreview.net/forum?id=H1eA7AEtvS), Lan et al., ICLR, 2020



