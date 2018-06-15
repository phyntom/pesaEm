# pesaEM Demo Application

This application is written in Java and it is based on 

- JSF (Java Server Faces) technology.
- Bean Validation
- CDI (Context Dependency Inject)
- Primefaces
- Wildfly Swarm ()

For the runtime it uses Wildfly Swarm as container/packaging.

The data model of this application consists of two entities: Customer and Note.

It can be built using Maven.

# Requirement to run demo

1. JDK 8
2. Maven 3.3.x
3. IDE (Intellij/Eclipse)
4. Git
**Another important requirement is to change the upload_folder value located in config.propeties in (src/main/resources)**
Example :
	__upload_folder=/home/awesome/_
	
The value /home/awesome/ will represent the location where the csv file will be uploaded. Pay attention to this detail in order to make 
this application run smoothly

# Starting and runnin the demo on local machine

Go inside the project folder and follow the instruction
## First Approach
```
 mvn wildfly-swarm:run 
 
 ```

Open browser and access http://127.0.0.1:8085/
You will be required to provide _**username and password**_:
-  username : admin
-  password : admin123

In addition the customer data is exposed in a JAX-RS resource which is accessible via http://127.0.0.1:8085/index.xhtml.

## Second Approach

```
 mvn package && java -jar target/demo-swarm.jar
```

# Running the demo online
Go in your prefered web browser and put this link 

http://104.222.96.147:8085/

Provide **username and password** with following credentials
- username: admin
- password: admin123


# Focus

The main focus of this application is to demonstrate a simple project setup and usage of Java EE standards and respond to the following requirement:
	-	Create an application that will send the cash to the employees using the M-Pesa B2C API
	- Your application does not have to send the cash but at least work in the sandbox environment
	- As much as possible not to use 3rd party libraries except the ones that come with the
		programming language of choice

# Choice of Technology  

## WildFly Swarm ( Java EE Microservice)
    That said, why run your Java apps in an application server that has many pieces that aren’t used? By cutting out the fat via WildFly Swarm, 
		you get an app and application server combined uber-JAR that sucks up less memory, and is more responsive. Plus, by eliminating unnecessary 
		overhead, they improve security because they reduce your apps’ exposure.

    In short, WildFly Swarm lets you fully embrace all the benefits of a microservices-oriented development and deployment workflow. 
		You no longer have to take a monolithic approach to building and running JavaEE apps. Instead, you can compile and deploy just the parts you need, 
		and leave out everything you don’t.
## Microservice 
The concept of microservices is more than a simple novelty. With the advent of DevOps and the boom of container technologies and deployment automation tools, microservices are changing the way developers structure their applications.

The main purpose of a microservice architecture is to break down an application into smaller standalone components that are easier to handle, deploy, scale and maintain in the long term.Encapsulation, cohesion and a good understanding of service-oriented architectures have helped developers apply this “divide and conquer” strategy to software architecture for many years and will do so in the future as well.
So, what’s the advantage of microservices then:

- Have a small
- Be cohesive, e.g. focus on a single feature or functionality
- Expose an interface to be used by other services of the same application or other external services.

Allow independent management, so that it can be coded, tested, deployed and scaled faster.
Be responsible for handling its own data.
Be completely isolated from other services, such as no direct dependencies are needed.
## Maven
[Maven](https://maven.apache.org) is a build automation tool used primarily for Java projects. Maven addresses two aspects of building software: first, it describes how software is built, and second, it describes its dependencies

Maven’s primary goal is to allow a developer to comprehend the complete state of a development effort in the shortest period of time. In order to attain this goal there are several areas of concern that Maven attempts to deal with:

- Making the build process easy
- Providing a uniform build system
- Providing quality project information
- Providing guidelines for best practices development
- Allowing transparent migration to new features

## Java EE

##

