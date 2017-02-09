import sys
import random
from functools import partial
from itertools import islice
from elit.reader import *
from elit.model import *


# filename : str
def read_graphs(filename):
    return TSVReader(tsv_to_pos_graph, open(filename)).next_all()


# model : SparseModel
# graphs : list<NLPGraph>
def create_instances(model, graphs):
    xs = list()
    ys = list()
    correct = 0
    total = 0

    for graph in graphs:
        backup = [node.pos for node in graph]
        add_instances(model, graph, xs, ys)

        for pos, node in zip(backup, graph):
            if node.pos == pos:
                correct += 1
            else:
                node.pos = pos

        total += len(graph)

    return xs, ys, correct, total


# model : SparseModel
# sentence : list of fields
def add_instances(model, graph, xs, ys):
    for i in range(len(graph)):
        x = [0]

        f = 0
        node = graph.nodes[i]
        x.append(model.index_x(str(f) + node.word))

        f += 1
        if i >= 2:
            node = graph.nodes[i-2]
            x.append(model.index_x(str(f) + node.word))

        f += 1
        if i >= 1:
            node = graph.nodes[i-1]
            x.append(model.index_x(str(f) + node.word))

        f += 1
        if i + 1 < len(graph):
            node = graph.nodes[i+1]
            x.append(model.index_x(str(f) + node.word))

        f += 1
        if i + 2 < len(graph):
            node = graph.nodes[i+2]
            x.append(model.index_x(str(f) + node.word))

        '''
        f += 1
        if i >= 2:
            node = graph.nodes[i - 2]
            x.append(model.index_x(str(f) + node.pos))

        f += 1
        if i >= 1:
            node = graph.nodes[i - 1]
            x.append(model.index_x(str(f) + node.pos))
        '''

        node = graph.nodes[i]
        x.sort()
        xs.append(x)
        ys.append(model.index_y(node.pos))
        node.pos = model.label(model.argmax(x))


def perceptron(model, x, y, learning_rate):
    z = model.argmax(x)
    if y != z:
        model.update_weights(x, y,  learning_rate)
        model.update_weights(x, z, -learning_rate)


def update(model, xs, ys, algorithm):
    for x, y in zip(xs, ys):
        algorithm(model=model, x=x, y=y)


def main():
    trn_file = sys.argv[1]
    dev_file = sys.argv[2]
    learning_rate = 0.01
    max_x = 500000
    max_y = 50
    max_iter = 20
    mini_batch = 5

    print('Reading: '+trn_file)
    trn_graphs = read_graphs(trn_file)

    print('Reading: '+dev_file)
    dev_graphs = read_graphs(dev_file)

    print('Training:')
    model = SparseModel(max_x, max_y)

    for i in range(max_iter):
        random.shuffle(trn_graphs)

        for b_index in range(0, len(trn_graphs), mini_batch):
            e_index = b_index + mini_batch if b_index + mini_batch <= len(trn_graphs) else len(trn_graphs)
            xs, ys, correct, total = create_instances(model, islice(trn_graphs, b_index, e_index))
            update(model, xs, ys, partial(perceptron, learning_rate=learning_rate))

        xs, ys, correct, total = create_instances(model, dev_graphs)
        print('%3d: %5.2f (%d/%d)' % (i, 100.0*correct/total, correct, total))


main()