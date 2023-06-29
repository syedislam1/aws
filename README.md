# aws
This test the DynamoDB database using aws resorces like aws lambda, API Gateway.

The object of this project is to Create an automating script framework which involves the testing the AWS DynamoDB database to store and retrieve user information.

The application has two main functionalities:

Registration: allows a user to register by providing their name, email, and password. The registration API creates a new item in the DynamoDB table with the user information.
Login: allows a registered user to log in by providing their email and password. The login API queries the DynamoDB table to find the user with the provided email and password and returns a token that the user can use to access protected resources.
