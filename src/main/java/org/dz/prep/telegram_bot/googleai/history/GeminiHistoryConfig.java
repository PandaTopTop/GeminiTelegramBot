package org.dz.prep.telegram_bot.googleai.history;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiHistoryConfig {

    @Bean
    public GeminiHistory geminiHistory() {
        return new GeminiHistory();
    }
}
