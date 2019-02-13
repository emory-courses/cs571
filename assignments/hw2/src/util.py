import csv


def tsv_reader(filename):
    """

    :param filename:
    :return: List[Tuple]
    """
    with open(filename) as fin:
        return [tuple((int(row[0]), row[1])) for row in csv.reader(fin, delimiter='\t')]