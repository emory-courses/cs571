import sys
import random

DELIM = ' '

def read_ngrams(filename):
    fin = open(filename)
    ngrams = dict()
    totals = dict()

    # count
    for line in fin:
        l = line.split()
        count = int(l[0])
        given = DELIM.join(l[1:-1])
        word  = l[-1]

        if given in ngrams:
            d = ngrams[given]
        else:
            d = dict()
            ngrams[given] = d

        d[word] = d.get(word, 0) + count
        totals[given] = totals.get(given, 0) + count

    # normalize
    for given, d in ngrams.items():
        div = 1.0 / totals[given]
        for word in d:
            d[word] = div * float(d[word])

    return ngrams

def concat(sequence, word, idx):
    if idx >= 0: return word
    l = sequence[idx:]
    l.append(word)
    return DELIM.join(l)

def generate(ngrams, init_words, threshold):
    sequence = list(init_words)
    idx = -len(init_words)

    for i in range(20):
        prev = DELIM.join(sequence[idx:])
        d = ngrams[prev]
        l = [word for word, prob in d.items() if prob >= threshold and concat(sequence, word, idx+1) in ngrams]
        if not l: break
        sequence.append(random.choice(l))

    return sequence

ngram_file = sys.argv[1]
threshold = float(sys.argv[2])
init_words = sys.argv[3].split('_')

ngrams = read_ngrams(ngram_file)

for i in range(50):
    seq = generate(ngrams, init_words, threshold)
    print(' '.join(seq))

# python hw0.py w2_.txt 0.01 dear
# python hw0.py w3_.txt 0.01 dear_to
# python hw0.py w4_.txt 0.01 dear_friend_of