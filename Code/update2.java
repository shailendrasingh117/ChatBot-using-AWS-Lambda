import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Chatbot implements RequestStreamHandler {

    private final List<String> whitePaperLinks = new ArrayList<>(); // List of AWS whitepaper links

    public Chatbot() {
        // Initialize the list of AWS whitepaper links
        whitePaperLinks.add("https://d1.awsstatic.com/whitepapers/configuring-amazon-rds-as-peoplesoft-database.pdf?did=wp_card&trk=wp_card");
        whitePaperLinks.add("https://docs.aws.amazon.com/whitepapers/latest/s3-optimizing-performance-best-practices/welcome.html?did=wp_card&trk=wp_card");
        whitePaperLinks.add("https://docs.aws.amazon.com/whitepapers/latest/optimizing-mysql-on-ec2-using-amazon-ebs/optimizing-mysql-on-ec2-using-amazon-ebs.html?did=wp_card&trk=wp_card");
    }

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
                String message = body.get("message").toString().toLowerCase();

                if (message.contains("whitepaper")) {
                    String link = whitePaperLinks.get((int) (Math.random() * whitePaperLinks.size()));
                    responseJson.put("output", "Here is a link to an AWS whitepaper: " + link);
                } else {
                    responseJson.put("output", "I'm sorry, I don't understand. Can you please rephrase?");
                }
            }
        } catch (ParseException e) {
            responseJson.put("output", "Sorry, something went wrong. Please try again later.");
            responseCode = "400";
        }

        JSONObject headersJson = new JSONObject();
        headersJson.put("Content-Type", "application/json");
        try {
            outputStream.write(new JSONObject()
                    .put("isBase64Encoded", false)
                    .put("statusCode", responseCode)
                    .put("headers", headersJson)
                    .put("body", responseJson.toJSONString())
                    .toString()
                    .getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
