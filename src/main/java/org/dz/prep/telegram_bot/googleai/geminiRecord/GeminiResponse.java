package org.dz.prep.telegram_bot.googleai.geminiRecord;

import java.util.List;

public record GeminiResponse(List<Candidate> candidates,
                             GeminiResponse.UsageMetaData usageMetaData, String modelVersion) {

    public record Candidate(Content content, String finishReason, Long avgLogprobs)  {
    }
    public record UsageMetaData(Integer promptTokenCount, Integer candidatesTokenCount, Integer totalTokenCount) {}

}
