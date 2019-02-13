import os
from time import time

from src.hw2 import SentimentAnalysis
from src.util import tsv_reader

if __name__ == '__main__':
    resource_dir = os.environ.get('RESOURCE')
    tst_data = tsv_reader('{}/sst.tst.tsv'.format(resource_dir))
    start = time()
    sentiment_analyzer = SentimentAnalysis(resource_dir)
    score = sentiment_analyzer.evaluate(tst_data)
    end = time()
    print(score, end - start)