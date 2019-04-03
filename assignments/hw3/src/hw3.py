# ========================================================================
# Copyright 2019 Emory University
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
import os
from typing import List, Tuple

from elit.component import Component
from elit.embedding import FastText

from src.util import tsv_reader


class NamedEntityRecognizer(Component):
    def __init__(self, resource_dir: str, embedding_file='fasttext-50-180614.bin'):
        """
        Initializes all resources and the model.
        :param resource_dir: a path to the directory where resource files are located.
        """
        self.vsm = FastText(os.path.join(resource_dir, embedding_file))
        # TODO: to be filled.

    def load(self, model_path: str, **kwargs):
        """
        Load the pre-trained model.
        :param model_path:
        :param kwargs:
        """
        # TODO: to be filled
        pass

    def save(self, model_path: str, **kwargs):
        """
        Saves the current model to the path.
        :param model_path:
        :param kwargs:
        """
        # TODO: to be filled
        pass

    def train(self, trn_data: List[Tuple[List[str], List[str]]], dev_data: List[Tuple[List[str], List[str]]], *args, **kwargs):
        """
        Trains the model.
        :param trn_data: the training data.
        :param dev_data: the development data.
        :param args:
        :param kwargs:
        :return:
        """
        trn_ys, trn_xs = zip(*[(y, self.vsm.emb_list(x)) for y, x in trn_data])
        dev_ys, dev_xs = zip(*[(y, self.vsm.emb_list(x)) for y, x in dev_data])
        # TODO: to be filled
        pass

    def decode(self, data: List[Tuple[List[str], List[str]]], **kwargs) -> List[int]:
        """
        :param data:
        :param kwargs:
        :return: the list of predicted labels.
        """
        xs = [self.vsm.emb_list(x) for _, x in data]
        # TODO: to be filled

    def evaluate(self, data: List[Tuple[List[str], List[str]]], **kwargs) -> float:
        """
        :param data:
        :param kwargs:
        :return: the accuracy of this model.
        """
        # TODO: will be updated
        return 0


if __name__ == '__main__':
    resource_dir = os.environ.get('RESOURCE')
    sentiment_analyzer = NamedEntityRecognizer(resource_dir)
    trn_data = tsv_reader(resource_dir, 'conll03.eng.trn.tsv')
    dev_data = tsv_reader(resource_dir, 'conll03.eng.dev.tsv')
    tst_data = tsv_reader(resource_dir, 'conll03.eng.tst.tsv')
    sentiment_analyzer.train(trn_data, dev_data)
    sentiment_analyzer.evaluate(tst_data)
    sentiment_analyzer.save(os.path.join(resource_dir, 'hw3-model'))
