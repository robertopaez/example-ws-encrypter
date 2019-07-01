
#Example Password Utility Plugin plus Workflow Step plugin

This is an example about how to use a Password Utility Plugin along with a workflow step in order to encrypt password using the GUI and decrypt them on a workflow step.

 
## Install 

* build: `gradle clean build`
* install: copy the file `build/libs/example-ws-encrypter-0.1.0.jar` to libext folder


## Use 

* go to Password Utility page

![example/image1.png](example/image1.png)


* Select the option MyPasswordEncrypter
![example/image2.png](example/image2.png)


* type a value and encrypt it. Copy the result
![example/image3.png](example/image3.png)

![example/image4.png](example/image4.png)


* go to the job example, pass the encrypted value and run the job


![example/image5.png](example/image5.png)
![example/image6.png](example/image6.png)
