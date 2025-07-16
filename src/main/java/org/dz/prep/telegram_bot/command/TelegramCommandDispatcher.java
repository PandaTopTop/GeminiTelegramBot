package org.dz.prep.telegram_bot.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


/**
 * A service class responsible for processing Telegram commands by dispatching
 * them to the appropriate handlers. It determines if a given message is a command
 * and ensures unsupported commands are handled gracefully.
 */
@Service
@AllArgsConstructor
public class TelegramCommandDispatcher {

    // A list of command handlers responsible for processing specific commands.
    private final List<TelegramCommandHandler> commandHandlerList;

    /**
     * Processes a given Telegram update to find and execute the appropriate command handler.
     *
     * @param update The update containing the user's command message.
     * @return A BotApiMethod representing the response to be sent to the user.
     */
    public BotApiMethod<?> processCommand(Update update) {


        var commandText = update.getMessage().getText();

        // Finds a suitable command handler for the given command
        var  suitableHandler = commandHandlerList.stream().
              filter(x -> x.getSupportedCommands().getCommand().equals(commandText)).
              findFirst();

        // Handles unsupported commands if no suitable handler is found.
        if (suitableHandler.isEmpty()){
            return  handleUnsupportedCommand(update, commandText);
        }

        // Executes the command using the suitable handler.
        return suitableHandler.get().processCommand(update);
    }

    /**
     * Determines if a given Telegram update contains a command.
     *
     * @param update The update to be checked.
     * @return true if the update contains a valid command, false otherwise.
     */
    public boolean isCommand(Update update){

        return  update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().startsWith("/");
    }


    /**
     * Handles unsupported commands by sending a message indicating the command is not recognized.
     *
     * @param update      The update containing the unsupported command.
     * @param commandText The text of the unsupported command.
     * @return A BotApiMethod representing the error message to be sent to the user.
     */
    private BotApiMethod<?> handleUnsupportedCommand(Update update, String commandText){


            var userId = update.getMessage().getChatId().toString();

            // Constructs an error message indicating the unsupported command.
            return SendMessage.builder().chatId(userId).text("Command %s isn`t supported".formatted(commandText)).build();


    }
}
