Sentiment Analysis
=====

TODO

## Development

**Important** Here are things you should notice before start your hw2.

1. Each homework is structured as a python package. If you are using IDE, such as PyCharm, please open a new project and use [hw2](.) as your project **ROOT** directory; otherwise, the package import path will be incorrect. 
2. Please develop everything based on the given class. **Do not** delete existing methods, modify inheritance, remove the given parameters. You can add new functions no matter outside or inside the class. You just need to make sure everything can be run in the container successfully. 
3. Please list all your dependencies in [requirements.txt](requirements.txt). A fixed library version is required. [Ref](https://pip.readthedocs.io/en/1.1/requirements.html)

### GPU usage
 
You might want to use GPU for computing in this homework. Using AWS is recommended. I have created an ec2 AMI with all the prerequisites for this homework. Please launch an ec2 `p2.xlarge` instance with AMI id:  `ami-04cd8c7e3716b3284`. It's okay to launch instance in your own. Just make sure you instance can run [nvidia-docker](https://github.com/NVIDIA/nvidia-docker) without `sudo` permission.

