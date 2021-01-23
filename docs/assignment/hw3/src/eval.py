import os
from time import time

from src.hw3 import NamedEntityRecognizer
from src.util import tsv_reader

if __name__ == '__main__':
    resource_dir = os.environ.get('RESOURCE')
    tst_data = tsv_reader(resource_dir, 'conll03.eng.tst.tsv')
    start = time()
    named_entity_recognizer = NamedEntityRecognizer(resource_dir)
    named_entity_recognizer.load(os.path.join(resource_dir, 'hw3-model'))
    score = named_entity_recognizer.evaluate(tst_data)
    end = time()
    print(score, end - start)
