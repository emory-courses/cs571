# Hashtag Segmentation

Your task is to develop an n-gram model that segments [hashtags](https://en.wikipedia.org/wiki/Hashtag) into token lists.  For example, given the input hashtag `'#photooftheday'`, your model is expected to generate the list of tokens `['photo', 'of', 'the', 'day']`.

## Task 1

* Download [hw1_id.py](hw1_id.py) and change the filename by replacing `id` with your Emory ID (e.g., `hw1_jchoi31.py`).
* Download the [ngrams.zip](https://canvas.emory.edu/files/1997331/download?download_frd=1) and uncompress it.
  * You should see `[1-5]gram.txt`. Place them under the same directory as `hw1_id.py`.
  * Do not change the location or the names of these files. Use the global variable `FILE_[1-5]GRAM` in `hw1_id.py` to access these files.
  * Do not update any content in these files; your model will be evaluated using the original n-gram files, not your custom files.

* Update the `segment` function in `hw1_id.py` so that it takes a list of hashtags and returns a list of token lists, where each token list corresponds to the same index'th hashtag.
  * Assume that each hashtag starts with the initial `#` (e.g., `#HelloWorld`).
  * The token list should not include the initial `#` (e.g., `['Hello', 'World']`).
  * Casing must be preserved for all tokens in the token list such that the concatenation of all tokens must be equal to the input `hashtag[1:]`.
* 
Submit a model.


## Resources

* [N-gram data](https://www.ngrams.info).
* http://aircconline.com/ijnlc/V5N4/5416ijnlc02.pdf


