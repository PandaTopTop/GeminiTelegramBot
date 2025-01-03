package org.dz.prep.telegram_bot.command;

public enum TelegramCommands {

    START_COMMAND("/start"),
    CLEAR_COMMAND("/clear");

    private final   String command;

    private TelegramCommands(String command){
        this.command = command;
    }

    public String getCommand(){
        return this.command;
    }
}
