package org.dz.prep.telegram_bot.command;

/**
 * Enum representing the supported Telegram commands in the bot. Each command is associated
 * with a string value representing the actual command text used in Telegram messages.
 */
public enum TelegramCommands {

    START_COMMAND("/start"),
    CLEAR_COMMAND("/clearhistory");

    private final String command;

    TelegramCommands(String command){
        this.command = command;
    }

    public String getCommand(){
        return this.command;
    }
}
