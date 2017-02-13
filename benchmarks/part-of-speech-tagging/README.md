# Part-of-Speech Tagging

## Penn Treebank

Source:

* https://catalog.ldc.upenn.edu/LDC99T42

Files:

* [`wsj-pos.trn.gold.tsv`](wsj-pos.trn.gold.tsv): training set (WSJ sections 0 - 18)
* [`wsj-pos.trn.gold.tsv`](wsj-pos.dev.gold.tsv): development set (WSJ sections 19 - 21)
* [`wsj-pos.trn.unlabeled.tsv `](wsj-pos.trn.unlabeled.tsv): evaluation set (WSJ sections 22 - 24; unlabeled)

Format:

```
token ::= <word_form><tab><pos_tag>
sentence ::= (<token><new_line>)+
```

* Each sentence is delimited by a blank line.

Labels:

* `#`: pound sign
* `$`: dollar sign
* <code>\`\`</code>: left quote (e.g., `'`, `"`)
* `''`: right quote (e.g., `'`, `"`)
* `,`: comma
* `-LRB-`: left bracket (e.g., `(`, `{, `[`)
* `-RRB-`: right bracket (e.g., `)`, `}, `]`)
* `.`: ending symbol (e.g., `.`, `!`, `?`)
* `:`: colon (e.g., `:` `;`)
* `CC`: coordination conjunction
* `CD`: cardinal number
* `DT`: determiner
* `EX`: existential
* `FW`: foreign word
* `IN`: preposition
* `JJ`: adjective
* `JJR`: adjective, comparative
* `JJS`: adjective, superlative
* `LS`: list marker
* `MD`: modal verb
* `NN`: noun, singular or mass
* `NNS`: noun, plural
* `NNP`: proper noun, singular
* `NNPS`: proper noun, plural
* `PDT`: predeterminer
* `POS`: possessive ending
* `PRP`: pronoun, personal
* `PRP$`: pronoun, possessive
* `RB`: adverb
* `RBR`: adverb, comparative 
* `RBS`: adverb, superlative
* `RP`: particle
* `SYM`: symbol
* `TO`: to
* `UH`: interjection
* `VB`: verb, base form
* `VBD`: verb, past tense
* `VBG`: verb, gerund or present participle
* `VBN`: verb, past participle
* `VBP`: verb, non-3rd person singular present
* `VBZ`: verb, 3rd person singular present
* `WDT`: wh-determiner
* `WP`: wh-pronoun, personal
* `WP$`: wh-pronoun, possessive
* `WRB`: wh-adverb

Benchmark:

| Model | ALL | OOV | EXT |
|---|:-:|:-:|:-:|
| [Manning (2011)](http://nlp.stanford.edu/pubs/CICLing2011-manning-tagging.pdf) | 97.32 | 90.79 | O |
| [Shen et. al. (2014)](http://www.aclweb.org/anthology/P07-1096) | 97.33 | 89.61 |   |
| [Sun (2014)](http://papers.nips.cc/paper/5563-structure-regularization-for-structured-prediction.pdf) | 97.36 |   -   |   |
| [Moore (2015)](http://aclweb.org/anthology/D15-1151) | 97.36 | 91.09 | O |
| [Spoustová et al. (2009)](http://www.aclweb.org/anthology/E09-1087) | 97.44 | 89.92 | O |
| [Søgaard (2011)](http://www.aclweb.org/anthology/P11-2009) | 97.50 |   -   | O |
| [Tsuboi (2014)](http://aclweb.org/anthology/D14-1101) | 97.51 | 91.64 | O |
| [Choi (2016)](https://www.aclweb.org/anthology/N16-1031) | 97.64 | **92.03** | O |
| [Ling et al. (2015)](https://aclweb.org/anthology/D15-1176) | **97.78** |   -   | O |
