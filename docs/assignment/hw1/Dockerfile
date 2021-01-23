FROM python:3.6.8

RUN mkdir /app
WORKDIR /app
COPY requirements.txt /app
RUN pip install --upgrade pip
RUN pip install -r requirements.txt
RUN pip install pytest
COPY src /app/src

CMD ["python", "-m", "pytest"]