FROM nvidia/cuda:9.2-cudnn7-devel

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y \
	git \
	python3 \
	python3-dev \
	python3-setuptools \
	python3-pip && \
	pip3 install -U pip setuptools && \
   rm -rf /var/lib/apt/lists/*

RUN mkdir /app
WORKDIR /app
COPY requirements.txt /app
RUN pip3 install --upgrade pip setuptools
RUN pip3 install -r requirements.txt
RUN pip install pytest
COPY src /app/src

CMD ["python", "-m", "src.eval"]