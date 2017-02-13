# Sentiment Analysis

## Stanford Sentiment Treebank

Source:

* http://nlp.stanford.edu/sentiment/treebank.html

Files:

* [sst.trn.gold.tsv](sst.trn.gold.tsv): training set
* [sst.dev.gold.tsv](sst.dev.gold.tsv): development set
* [sst.tst.unlabeled.tsv](sst.tst.unlabeled.tsv): evaluation set

Format:

```
line ::= <sentiment><tab><document>
sentiment ::= 0|1|2|3|4
document ::= <token>(<space><token>)*
```

* Each line represents a document.

Labels:

* `0`: very negative
* `1`: negative
* `2`: neutral
* `3`: positive
* `4`: very positive

Benchmark:

| Model | 5 classes | 2 classes |
|---|:-:|:-:|
| [Socher et al. (2013)](http://www.aclweb.org/anthology/D13-1170) | 45.7 | 85.4 |
| [Kim (2014)](http://www.aclweb.org/anthology/D14-1181) | 48.0 | 87.2 |
| [Kalchbrenner et al. (2014)](http://www.aclweb.org/anthology/P14-1062) | 48.5 | 86.8 |
| [Le and Mikolov (2014)](http://www.jmlr.org/proceedings/papers/v32/le14.pdf) | 48.7 | 87.8 |
| [Shin et al. (2016)](https://arxiv.org/abs/1610.06272) | 48.8 | -    |
| [Yin and Schütze (2015)](http://www.aclweb.org/anthology/K15-1021) | 49.6 | **89.4** |
| [Irsoy and Cardie (2014)]((https://papers.nips.cc/paper/5551-deep-recursive-neural-networks-for-compositionality-in-language.pdf)) | 49.8 | 86.6 |
| [Tai et al. (2015)](http://www.aclweb.org/anthology/P15-1150) | **51.0** | 88.0 |
