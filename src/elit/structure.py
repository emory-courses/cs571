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
__author__ = 'Jinho D. Choi'

ROOT_TAG = '@#r$%'


class NLPNode:
    # word : str
    # pos : str
    def __init__(self, word=None, pos=None):
        self.word = word
        self.pos = pos

    def __str__(self):
        return '\t'.join([self.word, self.pos])


class NLPGraph:
    # nodes : list<NLPNode>
    def __init__(self, nodes=[]):
        self.root = create_root()
        self.nodes = nodes

    def __next__(self):
        if self._idx >= len(self.nodes):
            raise StopIteration

        node = self.nodes[self._idx]
        self._idx += 1
        return node

    def __iter__(self):
        self._idx = 0
        return self

    def __str__(self):
        return '\n'.join(map(str, self.nodes))

    def __len__(self):
        return len(self.nodes)


# returns an artificial root node : NLPNode
def create_root():
    return NLPNode(ROOT_TAG, ROOT_TAG)
