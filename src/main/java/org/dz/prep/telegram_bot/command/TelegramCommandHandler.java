package org.dz.prep.telegram_bot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

 /** Interface representing a handler for Telegram commands.
 * Each implementing class is responsible for processing a specific Telegram command and
 * providing the logic for handling that command.
 */
public interface TelegramCommandHandler {
     /**
      * Processes the incoming Telegram update containing the command.
      *
      * @param update The update object containing the command and related information.
      * @return A BotApiMethod object that represents the response to be sent to the user.
      */
    BotApiMethod<?> processCommand(Update update);



     /**
      * Retrieves the Telegram command supported by this handler.
      *
      * @return A {@link TelegramCommands }object representing the command supported by this handler.
      */
    TelegramCommands getSupportedCommands();
}
