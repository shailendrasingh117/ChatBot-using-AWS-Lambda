import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Chatbot implements RequestStreamHandler {

    private static final String S3_OPTIMIZING_PERFORMANCE_BEST_PRACTICES_URL = "https://docs.aws.amazon.com/whitepapers/latest/s3-optimizing-performance-best-practices/welcome.html?did=wp_card&trk=wp_card";

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

                // Get the answer from the whitepaper
                String answer = getAnswerFromWhitepaper(input);

                // Format the response
                String output = "You asked: " + input + "\n\nHere's what I found:\n" + answer;

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

    private static String getAnswerFromWhitepaper(String query) throws IOException {
        // Get the whitepaper content
        String content = getWhitepaperContent(S3_OPTIMIZING_PERFORMANCE_BEST_PRACTICES_URL);

        // Search for the query in the content
        Pattern pattern = Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            // Get the answer text
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            String answer = content.substring(startIndex, endIndex);

            // Format the answer text
            answer = answer.replaceAll("[\\t\\n\\r]+", " ");
            answer = answer.replaceAll(" +", " ");
            answer = answer.trim();

            return answer;
        } else {
            return "Sorry, I couldn't find an answer to your question.";
        }
    }

    private static String getWhitepaperContent(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        try (InputStream inputStream = con.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

}
