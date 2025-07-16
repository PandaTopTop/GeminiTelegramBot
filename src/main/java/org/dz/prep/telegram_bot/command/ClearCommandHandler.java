package org.dz.prep.telegram_bot.command;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.dz.prep.telegram_bot.googleai.history.GeminiHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * A handler for the "/clear" command in a Telegram bot. This handler clears the chat history
 * for the user and responds with a confirmation message.
 */
@RequiredArgsConstructor
@Component
public class ClearCommandHandler implements TelegramCommandHandler{


    private final String CLEAR_MESSAGE = "The History has been cleared!";

    // Service responsible for managing and clearing the user's chat history.
    private final GeminiHistoryService geminiHistoryService;



    /**
     * Processes the "/clear" command by clearing the user's chat history and
     * returning a confirmation message.
     *
     * @param update The Telegram update containing the user's command and information.
     * @return A SendMessage instance with the confirmation message to be sent to the user.
     */
    @Override
    public BotApiMethod<?> processCommand(Update update) {
        // Retrieve the user's chat ID and clear their chat history using the service.
        var userId = update.getMessage().getChatId();
            geminiHistoryService.clearHistory(userId);

        return SendMessage.builder().chatId(userId)
                .text(CLEAR_MESSAGE).build();
    }

    @Override
    public TelegramCommands getSupportedCommands() {
        return TelegramCommands.CLEAR_COMMAND;
    }
}
