# ChatBot-using-AWS-Lambda
This is a simple chatbot that can answer questions about AWS services. It uses AWS Lambda and API Gateway to handle requests and responses.

# Getting Started
# Prerequisites
To run this chatbot, you will need:

1. An AWS account
2. AWS CLI installed on your local machine
3. Maven installed on your local machine
# Installation
1. Clone the repository:
```
bash
Copy code
git clone https://github.com/yourusername/aws-services-chatbot.git
```
2. Go to the project directory:
```
bash
Copy code
cd aws-services-chatbot
```
3. Build the project using Maven:
go
Copy code
```
mvn clean package
```
4. Deploy the Lambda function and API Gateway using AWS CLI:
css
Copy code
```
aws cloudformation deploy --template-file cloudformation.yml --stack-name aws-services-chatbot --capabilities CAPABILITY_IAM
```
5. Get the API Gateway URL and test the chatbot:
scss
Copy code
```
aws cloudformation describe-stacks --stack-name aws-services-chatbot --query 'Stacks[].Outputs[?OutputKey==`ApiUrl`].OutputValue' --output text
```
# Usage
6. To use the chatbot, send a POST request to the API Gateway endpoint with the following JSON payload:

css
Copy code
```
{
    "input": "What is S3?"
}
```
7. The chatbot will respond with a message that contains information about S3.

# Contributing
If you would like to contribute to this project, please fork the repository and submit a pull request.
