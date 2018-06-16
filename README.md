# pesaEM Demo Application

This application is written in Java and it is based on 

- JSF (Java Server Faces) technology.
- Bean Validation
- CDI (Context Dependency Inject)
- Primefaces
- Wildfly Swarm ()

For the runtime it uses Wildfly Swarm as container/packaging.
# Requirement to run demo

1. JDK 8
2. Maven 3.3.x
3. IDE (Intellij/Eclipse)
4. Git
5. **Another important requirement is to change the upload folder location value in the file located in config.propeties in (src/main/resources)**. The content of this file looks as follow: 
```
upload_folder=/home/aimable/
consumer_key=AcJHVPJGXVGrVkgG3qpMS3eNneKsFlqs
consumer_secret=KUuN2GESyYWANyEK
initiator_name=testapi
initiator_password=Safaricom864!
shortcode=600864
```
> The value **/home/aimable/** should be changed to something like `/home/awesome/` representing your folder location where you want the csv files to be uploaded.  > Do not change the properties keys as those values are read by the applications. Only change property values.

# Starting and running the demo on local machine

Go inside the project folder and follow the instruction

## First Approach
```
 mvn wildfly-swarm:run 
 
 ```
Open browser and access http://127.0.0.1:8085/
You will be required to provide _**username and password**_:
-  username : admin
-  password : admin123

> **Note** : To simplify testing the functionalities of the application logs are printed in the console and file so that reviewer can see the execution process. The process of sending payment requests to Mpesa platform is also logged in the console and log file located in 
## Second Approach

```
mvn package && java -jar target/demo-swarm.jar

```
> **Note** : To simplify testing the functionalities of the application logs are printed in the console and file so that reviewer can see the execution process. The process of sending payment requests to Mpesa platform is also logged in the console and log file located in 
# Running the demo online
Go in your prefered web browser and put this link :
```
http://104.222.96.147:8085/

```
Provide **username and password** with following credentials
- username: admin
- password: admin123
# Focus
The main focus of this application is to demonstrate a simple project setup and usage of Java EE standards and respond to the following requirement:

- Create an application that will send the cash to the employees using the M-Pesa B2C API
- Your application does not have to send the cash but at least work in the sandbox environment
- As much as possible not to use 3rd party libraries except the ones that come with the programming language of choice
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
I have opted to use Java in this because it is the programming language which I'm familiar with. Having choosen Java as programming language the choice was clear to opt for Java Entreprise Edition (Java EE) components which offers a set of specifications, extending native Java or Java SE with specifications for enterprise features such as distributed computing and web services. Java EE applications are run on reference runtimes, that can be microservices or application servers, which handle transactions, security, scalability, concurrency and management of the components it is deploying
### Java Servlet 
Defines how to manage HTTP requests, in a synchronous or asynchronous way. It is low level and other Java EE specifications rely on it.
### Java Server Faces 
A technology for constructing user interfaces out of components;
### CDI 
It is a specification to provide a depencency injection container, as in Spring
### Bean Validation
Bean Validation defines a metadata model and API for JavaBean validation. The metadata source is annotations, with the ability to override and extend the meta-data through the use of XML validation descriptors.
# Source code structure
| directories | content |
|--|--|
| src/main/java | contains java classes |
| src/main/reources | contains project resources files |
| src/main/webapp | contains web application files such as deployment descriptor, web pages,... |
# Future improvement
I beleive this demo app can be improved in many area such as:

- Persisting transactions in database or other storage 
- Since Mpesa Core process request in async mode, it is required to implement as RestFull end point to listen to asynch responses sent back by Mpesa Core System. This mean we can introduce RestFull web service to handle incoming request or implement low level servlet o listen to those incoming response
- Entreprise Java Bean to handle transactions, security, dependency injection and access control for business objects and many more.
- If the process of sending those payment need to be automated for periodically execution we can implement Batch Applications which provides the means to run long running background tasks that possibly involve a large volume of data and which may need to be periodically executed
