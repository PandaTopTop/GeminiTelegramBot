package org.dz.prep.telegram_bot.googleai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//Class is a Spring configuration class responsible for setting up
//and providing a configured GeminiClient bean for interacting with the Gemini AP
@Configuration
public class GeminiConfig {

@Bean
    public GeminiClient geminiClient(@Value("${gemini.token}")String token,
                                     RestTemplateBuilder restTemplateBuilder) {

    return new GeminiClient(token,restTemplateBuilder.build());
}

}
