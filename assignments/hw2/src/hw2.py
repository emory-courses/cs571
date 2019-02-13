# ========================================================================
# Copyright 2019 ELIT
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ========================================================================
import csv
import os
import glob
from typing import List, Any

from elit.component import Component

__author__ = "Gary Lai, Jinho D. Choi"


class SentimentAnalyzer(Component):

    def __init__(self, resource_dir: str):
        """
        :param resource_dir: a path to the directory where resource files are located.
        """
        # initialize the n-grams
        ngram_filenames = glob.glob(os.path.join(resource_dir, '[1-6]gram.txt'))
        # TODO: initialize resources
        pass

    def decode(self, hashtag: str, **kwargs) -> List[str]:
        """
        :param hashtag: the input hashtag starting with `#` (e.g., '#helloworld').
        :param kwargs:
        :return: the list of tokens segmented from the hashtag (e.g., ['hello', 'world']).
        """
        # TODO: update the following code.
        return [hashtag[1:]]

    def evaluate(self, data: Any, **kwargs):
        pass  # NO NEED TO UPDATE

    def load(self, model_path: str, **kwargs):
        pass  # NO NEED TO UPDATE

    def save(self, model_path: str, **kwargs):
        pass  # NO NEED TO UPDATE

    def train(self, trn_data, dev_data, *args, **kwargs):
        pass  # NO NEED TO UPDATE


if __name__ == '__main__':
    resource_dir = os.environ.get('RESOURCE')
    segmenter = HashtagSegmenter(resource_dir)
    total = correct = 0

    with open(os.path.join(resource_dir, 'hashtags.csv')) as fin:
        reader = csv.reader(fin)
        for row in reader:
            hashtag = row[0]
            gold = row[1]
            auto = ' '.join(segmenter.decode("#helloworld"))
            print('%s -> %s | %s' % (hashtag, auto, gold))
            if gold == auto: correct += 1
            total += 1

    print('%5.2f (%d/%d)' % (100.0*correct/total, correct, total))
