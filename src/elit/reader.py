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
from elit.structure import NLPGraph
from elit.structure import NLPNode
import re
__author__ = 'Jinho D. Choi'

RE_TAB = re.compile('\t')


class TSVReader:
    # tsv_to : list<list<string> -> NLPGraph
    # fin : file input-stream
    def __init__(self, tsv_to, fin=None):
        self.tsv_to = tsv_to
        self.fin = fin

    def __next__(self):
        graph = self.next()
        if graph:
            return graph
        else:
            raise StopIteration

    def __iter__(self):
        return self

    # fin : file input-stream
    def open(self, fin):
        self.fin = fin

    def close(self):
        self.fin.close()

    def next(self):
        tokens = list()

        for line in self.fin:
            line = line.strip()
            if line:
                tokens.append(RE_TAB.split(line))
            elif tokens:
                break

        if tokens:
            return self.tsv_to(tokens=tokens)
        else:
            return None

    def next_all(self):
        return [graph for graph in self]


# tokens : list<list<string>>
def tsv_to_pos_graph(tokens, word_index=0, pos_index=1):
    return NLPGraph([NLPNode(t[word_index], t[pos_index]) for t in tokens])
