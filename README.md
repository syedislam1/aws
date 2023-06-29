# aws
This test the DynamoDB database using aws resorces like aws lambda, API Gateway.

The object of this project is to Create an automating script framework which involves the testing the AWS DynamoDB database to store and retrieve user information.

The application has two main functionalities:

Registration: allows a user to register by providing their name, email, and password. The registration API creates a new item in the DynamoDB table with the user information.
Login: allows a registered user to log in by providing their email and password. The login API queries the DynamoDB table to find the user with the provided email and password and returns a token that the user can use to access protected resources.


**SETUP:**
Tool: Eclipse
Languagae: JAVA
Test written in Junit: **DynamoDBAPITest.java** With methods to save User:storeUser and to Login : loginUser
2 lambda functions name: Userlogin and userRegistration .
2 Api Gateway with Post Method Name: registeruser("https://bixc0so21f.execute-api.us-east-1.amazonaws.com/dev/"/registeruser")  and loginuser ("https://bixc0so21f.execute-api.us-east-1.amazonaws.com/dev/loginuser")
Database DynamoDB Name: UserTable Values Email as primary key, name and password.

