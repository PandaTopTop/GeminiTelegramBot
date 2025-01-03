package org.dz.prep.telegram_bot.googleai.history;

import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GeminiHistory {

    Map<Long,UserChatHistory> usersHistory = new ConcurrentHashMap<>();


    public UserChatHistory getUserChatHistory(long userId) {


        return usersHistory.get(userId);
    }



    public UserChatHistory createUserChatHistory(long userId){


            usersHistory.put(userId,new UserChatHistory(new LinkedList<>()));

        return usersHistory.get(userId);
    }



    public void  addContentUserChatHistory(long userId, Content content) {

        usersHistory.get(userId).userChatHistory().add(content);
    }

    public void createUserChatHistoryIfNotExist(long userId){


        if(!usersHistory.containsKey(userId)) {
            createUserChatHistory(userId);
        }
    }
}
