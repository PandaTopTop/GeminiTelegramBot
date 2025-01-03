package org.dz.prep.telegram_bot;


import lombok.SneakyThrows;
import org.dz.prep.telegram_bot.googleai.GeminiClient;
import org.dz.prep.telegram_bot.googleai.GeminiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DzBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(DzBot.class.getName());

    private final GeminiService geminiService;

    public DzBot(DefaultBotOptions options, String botToken, GeminiService geminiService) {
        super(options, botToken);
        this.geminiService = geminiService;

    }



    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {


        if(update.hasMessage() && update.getMessage().hasText()){
            var text = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();

            var textResp = geminiService.getChatResponse(chatId,text);

            SendMessage message = new SendMessage(chatId.toString(),textResp);

            sendApiMethod(message);

        }


    }

    @Override
    public String getBotUsername() {
        return "DzBot";
    }
}
