package controllers;

import org.json.JSONObject;

public class BotResponseParser {

    public static String getReadableResponse(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        // Check if intents array is present and not empty
        if (jsonObject.has("intents") && !jsonObject.getJSONArray("intents").isEmpty()) {
            // Get the first intent from the array
            JSONObject firstIntent = jsonObject.getJSONArray("intents").getJSONObject(0);
            // Extract the name of the intent and confidence if needed
            String intentName = firstIntent.getString("name");
            double confidence = firstIntent.getDouble("confidence");

            // Here you can map the intent to a user-friendly response
            // This is a very basic example and should be expanded based on your application's needs
            return "Bot understood intent: " + intentName + " with confidence: " + confidence;
        } else {
            // No intents found, provide a default response or prompt the user for more information
            return "I'm not sure how to respond to that. Can you provide more details?";
        }
    }
}
