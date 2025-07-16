package org.dz.prep.telegram_bot;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dz.prep.telegram_bot.command.TelegramCommandDispatcher;
import org.dz.prep.telegram_bot.command.TelegramCommandHandler;
import org.dz.prep.telegram_bot.googleai.GeminiClient;
import org.dz.prep.telegram_bot.googleai.GeminiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A Telegram bot implementation  that handles incoming updates, process commands and
 * interacts with GeminiService for generating response.
 */
@Component
public class DzBot extends TelegramLongPollingBot {


    private static final Logger log = LoggerFactory.getLogger(DzBot.class.getName());

    //Service for generating response to user`s messages
    private final GeminiService geminiService;

    // Dispatcher for processing Telegram-specific commands.
    private final TelegramCommandDispatcher telegramCommandDispatcher;

    /**
     * Constructor to initialize the bot with the required dependencies and token
     * @param botToken The bot token required for authenticating with Telegram's API.
     * @param geminiService The service for interacting with Gemini API to generate responses.
     * @param telegramCommandDispatcher The dispatcher for handling Telegram-specific commands.
     */
    public DzBot(
                 @Value("${bot.token}") String botToken,
                 GeminiService geminiService,
                 TelegramCommandDispatcher telegramCommandDispatcher) {

        super(new DefaultBotOptions(), botToken);
        this.telegramCommandDispatcher = telegramCommandDispatcher;
        this.geminiService = geminiService;

    }


    /**
     * Processes incoming updates from Telegram and sends appropriate responses.
     *
     * @param update The update received from Telegram, which may contain a message or a command.
     */
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        try {
            var methods = processCommand(update); // Process the update and generate responses.
            methods.forEach(m -> {
                try {
                    sendApiMethod(m); // Send the generated responses to the user.
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e); // Log and rethrow exceptions during API communication
                }
            });
        }
        catch (Exception e){
            log.error(e.getMessage());
            sendErrorMessage(update.getMessage().getChatId());   // Inform the user about the error.
        }

    }


    /**
     * Retrieves the bot's username as registered with Telegram.
     *
     * @return The bot's username ("DzBot").
     */
    @Override
    public String getBotUsername() {
        return "DzBot";
    }


    /**
     * Processes a given update to determine whether it's a command or a message.
     * Delegates command handling to the TelegramCommandDispatcher and generates responses using GeminiService.
     *
     * @param update The update containing the user's input.
     * @return A list of BotApiMethod objects to be sent as responses to the user.
     */
    private List<BotApiMethod<?>> processCommand(Update update){

        if(telegramCommandDispatcher.isCommand(update)){
            return List.of(telegramCommandDispatcher.processCommand(update));
        }

        if(update.hasMessage() && update.getMessage().hasText()){
            var text = update.getMessage().getText(); // Extracts the user's text message.
            var userId = update.getMessage().getChatId();  // Extracts the user's chat ID.

            var textResp = geminiService.getChatResponse(userId,text); // Generates a response using GeminiService.

            SendMessage message = new SendMessage(userId.toString(),textResp); // Prepares the response message.

            return List.of(message);
        }

        return List.of(); // Returns an empty list if no valid command or message is found.

    }



    /**
     * Sends an error message to the user in case of any exception during processing.
     *
     * @param userId The user's chat ID to whom the error message should be sent.
     */
    @SneakyThrows
    private void sendErrorMessage(Long userId) {

        SendMessage message =
                new SendMessage(userId.toString(),"There was an error processing your request. Please try again later");


            sendApiMethod(message);


    }
}




