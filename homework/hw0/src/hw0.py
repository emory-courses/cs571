import os
import socket
import numpy as np


def main(in_file, out_file):
    fin = open(in_file)
    x = np.array([float(n) for n in fin.readline().split()])
    y = np.array([float(n) for n in fin.readline().split()])
    z = x + y

    fout = open(out_file, 'w')
    s = [os.environ.get('HOME'), socket.gethostbyname(socket.gethostname()), in_file, out_file, str(z)]
    fout.write('\n'.join(s)+'\n')


if __name__ == '__main__':
    in_file = os.environ.get('IN_FILE')
    out_file = os.environ.get('OUT_FILE')
    main(in_file, out_file)