from collections import Counter
import math
import sys

def cosine(d1, d2):
    num = den1 = den2 = 0.0

    for k, v1 in d1.items():
        den1 += v1**2
        if k in d2: num += v1 * d2[k]

    for v2 in d2.values():
        den2 += v2**2

    return float(num) / (math.sqrt(den1) * math.sqrt(den2));

INPUT_FILE = sys.argv[1]
fin = open(INPUT_FILE)

# bag-of-words
vectors_bow = list()
dfs = Counter()

for line in fin:
    t = line.split('\t')
    label = t[0]
    document = t[1]
    vector = (label, Counter(document.split()))
    vectors_bow.append(vector)
    dfs.update(vector[1].keys())

# document frequency
D = float(len(vectors_bow))

for token in dfs:
    dfs[token] = 1.0 / math.log(D/dfs[token])

# tf-idf
vectors_tfidf = list()

for (label, bow) in vectors_bow:
    tfidf = {token : dfs[token]*count for (token, count) in bow.items()}
    vectors_tfidf.append((label, tfidf))

# cosine similarity between all pairs
for i,v1 in enumerate(vectors_tfidf):
    for v2 in vectors_tfidf:
        cosine(v1[1], v2[1])
