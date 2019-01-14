Getting Started
=====

## Git Repository

* Login to Github (create an account if you do not have one). 
* Create a new repository called `cs571` and make it private.
* From the `Settings` menu, add the TAs as collaborators of this repository.
  * Gary Lai: `imgarylai` 
* Clone the repository on your local machine.
* Create hw0 and copy and paste everyone in [hw0]().

## Cloud Computing

* Login to the [Amazon Web Services](https://aws.amazon.com) (create an account if you do not have one).
* Go to the [AWS Management Console](https://console.aws.amazon.com) and create an EC2 instance.
  * Machine Image: Ubuntu Server 18.04 LTS.
  * Instance Type: `t2.micro`.
  * You may need to create a key pair during this process. Save the key pair on your local machine and never upload it to the web. You will not be able to access the instance without this key pair so remember where you save it.
* Once the instance is launched, connect to the instance using the secure shell (`ssh`):

  ```
  ssh -i "your-key-pair.pem" ubuntu@ec2-3-xxx-xxx-xxx.compute-1.amazonaws.com
  ```
* Make sure you STOP the instance when it is idle; it will keep charging you as long as it is running.




## Docker



```
$ docker build -t latest . 
```

## Test on local machine

Create a file on your local machine. 

For example, I create a `test.txt` under directory `/home/glai2`.

```
$ ls /home/glai2
test.txt
``` 

To test your program please replace the `/home/glai2` path to the directory where you create a test file in the following command. 

```
$ docker run -v /home/glai2:/mnt:ro -e "TRN_FILE=/mnt/trn.txt" "TEST_FILE=/mnt/test.txt" -it --rm latest
Hello World from Test file
```