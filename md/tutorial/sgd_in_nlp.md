# Stochastic Gradient Descent in NLP

This post gives in-depth analyses of [stochastic gradient descent](https://en.wikipedia.org/wiki/Stochastic_gradient_descent) in applications of natural language processing.  The goal is to understand the best practice of stochastic gradient descent, which is arguably the most commonly used optimization method for NLP tasks.

## Features



## Random Shuffle 

Most stochastic gradient descent methods are sensitive to the order of training instances such that the final model has a high chance of getting overfitted to the later training instances.  This is not an issue for algorithms such as [support vector machines](https://en.wikipedia.org/wiki/Support_vector_machine) that assumes the feature space is convex and finds the global optimum.  One quick way of handling this issue is to randomly shuffle training instances for every epoch so the model does not get overfitted to the same training instances.

