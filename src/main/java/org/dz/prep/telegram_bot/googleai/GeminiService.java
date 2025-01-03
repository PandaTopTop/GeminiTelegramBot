package org.dz.prep.telegram_bot.googleai;

import lombok.AllArgsConstructor;
import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;
import org.dz.prep.telegram_bot.googleai.geminiRecord.GeminiRequest;
import org.dz.prep.telegram_bot.googleai.geminiRecord.TextPart;
import org.dz.prep.telegram_bot.googleai.history.GeminiHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GeminiService {

    private final GeminiClient geminiClient;
    private final GeminiHistory geminiHistory;
    public String getChatResponse(Long userId, String userMessage){

        geminiHistory.createUserChatHistoryIfNotExist(userId);

        Content content = Content.builder().parts(List.of(new TextPart(userMessage))).role("user").build();

        geminiHistory.addContentUserChatHistory(userId,content);
        List<Content> contentList = geminiHistory.getUserChatHistory(userId).userChatHistory();

        GeminiRequest geminiRequest = GeminiRequest.builder().contents(contentList).build();

        var geminiResponse = geminiClient.getGeminiResponse(geminiRequest);

        geminiHistory.addContentUserChatHistory(userId,geminiResponse.candidates().get(0).content());

        var textResp = geminiResponse.candidates().get(0).content().parts().get(0).text();



        return textResp;
    }
}
