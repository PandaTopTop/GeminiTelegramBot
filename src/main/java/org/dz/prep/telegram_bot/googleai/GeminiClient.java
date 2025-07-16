package org.dz.prep.telegram_bot.googleai;

import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;
import org.dz.prep.telegram_bot.googleai.geminiRecord.GeminiRequest;
import org.dz.prep.telegram_bot.googleai.geminiRecord.GeminiResponse;
import org.dz.prep.telegram_bot.googleai.geminiRecord.TextPart;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * A client for interacting with the Gemini API to generate responses.
 * This class uses a {@link RestTemplate} to send HTTP requests and retrieve
 * responses from the Gemini service.
 *
 * @param token         The API key used for authentication with the Gemini API.
 * @param restTemplate  A Spring utility for making HTTP requests.
 */
public record GeminiClient(String token, RestTemplate restTemplate)  {


    /**
     * Sends a POST request to the Gemini API with the given request payload and retrieves
     * the corresponding response.
     *
     * @param geminiRequest The request payload containing the necessary data to send to the Gemini API.
     * @return The response from the Gemini API as a {@link GeminiResponse} object.
     */
    public GeminiResponse getGeminiResponse(GeminiRequest geminiRequest) {

        // The URI of the Gemini API endpoint.
        String uri = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        headers.set("x-goog-api-key", token);// Add the API key for authentication.

        // Create an HTTP entity that wraps the request payload and headers.
        HttpEntity<GeminiRequest> request = new HttpEntity<>(geminiRequest, headers);

        // Send the POST request to the API and map the response to a GeminiResponse object.
        GeminiResponse response = restTemplate.postForObject(uri,request, GeminiResponse.class);
        return response;


    }
}
