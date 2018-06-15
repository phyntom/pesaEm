# pesaEM Demo Application

This application is written in Java and it is based on JSF (Java Server Faces) technology.

For the runtime it uses Wildfly Swarm as container/packaging.

The data model of this application consists of two entities: Customer and Note.

It can be built using Maven.

# Requirement to run demo

1. JDK 8
2. Maven 3.3.x
3. IDE (Intellij/Eclipse)
4. Git
Another important requirement is to change the upload_folder value located in config.propeties in (src/main/resources)
Example :
	upload_folder=/home/awesome/
The value /home/awesome/ will represent the location where the csv file will be uploaded. Pay attention to this detail in order to make 
this application run smoothly

# Starting the app

Go inside the project folder and follow the instruction
## First Approach
mvn wildfly-swarm:run

Open browser and access http://127.0.0.1:8085/
You will be required to provide _**username and password**_:
-  username : admin
-  password : admin123
In addition the customer data is exposed in a JAX-RS resource which is accessible via http://127.0.0.1:8085/index.xhtml.

## Second Approach
mvn package && java -jar target/demo-swarm.jar

# Focus

The main focus of this application is to demonstrate a simple project setup and usage of Java EE standards, which Wildfly Swarm supports.

