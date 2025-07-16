package org.dz.prep.telegram_bot.googleai;

import lombok.AllArgsConstructor;
import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;
import org.dz.prep.telegram_bot.googleai.geminiRecord.GeminiRequest;
import org.dz.prep.telegram_bot.googleai.geminiRecord.TextPart;
import org.dz.prep.telegram_bot.googleai.history.GeminiHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GeminiService {
    // The GeminiClient is used to interact with the Gemini API.
    private final GeminiClient geminiClient;

    // The GeminiHistoryService manages the user's chat history.
    private final GeminiHistoryService geminiHistory;



    /**
     * Generates a response for the user`s message by interacting with the Gemini API
     * and maintaining the chat history
     * @param userId The unique identifier of the user sending the message
     * @param userMessage The content of the message sent by the user.
     * @return The response text generate by the Gemini API
     */
    public String getChatResponse(Long userId, String userMessage){

        // Ensure the chat history exists for the user; create it if not.
        geminiHistory.createUserChatHistoryIfNotExist(userId);

        // Build a Content object representing the user's message.
        Content content = Content.builder().
                parts(List.of(new TextPart(userMessage)))
                .role("user").
                build();

        // Add the user's message to their chat history.
        geminiHistory.addContentUserChatHistory(userId,content);

        // Retrieve the complete chat history for the user.
        List<Content> contentList = geminiHistory.getUserChatHistory(userId).getUserChatHistoryList();

        // Build a GeminiRequest with the user chat history.
        GeminiRequest geminiRequest = GeminiRequest.builder().
                contents(contentList)
                .build();

        //Send the request to the Gemini API and retrieve the response.
        var geminiResponse = geminiClient.getGeminiResponse(geminiRequest);

        //Add the Gemini API response to the user`s chat history.
        geminiHistory.addContentUserChatHistory(userId,geminiResponse.candidates().get(0).content());


        // Extract the text content from the first candidate of the response.
        var textResp = geminiResponse.candidates().get(0).content().parts().get(0).text();


        // Return the generated response text.
        return textResp;
    }
}
