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
import csv


def tsv_reader(resource_dir: str, filename: str) -> List[Tuple[List[str], List[str]]]:
    """
    :param resource_dir:
    :param filename:
    :return: 
    """
    with open(os.path.join(resource_dir, filename)) as fin:
        labels, tokens = [], []
        dat = []

        for line in fin:
            l = line.split()
            if l:
                labels.append(l[-1])
                tokens.append(l[0])
            elif tokens:
                dat.append((labels, tokens))
                labels, tokens = [], []

        return dat
