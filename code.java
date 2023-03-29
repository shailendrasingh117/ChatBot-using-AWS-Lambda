import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lexruntime.AmazonLexRuntime;
import com.amazonaws.services.lexruntime.AmazonLexRuntimeClientBuilder;
import com.amazonaws.services.lexruntime.model.PostTextRequest;
import com.amazonaws.services.lexruntime.model.PostTextResult;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Chatbot implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();
        String responseCode = "200";

        try {
            JSONObject event = (JSONObject) parser.parse(reader);
            if (event.get("httpMethod").equals("POST")) {
                JSONObject body = (JSONObject) parser.parse(event.get("body").toString());
                String input = body.get("input").toString();

                String output = "";

                // Logic to handle user input
                if (input.equalsIgnoreCase("What is Amazon S3?")) {
                    output = "Amazon Simple Storage Service (Amazon S3) is an object storage service that offers industry-leading scalability, data availability, security, and performance.";
                } else if (input.equalsIgnoreCase("What is AWS Lambda?")) {
                    output = "AWS Lambda is a compute service that lets you run code without provisioning or managing servers. You pay only for the compute time you consume.";
                } else if (input.equalsIgnoreCase("What is Amazon EC2?")) {
                    output = "Amazon Elastic Compute Cloud (Amazon EC2) is a web service that provides resizable compute capacity in the cloud.";
                } else {
                    output = "I'm sorry, I don't understand. Please ask a question about an AWS service.";
                }

                JSONObject responseBody = new JSONObject();
                responseBody.put("message", output);

                JSONObject responseHeaders = new JSONObject();
                responseHeaders.put("Content-Type", "application/json");

                responseJson.put("isBase64Encoded", false);
                responseJson.put("statusCode", responseCode);
                responseJson.put("headers", responseHeaders);
                responseJson.put("body", responseBody.toString());
            }
        } catch (ParseException e) {
            responseJson.put("statusCode", "400");
            responseJson.put("exception", e);
        }

        outputStream.write(responseJson.toJSONString().getBytes());
    }
}
