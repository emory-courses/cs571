Sentiment Analysis
=====

Your task is to develop a deep learning model that takes each sentence from movie reviews and classifies it into one of the 5 sentiments: 0 - very negative, 1 - negative, 2 - neutral, 3 - positive, 4 - very positive.


## Task 1

* Copy and paste the [`hw2/`](.) directory under the `cs571` project to your own `cs571` repo.
* You should see the following 3 files under [`hw2/res/`](res):
  * `sst.trn.gold.tsv`: training set.
  * `sst.dev.gold.tsv`: development set.
  * `sst.tst.unlabeled.tsv`: evaluation set (unlabeled).
    ```
    line ::= <sentiment><tab><document>
    sentiment ::= 0|1|2|3|4
    document ::= <token>(<space><token>)*
    ```
* Download a pre-trained FastText model and put it under [`hw2/res/`](res):
  * [`fasttext-50-180614.bin`](https://s3.amazonaws.com/elit-public/resources/embedding/fasttext-50-180614.bin): 50 dimensional word embeddings.
  * [`fasttext-100-180614.bin`](https://s3.amazonaws.com/elit-public/resources/embedding/fasttext-100-180614.bin): 100 dimensional word embeddings.
  * [`fasttext-200-180614.bin`](https://s3.amazonaws.com/elit-public/resources/embedding/fasttext-200-180614.bin): 200 dimensional word embeddings.
* Update the following functions in [`src/hw2.py`](src/hw2.py):
  * `__init__`: initialize the FastText model as well as your neural network model.
  * `load`: loads a pre-trained model saved by the `save` function.
  * `save`: saves a pre-trained model trained by the `train` function.
  * `train`: trains a model using training and development sets.
  * `decode`: predicts sentiment labels for the input data.
* Your minimum required approach is the Convolutional Neural Network model introduced by [Kim 2014](https://www.aclweb.org/anthology/D14-1181).


## Task 2

* Write a report, `res/hw2.pdf`, describing your approach.


## Submission

* Push all your changes to Github and make sure the following files are properly updated:
  * `src/hw2.py`
  * `res/hw2.pdf`
  * `res/hw2-model` (your final model; should be able to be loaded by the `load` function)
* Do not add the FastText model to git.
* Submission: https://canvas.emory.edu/courses/54027/assignments/209131


## Development

### Important

Here are things you should notice before you start.

* Each homework is structured as a python package. If you are using IDE, such as PyCharm, please open a new project and use [hw2](.) as your project **ROOT** directory; otherwise, the package import path will be incorrect. 
* Develop everything based on the provided class. **DO NOT** delete existing methods, modify inheritance, or remove the given parameters. You can add new functions either outside or inside the class. Make sure everything can be run in the container successfully. 
* Please list all your dependencies in [requirements.txt](requirements.txt). A fixed library version is required ([ref](https://pip.readthedocs.io/en/1.1/requirements.html)).

### GPU usage
 
You might want to use a GPU machine for this homework (using AWS is recommended).
We have created an EC2 AMI with all the prerequisites for this homework.
Please change your AWS region to Oregon(us-west-2) and launch an EC2 `p2.xlarge` instance with AMI id:  `ami-04cd8c7e3716b3284`.
It is okay to launch instance of your own.
Make sure you instance can run [nvidia-docker](https://github.com/NVIDIA/nvidia-docker) without `sudo` permission.

### CUDA version for deep learning framework

Here let's assume we want to use `tenforflow==1.12` as our deep learning framework. Go to the [hardware configuration page](https://www.tensorflow.org/install/gpu) to check which GPU drivers are needed. It requires `CUDA==9.0` and `cuDNN>=7.2`.    

Next we need to change the source of Docker. List of `CUDA` and `cuDNN` dockers can be founded on [Nvidia Docker Hub](https://hub.docker.com/r/nvidia/cuda/). We choose Ubuntu 16.04 for Operating System and find 
docker with `CUDA==9.0` and `cuDNN>=7.2` in this section. 

- 9.0-cudnn7-devel, 9.0-cudnn7-devel-ubuntu16.04 (9.0/devel/cudnn7/Dockerfile)

And replace the first line in the [`Dockerfile`](Dockerfile) from

```diff
- FROM nvidia/cuda:9.2-cudnn7-devel
+ FROM nvidia/cuda:9.0-cudnn7-devel
```  


## Run

* Build a docker image:
  ```
  $ cd cs571/hw2
  $ docker build -t hw2 .
  ```
* Run the docker image:
  > Remember to replace `/home/ubuntu/cs571/hw2` to the path where you locate `res` directory on your machine.
  ```
  $ docker run -v /home/ubuntu/cs571/hw2:/mnt:rw -e "RESOURCE=/mnt/res/" hw2
  ```
