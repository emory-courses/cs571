import os

from elit.component import Component

from src.util import tsv_reader


class SentimentAnalysis(Component):

    def __init__(self, resource_dir: str):
        """
        :param resource_dir: a path to the directory where resource files are located.
        """
        # TODO

    def decode(self, data, **kwargs) :
        """

        :param data:
        :param kwargs:
        :return:
        """
        # TODO

    def evaluate(self, data, **kwargs):
        """

        :param data:
        :param kwargs:
        :return:
        """
        # TODO

    def load(self, model_path: str, **kwargs):
        """

        :param model_path:
        :param kwargs:
        :return:
        """
        # TODO

    def save(self, model_path: str, **kwargs):
        """

        :param model_path:
        :param kwargs:
        :return:
        """
        # TODO

    def train(self, trn_data, dev_data, *args, **kwargs):
        """

        :param trn_data:
        :param dev_data:
        :param args:
        :param kwargs:
        :return:
        """
        # TODO


if __name__ == '__main__':
    resource_dir = os.environ.get('RESOURCE')
    sentiment_analyzer = SentimentAnalysis(resource_dir)
    trn_data = tsv_reader('{}/sst.trn.tsv'.format(resource_dir))
    dev_data = tsv_reader('{}/sst.dev.tsv'.format(resource_dir))
    tst_data = tsv_reader('{}/sst.tst.tsv'.format(resource_dir))
    sentiment_analyzer.train(trn_data, dev_data)
    sentiment_analyzer.decode(tst_data)