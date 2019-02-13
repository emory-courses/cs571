import os
from time import time

from src.hw2 import SentimentAnalyzer
from src.util import tsv_reader

if __name__ == '__main__':
    resource_dir = os.environ.get('RESOURCE')
    tst_data = tsv_reader('{}/sst.tst.tsv'.format(resource_dir))
    start = time()
    sentiment_analyzer = SentimentAnalyzer(resource_dir)
    sentiment_analyzer.load(os.path.join(resource_dir, 'hw2-model'))
    score = sentiment_analyzer.evaluate(tst_data)
    end = time()
    print(score, end - start)
