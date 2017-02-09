# ========================================================================
# Copyright 2017 Emory University
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
import numpy as np
import xxhash
__author__ = 'Jinho D. Choi'


class SparseModel:
    # max_x : int
    # max_y : int
    def __init__(self, max_x, max_y):
        self._max_x = max_x
        self._label_map = dict()
        self._label_list = list()
        self._weights = np.zeros(shape=(max_y, max_x+1), dtype=float)

    # feature : str
    def index_x(self, feature):
        return xxhash.xxh32(feature).intdigest() % self._max_x + 1

    # label : str
    def index_y(self, label):
        if label in self._label_map:
            return self._label_map[label]
        else:
            index = len(self._label_map)
            self._label_map[label] = index
            self._label_list.append(label)
            return index

    # index : int
    def label(self, index):
        return self._label_list[index]

    # x : list<int>
    def scores(self, x):
        return np.sum(self._weights[:, x], axis=1)

    # x : list<int>
    def argmax(self, x):
        return np.argmax(self.scores(x))

    # x : list<int>
    # y : int
    # gradient : float
    def update_weights(self, x, y, gradient):
        self._weights[y, x] += gradient
