package controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class WitAiConnector {

    private static final String WIT_AI_URL = "https://api.wit.ai/message?v=20220401&q=";
    private static final String WIT_AI_TOKEN = "7MGMLX47K5HR5ISBLSYB2J3J7ATH5DEP"; // Replace with your server access token

    public static String sendMessage(String message) {
        // Encode the message to ensure special characters are processed correctly in the URL
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        // Construct the complete URL for the GET request
        String url = WIT_AI_URL + encodedMessage;

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + WIT_AI_TOKEN)
                .GET() // GET request
                .build();

        // Create an HttpClient
        HttpClient client = HttpClient.newHttpClient();

        try {
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Return the response body as a String
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error during communication with Wit.ai: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        // Example usage of the sendMessage method
        String response = sendMessage("hello");
        System.out.println(response);
    }
}
