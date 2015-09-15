# Training Configuration

## General

The following describes the format of the [configuration files](../../configuration/) used for training.

| Element | Description |
| :-----: | :---------- |
| `<language>` | Specifies the [language](https://github.com/emorynlp/common/blob/master/src/main/java/edu/emory/mathcs/nlp/common/util/Language.java) of the input data. |
| `<tsv>` | Specifies the [Tab-Separated-Values](https://en.wikipedia.org/wiki/Tab-separated_values) format used in the input data. |
| `<column>` | Specifies the columns in TSV.<ul><li>`index` specifies the index of the field, starting at `0`.</li><li>`field` specifies the name of the field.</li>&#9702; `id`: node ID.<br>&#9702; `form`: word form.<br>&#9702; `lemma`: lemma.<br>&#9702; `pos`: part-of-speech tag.<br>&#9702; `feats`: pre-defined features.<br>&#9702; `headID`: head node ID.<br>&#9702; `deprel`: dependency label.<br>&#9702; `nament`: named entity tag.<br>&#9702; `sheads`: semantic heads.</ul> |
| `<optimizer>` | Specifies the optimizer and its parameters for training.<ul><li>`algorithm`: see [below](#algorithms) for the examples.</li><li>`label_cutoff`: discard labels appearing less than this cutoff.</li><li>`feature_cutoff`: discard features appearing less than this cutoff.</li><li>`reset_weights`: if `true`, reset the weight vector to `0` before self-training.</li><li>`average`: if `true`, return the averaged weight vector (for online learning).</li><li>`learning_rate`: the learning rate.</li><li>`bias`: the bias weight.</li><li>`batch_ratio`: the portion of each mini-batch (e.g., use every 10% of the training data as a mini-batch).</li></ul>| 
| `<self_training>` | If set, use self-training for sequence classification.<ul><li>`tolerance`: tolerance of termination criterion.</li></ul> | 

## Algorithms

### Perceptron

```
<optimizer>
    <algorithm>perceptron</algorithm>
    <label_cutoff>4</label_cutoff>
    <feature_cutoff>3</feature_cutoff>
    <reset_weights>false</reset_weights>
    <average>false</average>
    <learning_rate>0.01</learning_rate>
    <bias>0</bias>
</optimizer>
```

### AdaGrad

```
<optimizer>
    <algorithm>adagrad</algorithm>
    <label_cutoff>4</label_cutoff>
    <feature_cutoff>3</feature_cutoff>
    <reset_weights>false</reset_weights>
    <average>false</average>
    <learning_rate>0.01</learning_rate>
    <bias>0</bias>
</optimizer>
```

### AdaGrad with Mini-Batch

```
<optimizer>
    <algorithm>adagrad-mini-batch</algorithm>
    <label_cutoff>4</label_cutoff>
    <feature_cutoff>3</feature_cutoff>
    <reset_weights>false</reset_weights>
    <average>false</average>
    <batch_ratio>0.1</batch_ratio>
    <learning_rate>0.01</learning_rate>
    <bias>0</bias>
</optimizer>
```

*  `batch_ratio = 0.1`: use every 10% of the training data as a mini-batch.

### AdaGrad

```
<optimizer>
    <algorithm>perceptron</algorithm>
    <label_cutoff>4</label_cutoff>
    <feature_cutoff>3</feature_cutoff>
    <reset_weights>false</reset_weights>
    <average>false</average>
    <batch_ratio>0.1</batch_ratio>
    <learning_rate>0.01</learning_rate>
    <decaying_rate>0.4</decaying_rate>
    <bias>0</bias>
</optimizer>
```

### AdaGrad

```
<optimizer>
    <algorithm>perceptron</algorithm>
    <label_cutoff>4</label_cutoff>
    <feature_cutoff>3</feature_cutoff>
    <reset_weights>false</reset_weights>
    <average>false</average>
    <batch_ratio>0.1</batch_ratio>
    <learning_rate>0.01</learning_rate>
    <decaying_rate>0.4</decaying_rate>
    <bias>0</bias>
</optimizer>
```


