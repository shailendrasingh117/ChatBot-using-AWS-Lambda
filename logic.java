public String getResponse(String input) {
    // List of AWS services
    String[] services = {"S3", "Lambda", "EC2", "RDS", "DynamoDB", "CloudFront"};

    // Iterate over the services and check if the input contains the service name
    for (String service : services) {
        if (input.toLowerCase().contains(service.toLowerCase())) {
            return "Yes, AWS " + service + " is a service provided by Amazon Web Services.";
        }
    }

    // If the input doesn't contain any AWS service, return a default response
    return "I'm sorry, I didn't understand your question. Please try again with an AWS service name.";
}
