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
from typing import Any
import os
from elit.component import Component

__author__ = "Gary Lai, Jinho D. Choi"


class HashtagSegmenter(Component):
    def __init__(self, ngram_dir: str):
        """
        :param ngram_dir: a path to the directory where ngram files are located.
        """
        # TODO
        pass

    def decode(self, input_text: str, **kwargs):
        """
        :param input_text: the input text contains one hashtag per line.
        :param kwargs:
        :return:
        """
        # TODO
        pass

    def load(self, model_path: str, **kwargs):
        pass

    def save(self, model_path: str, **kwargs):
        pass

    def train(self, trn_data: Any, dev_data: Any, model_path: str, **kwargs) -> float:
        pass

    def evaluate(self, data: Any, **kwargs):
        pass


if __name__ == '__main__':
    ngram_dir = os.environ.get('NGRAM_DIR')
    seg = HashtagSegmenter(ngram_dir)
    seg.decode("#helloworld")
