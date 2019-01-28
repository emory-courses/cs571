Hashtag Segmentation
=====

Hashtags found in social media do not include whitespaces, which make them hard to interpret. Your task is to write a program that takes a hashtag and returns a list of tokens representing the most likely sequence of the input hashtag (e.g., `'#helloworld'` &rarr; `['hello', 'world']`).


## Task 1

* Copy and paste the [`hw1/`](.) directory under the `cs571` project.
* Create a directory called `res/` under `hw1/`.
* Download [ngrams.zip](https://canvas.emory.edu/files/1997331/download?download_frd=1) and uncompress it under `res/`.  You should see 5 files, `[1-5]gram.txt`.
  * Each file is tab-delimited where the second column gives _n_-grams and the first column gives their raw counts.
  * Tokens in each _n_-gram are delimited by spaces.
* Update [`src/hw1.py`]:
  * Initialize the _n_-gram files in the constructor, `__init__`.
  * The `decode` function takes a hashtag (including `#` at the front) and returns a list of tokens that is the most likely sequence of the input hashtag.
  * The token list returned by the `decode` function must preserve the original casing.
  * You may need to implement dynamic programming to handle long sequences.


## Task 2

* Add 5 interesting hashtags to `res/hashtags.csv` that you want us to evaluate.


## Task 3

* Write a report, `res/hw1.pdf` describing your approach.


## Run

* Build a docker image:
  ```
  $ cd cs571/hw1
  $ docker build -t hw1 .
  ```
* Run the docker image:
  ```
  $ docker run -v /home/ubuntu/cs571/hw1:/mnt:rw -e "RESOURCE=/mnt/res/" hw1
  ```

## Submission

* Push all your changes to your private Github repository:
  * Make sure `src/hw1.py`, `res/hw1.pdf`, and `res/hashtags.py` are properly updated.
  * Do not add the _n_-gram files to git.
