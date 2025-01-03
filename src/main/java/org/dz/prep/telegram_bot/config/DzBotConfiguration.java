package org.dz.prep.telegram_bot.config;


import lombok.SneakyThrows;
import org.dz.prep.telegram_bot.DzBot;
import org.dz.prep.telegram_bot.googleai.GeminiClient;
import org.dz.prep.telegram_bot.googleai.GeminiService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
//@ConfigurationProperties(prefix = "bot")
public class DzBotConfiguration {

//    String token; // try later and delete

    @Bean
//    @SneakyThrows
    public DzBot dzBot(
                @Value("${bot.token}") String token,
                TelegramBotsApi botsApi,
                GeminiService geminiService
    ) {

        System.out.println(token);
        var botOptions = new DefaultBotOptions();
        var bot = new DzBot(botOptions,token, geminiService);

        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return bot;
    }



    @Bean
//    @SneakyThrows
    public TelegramBotsApi telegramBotsApi(){
        TelegramBotsApi botApi = null;
        try {
            botApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return botApi;
    }
}
