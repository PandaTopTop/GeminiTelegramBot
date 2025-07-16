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
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
//@ConfigurationProperties(prefix = "bot")
public class DzBotConfiguration {




    @Bean
//    @SneakyThrows
    public TelegramBotsApi telegramBotsApi(TelegramLongPollingBot bot){
        TelegramBotsApi botApi;
        try {   botApi = new TelegramBotsApi(DefaultBotSession.class);

            botApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return botApi;
    }
}
