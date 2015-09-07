## POSTagger

The following code shows the core of [`POSTagger`](../../java/edu/emory/mathcs/nlp/component/pos/POSTagger.java).

```java
public void process(N[] nodes)
{
	L2RState<N> state = new POSState<>(nodes);
	if (!isDecode()) state.clearGoldLabels();
	
	while (!state.isTerminate())
	{
		StringVector x = extractFeatures(state);
		String label = getLabel(state, x);
		state.setLabel(label);
		state.next();
		
		if (isTrain())
			model.addInstance(new StringInstance(label, x));
	}
	
	if (isEvaluate()) state.evaluateTokens((AccuracyEval)eval);
}
```

The `process` method takes an array of nodes whose type extends [`POSNode`](../../java/edu/emory/mathcs/nlp/component/pos/POSNode.java).

```java
public void process(N[] nodes)
```

It begins by creating the initial state using the left-to-right strategy: [`L2RState`](../../java/edu/emory/mathcs/nlp/component/state/NLPState.java).

```java
L2RState<N> state = new POSState<>(nodes);
```

It is important to clear out and save existing gold-standard labels before training or development; accidental usage of these labels can lead to inflated evaluation scores.

```java
if (!isDecode()) state.clearGoldLabels();
```

The method iterates through every state as defined in [`L2RState`](../../java/edu/emory/mathcs/nlp/component/state/NLPState.java).

```java
while (!state.isTerminate())
{
	...
	state.next();
	...		
}
```
For each state, it creates a feature vector.

```java
StringVector x = extractFeatures(state);
```

The `extractFeatures` method generates a feature vector containing 5 types of features:

* The word forms of w<sub>i</sub>, w<sub>i-1</sub>, w<sub>i+1</sub>.
* The part-of-speech tag of w<sub>i-1</sub>.
* The ambiguity class of w<sub>i</sub>.

```java
protected StringVector extractFeatures(L2RState<N> state)
{
	StringVector x = new StringVector();
	N node; int type = 0;
	
	node = state.getNode(0);
	if (node != null) x.add(type++, node.getWordForm());
	
	node = state.getNode(-1);
	if (node != null) x.add(type++, node.getWordForm());
	
	node = state.getNode(1);
	if (node != null) x.add(type++, node.getWordForm());
	
	node = state.getNode(-1);
	if (node != null) x.add(type++, node.getPOSTag());
	
	node = state.getNode(0);
	if (node != null) x.add(type++, ambiguity_class_map.get(node));
	
	return x;
}
```

Give the feature vector, it predicts the label of the current state.

```java
String label = getLabel(state, x);
```

During training, the label is taken from the gold-standard data.  During decoding, it is predicted by the statistical model.

```java
private String getLabel(L2RState<N> state, StringVector x)
{
	return isTrain() ? state.getGoldLabel() : model.predictBest(x).getLabel();
}
```

Finally, it assigns the label to the current state.

```java
state.setLabel(label);
```

During training, it saves the training instance to the model.

```java
if (isTrain()) model.addInstance(new StringInstance(label, x));
```

During evaluation, the accuracy count is updated to the evaluator.

```java
if (isEvaluate()) state.evaluateTokens((AccuracyEval)eval);
```

