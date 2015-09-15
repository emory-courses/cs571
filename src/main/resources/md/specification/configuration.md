# Configuration

## Training

The following describes the format of the [configuration files](../../configuration/) used for training.

| Element | Description |
| :-----: | :---------- |
| `<language>` | Specifies the [language](https://github.com/emorynlp/common/blob/master/src/main/java/edu/emory/mathcs/nlp/common/util/Language.java) of the input data. |
| `<tsv>` | Specifies the [Tab-Separated-Values](https://en.wikipedia.org/wiki/Tab-separated_values) format used in the input data. |
| `<column>` | Specifies the field information.<ul><li>`index` specifies the index of the field, starting at 1.</li><li>`field` specifies the name of the field.</li>&#9702; `id`: node ID.<br>&#9702; `form`: word form.<br>&#9702; `lemma`: lemma.<br>&#9702; `pos`: part-of-speech tag.<br>&#9702; `feats`: pre-defined features.<br>&#9702; `headID`: head node ID.<br>&#9702; `deprel`: dependency label.<br>&#9702; `nament`: named entity tag.<br>&#9702; `sheads`: semantic heads.</ul> |
| `<trainer>` | Specifies the training algorithm and its parameters.<ul><li>`algorithm`: `adagrad` for AdaGrad, `liblinear`for Liblinear.</li><li>`type`: `svm` for hinge loss classification, `lr`for logistic regression.</li><li>`labelCutoff`: count threshold for labels appearing less than `N` times.</li><li>`featureCutoff`: count threshold for features appearing less than `N` times.</li><li>`average`: if `true`, apply averaging to online learning.</li><li>`alpha`: learning rate (AdaGrad).</li><li>`rho`: ridge to keep the inverse covariance well-conditioned (AdaGrad).</li></ul>| 
| `<bootstraps>` | If `true`, use bootstrap iterations for training sequences. | 

