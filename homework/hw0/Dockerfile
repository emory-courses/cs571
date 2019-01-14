FROM python:3.5

RUN mkdir /app
COPY requirements.txt /app
WORKDIR /app
RUN pip install -r requirements.txt
COPY src /app

CMD ["python", "hw0.py"]