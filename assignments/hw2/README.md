Sentiment Analysis
=====

Your task is to develop a deep learning model that takes each sentence from movie reviews and classifies it into one of the 5 sentiments: 0 - very negative, 1 - negative, 2 - neutral, 3 - positive, 4 - very positive.


## Task 1

* Copy and paste the [`hw2/`](.) directory under the `cs571` project to your own `cs571` repo.
* You should see the following 3 files under [`hw2/res/`](res).
  * `sst.trn.gold.tsv`: training set.
  * `sst.dev.gold.tsv`: development set.
  * `sst.tst.unlabeled.tsv`: evaluation set (unlabeled).
* Here is the format of each file:
  ```
  line ::= <sentiment><tab><document>
  sentiment ::= 0|1|2|3|4
  document ::= <token>(<space><token>)*
  ```
* Update [`src/hw2.py`]:
  * Initialize the _n_-gram files in the constructor, `__init__`.
  * The `decode` function takes a hashtag (including `#` at the front) and returns a list of tokens that is the most likely sequence of the input hashtag.
  * The token list returned by the `decode` function must preserve the original casing.
  * You may need to implement dynamic programming to handle long sequences.








## Task 2

* Write a report, `res/hw2.pdf`, describing your approach.

