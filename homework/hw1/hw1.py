from collections import Counter
import random
import scipy
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

def init_centroids(k, vectors):
    return map(lambda v: v[1], random.sample(vectors, k))

def init_centroids_pp(k, vectors):
    def distance(vector, centroids):
        return 1 - max([cosine(vector, centroid) for centroid in centroids])    

    centroids = [random.choice(vectors)[1]]

    for i in range(k-1):
        distances = scipy.array([distance(vector[1], centroids)**2 for vector in vectors])
        probs = distances / distances.sum()
        cum_probs = probs.cumsum()
        r = scipy.rand()
        idx = next(j for j,p in enumerate(cum_probs) if r < p)
        centroids.append(vectors[idx][1])

    return centroids

def find_centroid(cluster):
    centroid = sum(map(lambda t: t[1], cluster), Counter())
    n = 1.0 / len(cluster)
    for (k,v) in centroid.items(): centroid[k] = n * v
    return centroid

def purity_score(cluster):
    c = Counter(map(lambda t: t[0], cluster))
    return max(c.items(), key=lambda x: c[x])[1]

def kmeans_clustering(k, threshold, vectors, init):
    # initialize centroids
    centroids = init(k, vectors)

    for iter in xrange(50):
        # create empty clusters
        clusters = [[] for _ in xrange(k)]

        # maximization
        for vector in vectors:
            m = max([(cosine(vector[1], centroid), i) for (i, centroid) in enumerate(centroids)])
            clusters[m[1]].append(vector)

        # expectation
        new_centroids = [find_centroid(cluster) for cluster in clusters]
        m = min([cosine(centroids[i], new_centroids[i]) for i in xrange(k)])
        p = 100.0 * sum(map(lambda cluster: purity_score(cluster), clusters)) / len(vectors)
        print('%3d: purity = %5.2f, min = %6.4f' % (iter, p, m))
        if m > threshold: break
        centroids = new_centroids

    return clusters

# main
INPUT_FILE = sys.argv[1]
K = int(sys.argv[2])
THRESHOLD = float(sys.argv[3])
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
    tfidf = Counter({token : dfs[token]*count for (token, count) in bow.items()})
    vectors_tfidf.append((label, tfidf))

# k-means clustering
# print('K-means: bag-of-words')
# kmeans_clustering(K, THRESHOLD, vectors_bow, init_centroids)
# print('K-means: tf-idf')
# kmeans_clustering(K, THRESHOLD, vectors_tfidf, init_centroids)
# print('K-means++: bag-of-words')
# kmeans_clustering(K, THRESHOLD, vectors_bow, init_centroids_pp)
print('K-means++: tf-idf')
kmeans_clustering(K, THRESHOLD, vectors_tfidf, init_centroids_pp)

# python hw1.py docs.trn.tsv 7 0.99