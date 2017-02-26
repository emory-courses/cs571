# Dependency Parsing

## Penn Treebank

Source:

* https://catalog.ldc.upenn.edu/LDC99T42

Files:

* [wsj-dep.trn.gold.tsv](wsj-dep.trn.gold.tsv): training set (WSJ sections 2 - 21)
* [wsj-dep.dev.gold.tsv](wsj-dep.dev.gold.tsv): development set (WSJ sections 22, 24)
* [wsj-dep.tst.unlabeled.tsv](wsj-dep.tst.unlabeled.tsv): evaluation set (WSJ sections 23; unlabeled)

Format:

```
token ::= <node_id><tab><word_form><tab><lemma><tab><pos_tag><tab><feats><tab><y_m><tab><stanford>
<y_m> ::= <head_id><tab><dependency_label>
<stanford> ::= <head_id><tab><dependency_label>
sentence ::= (<token><new_line>)+
```

* Each sentence is delimited by a blank line.
* `y_m` uses the headrules introduced by [Yamada and Matsumoto, 2003](https://pdfs.semanticscholar.org/f0e1/883cf9d1b3c911125f46359f908557fc5827.pdf).
* `stanford` uses the [Stanford typed dependency](http://www.aclweb.org/anthology/W08-1301).

| Y&M Headrules | UAS | LAS | T/G | EXT |
|---|:-:|:-:|:-:|:-:|
| Honnibal et al. (2013)    | 91.1  | 88.9  | T | |
| Zhang and Zhang (2015)    | 91.80 | 90.68 | T | |
| Zhang and Nivre (2011)    | 92.9  | 91.8  | T | |
| Choi and McCallum (2013)  | 92.96 | 91.93 | T | |
| Koo and Collins (2010)    | 93.04 |   -   | G | |
| Ma et al. (2014)          | 93.06 |   -   | T | |
| Koo et al. (2008)         | 93.16 |   -   | G | O |
| Chen et al. (2009)        | 93.16 |   -   | G | O |
| Li et al. (2014)          | 93.19 |   -   | G | O |
| Zhou et al. (2015)        | 93.28 | 92.35 | T | O |
| Pei et. al. (2015)        | 93.29 | 92.13 | G | O |
| Carreras et al. (2008)    | 93.5  |   -   | G | O |
| Chen et al. (2013)        | 93.77 |   -   | G | O |
| Suzuki et al. (2009)      | 93.79 |   -   | G | O |
| Suzuki et al. (2011)      | 94.22 |   -   | G | O |

| Stanford | UAS | LAS | T/G | EXT |
|---|:-:|:-:|:-:|:-:|
| Goldberg and Nivre (2012) | 90.96 | 88.72 | T | |
| Honnibal et al. (2013)          | 91.0  | 88.9  | T | |
| Chen and Manning (2014)         | 91.8  | 89.6  | T | |
| Zhang and Nivre (2011)          | 93.5  | 91.9  | T | |
| Kiperwasser and Goldberg (2015) | 92.45 |   -   | | | |
| Dyer et. al. (2015)             | 93.1  | 90.9  | | | |
| Weiss et. al. (2015)            | 94.26 | 92.41 | | | |

* [Simple Semi-supervised Dependency Parsing](http://aclweb.org/anthology/P08-1068), Koo et al., ACL, 2008.
* [TAG, Dynamic Programming, and the Perceptron for Efficient, Feature-rich Parsing](), Carreras et al., CoNLL, 2008.
* [An Empirical Study of Semi-supervised Structured Conditional Models for Dependency Parsing](http://www.aclweb.org/anthology/D09-1058), Suzuki et al., EMNLP, 2009.
* [Improving Dependency Parsing with Subtrees from Auto-Parsed Data](http://www.aclweb.org/anthology/D09-1060), Chen et al., EMNLP, 2009.
* [Efficient Third-order Dependency Parsers](http://www.aclweb.org/anthology/P10-1001), Koo and Collins, ACL, 2010.
* [Learning Condensed Feature Representations from Large Unsupervised Data Sets for Supervised Learning](http://www.aclweb.org/anthology/P11-2112), Suzuki et al., ACL, 2011.
* [Transition-based Dependency Parsing with Rich Non-local Features](http://www.anthology.aclweb.org/P11-2033), Zhang and Nivre, ACL, 2011.
* [A Dynamic Oracle for Arc-Eager Dependency Parsing](http://www.aclweb.org/anthology/C12-1059), Goldberg and Nivre, COLING, 2012.
* [Transition-based Dependency Parsing with Selectional Branching](http://anthology.aclweb.org/P13-1104), Choi and McCallum, ACL, 2013.
* [Semi-supervised Feature Transformation for Dependency Parsing](http://www.aclweb.org/anthology/D13-1129), Chen et al., EMNLP, 2013.
* [Ambiguity-aware Ensemble Training for Semi-supervised Dependency Parsing](http://www.aclweb.org/anthology/P14-1043), Li et al., ACL, 2014.
* [Punctuation Processing for Projective Dependency Parsing](https://aclweb.org/anthology/P14-2128), Ma et al., ACL, 2014.
* [A Fast and Accurate Dependency Parser using Neural Networks](http://aclweb.org/anthology/D14-1082), Chen and Manning, EMNLP, 2014.
* [A Non-Monotonic Arc-Eager Transition System for Dependency Parsing](http://anthology.aclweb.org/W13-3518), Honnibal et al., CoNLL, 2015.
* [Combining Discrete and Continuous Features for Deterministic Transition-based Dependency Parsing](http://aclweb.org/anthology/D15-1153), Zhang and Zhang, EMNLP, 2015
* [Transition-Based Dependency Parsing with Stack Long Short-Term Memory](http://aclweb.org/anthology/P15-1033), Dyer et al., ACL, 2015.
* [Structured Training for Neural Network Transition-Based Parsing](http://aclweb.org/anthology/P15-1032), Weiss et al., ACL, 2015.
* [An Effective Neural Network Model for Graph-based Dependency Parsing](http://aclweb.org/anthology/P15-1031), Pei et. al., ACL, 2015.
* [A Neural Probabilistic Structured-Prediction Model for Transition-Based Dependency Parsing](http://aclweb.org/anthology/P15-1117), Zhou et al., ACL, 2015
* [Semi-supervised Dependency Parsing using Bilexical Contextual Features from Auto-Parsed Data](http://aclweb.org/anthology/D15-1158), Kiperwasser and Goldberg, EMNLP, 2015.