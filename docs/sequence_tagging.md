# Sequence Tagging

## Reading

### 02/18

* [Conditional Random Fields: Probabilistic Models for Segmenting and Labeling Sequence Data](https://repository.upenn.edu/cgi/viewcontent.cgi?article=1162&context=cis_papers), Lafferty et al., ICML, 2001.*

### 02/20

* [Natural Language Processing (Almost) from Scratch](http://www.jmlr.org/papers/volume12/collobert11a/collobert11a.pdf), Collobert et al., JMLR, 2011.
* [Learning Character-level Representations for Part-of-Speech Tagging](http://proceedings.mlr.press/v32/santos14.pdf), Santos and Zadrozny, ICML, 2014.
* [Named Entity Recognition with Bidirectional LSTM-CNNs](https://www.aclweb.org/anthology/Q16-1026), Chiu & Nichols, TACL, 2016.*
* [Transfer Learning for Sequence Tagging with Hierarchical Recurrent Networks](https://arxiv.org/abs/1703.06345), Yang et al., ICLR, 2017.*

### 02/25

* [Neural Architectures for Named Entity Recognition](https://www.aclweb.org/anthology/N16-1030), Lample et al., NAACL, 2016.
* [End-to-end Sequence Labeling via Bi-directional LSTM-CNNs-CRF](http://www.aclweb.org/anthology/P16-1101), Ma & Hovy, ACL, 2016.
* [Semi-supervised Sequence Tagging with Bidirectional Language Models](http://www.aclweb.org/anthology/P17-1161), Peters et al., ACL, 2017.*
* [Contextual String Embeddings for Sequence Labeling](http://aclweb.org/anthology/C18-1139), Akbik et al., COLING, 2018.*


## Penn POS Tagset

### Words

| Tag | Description | Tag | Description |
|---|---|---|---|
| ADD | Email | POS | Possessive ending |
| AFX | Affix | PRP | Personal pronoun |
| CC | Coordinating conjunction | PRP$ | Possessive pronoun  |
| CD | Cardinal number | RB | Adverb |
| CODE | Code ID | RBR | Adverb, comparative |
| DT | Determiner | RBS | Adverb, superlative |
| EX | Existential there | RP | Particle |
| FW | Foreign word | TO | To |
| GW | Go with | UH | Interjection |
| IN | Preposition or subordinating conjunction | VB | Verb, base form |
| JJ | Adjective | VBD | Verb, past tense |
| JJR | Adjective, comparative | VBG | Verb, gerund or present participle |
| JJS | Adjective, superlative | VBN | Verb, past participle |
| LS | List item marker | VBP | Verb, non-3rd person singular present |
| MD | Modal | VBZ | Verb, 3rd person singular present |
| NN | Noun, singular or mass | WDT | *Wh*-determiner |
| NNS | Noun, plural | WP | *Wh*-pronoun |
| NNP | Proper noun, singular | WP$ | *Wh*-pronoun, possessive |
| NNPS | Proper noun, plural | WRB | *Wh*-adverb |
| PDT | Predeterminer | XX | Unknown |

### Symbols

| Tag | Description | Tag | Description |
|---|---|---|---|
| $ | Dollar | -LRB- | Left bracket |
| : | Colon | -RRB- | Right bracket |
| , | Comma | HYPH | Hyphen |
| . | Period | NFP | Superfluous punctuation |
| `` | Left quote | SYM | Symbol |
| '' | Right quote | PUNC | General punctuation |