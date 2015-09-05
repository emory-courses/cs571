## POSTagger

See [`POSTagger`]() for the full source code.

```
public void process(N[] nodes)
{
	L2RState<N> state = new POSState<>(nodes);
	if (!isDecode()) state.clearGold();
	
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

The `process` method takes an array of nodes whose type extends [`POSNode`]().

```
public void process(N[] nodes)
```

It begins by creating the initial state using the left-to-right strategy ([`L2RState`](../../java/edu/emory/mathcs/nlp/component/state/NLPState.java)).

```
L2RState<N> state = new POSState<>(nodes);
```
It is important to clear out and save existing gold-standard tags before training or development because accidental usage of these tags can lead to inflated evaluation scores.

```
if (!isDecode()) state.clearGold();
```
The method iterates through every state as defined in 

```
while (!state.isTerminate())
{
	...
	state.next();
	...		
}
```