package org.dz.prep.telegram_bot.command;

import org.dz.prep.telegram_bot.googleai.history.GeminiHistoryService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * A handler for the "/start" command in a Telegram bot. This handler responds with
 * a welcome message introducing the bot's functionality and usage instructions.
 */
@Component
public class StartCommandHandler implements TelegramCommandHandler{

    private final String HELLO_MESSAGE = """
    Hello %s, you can use this bot to speak with Gemini AI!
    If you will wan`t to clear the history use the /clear command!
""";


    /**
     * Processes the "/start" command by generating a personalized welcome message.
     *
     * @param update The Telegram update containing the user's command and information.
     * @return A SendMessage instance with the welcome message to be sent to the user.
     */
    @Override
    public BotApiMethod<?> processCommand(Update update) {

       return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(HELLO_MESSAGE.formatted(update.getMessage().getFrom().getFirstName()))
                .build();

    }



    @Override
    public TelegramCommands getSupportedCommands() {
        return TelegramCommands.START_COMMAND;
    }
}
