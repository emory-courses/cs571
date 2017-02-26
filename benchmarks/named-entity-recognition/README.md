# Named Entity Recognition

## CoNLL'03 Shared Task

Source:

* http://www.cnts.ua.ac.be/conll2003/ner

Files:

* [conll03.eng.trn.gold.tsv](conll03.eng.trn.gold.tsv): training set
* [conll03.eng.dev.gold.tsv](conll03.eng.dev.gold.tsv): development set
* [conll03.eng.tst.unlabeled.tsv](conll03.eng.tst.unlabeled.tsv): evaluation set

Format:

```
token ::= <word_form><tab><lemma><tab><pos_tag><tab><ner_tag>
sentence ::= (<token><new_line>)+
```

* Each sentence is delimited by a blank line.
* Lemmas and part-pf-speech tags are automatically generated.

Labels:

* `B-*`: begin
* `I-*`: inside
* `L-*`: last
* `O-*`: outside
* `U-*`: unit
* `*-PER`: person
* `*-LOC`: location
* `*-ORG`: location
* `*-MISC`: miscellaneous

Benchmark:

| Approach | F1 | EXT |
|---|:-:|:-:|
| [Suzuki and Isozaki (2008)](https://aclweb.org/anthology/P08-1076) | 89.92 | O |
| [Turian et al. (2010)](http://www.aclweb.org/anthology/P10-1040) | 90.36 | O |
| [Ratinov and Roth (2009)](http://www.aclweb.org/anthology/W09-1119) | 90.57 |   |
| [Lin and Wu (2009)](https://www.aclweb.org/anthology/P09-1116) | 90.90 | O |
| [Passos et al. (2014)](http://www.aclweb.org/anthology/W14-1609) | 90.90 | O |
| [Lample et al. (2016)](http://www.aclweb.org/anthology/N16-1030) | 90.94 |   |
| [Choi (2016)](https://www.aclweb.org/anthology/N16-1031) | 91.00 |   |
| [Suzuki et al. (2011)](http://www.aclweb.org/anthology/P11-2112) | 91.02 | O |
| [Chiu and Nichols](https://www.aclweb.org/anthology/Q16-1026) | **91.62** (± 0.33) | O |
