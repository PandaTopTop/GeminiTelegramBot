package org.dz.prep.telegram_bot.googleai;

import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;
import org.dz.prep.telegram_bot.googleai.geminiRecord.GeminiRequest;
import org.dz.prep.telegram_bot.googleai.geminiRecord.GeminiResponse;
import org.dz.prep.telegram_bot.googleai.geminiRecord.TextPart;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public record GeminiClient(String token, RestTemplate restTemplate)  {

    public GeminiResponse getGeminiResponse(GeminiRequest geminiRequest) {
        String uri = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        headers.set("x-goog-api-key", token);


        HttpEntity<GeminiRequest> request = new HttpEntity<>(geminiRequest, headers);

        GeminiResponse response = restTemplate.postForObject(uri,request, GeminiResponse.class);
        return response;


    }
}
