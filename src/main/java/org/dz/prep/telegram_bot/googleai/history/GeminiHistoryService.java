package org.dz.prep.telegram_bot.googleai.history;

import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


//Create and store History for users of ChatBot
@Service
public class GeminiHistoryService {

    UserChatHistoryRepository chatHistoryRepository;

    @Autowired
    public GeminiHistoryService(UserChatHistoryRepository chatHistoryRepository) {
        this.chatHistoryRepository = chatHistoryRepository;
    }


    public UserChatHistory getUserChatHistory(long userId) {
        return     chatHistoryRepository.findById(userId).orElse(null);

    }


    public void createUserChatHistory(long userId){
        List<Content> userHistoryList = new LinkedList<>();
        UserChatHistory userChatHistory = new UserChatHistory(userId,userHistoryList);


        chatHistoryRepository.save(userChatHistory);


    }


    //store the previous user`s messages
    public void  addContentUserChatHistory(long userId, Content content) {

        Optional<UserChatHistory> userChatHistory = chatHistoryRepository.findById(userId);

//        if(userChatHistory.isPresent()){
        List<Content> existHistory = userChatHistory.get().getUserChatHistoryList();

        existHistory.add(content);

        UserChatHistory updatedUserChatHistory = new UserChatHistory(userId,existHistory);

        chatHistoryRepository.save(updatedUserChatHistory);
//        }
    }

    // create chat history for the concrete user
    public void createUserChatHistoryIfNotExist(long userId){

        if(!chatHistoryRepository.existsById(userId)) {
            createUserChatHistory(userId);
        }
    }


    public void clearHistory(Long userId) {

        if(chatHistoryRepository.existsById(userId)) {
            UserChatHistory userChatHistory = new UserChatHistory(userId, new LinkedList<>());
            chatHistoryRepository.save(userChatHistory);
        }

    }
}
