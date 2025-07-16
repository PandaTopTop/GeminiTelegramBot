package org.dz.prep.telegram_bot.googleai.history;


import org.apache.hc.core5.concurrent.CompletedFuture;
import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;
import org.dz.prep.telegram_bot.googleai.geminiRecord.TextPart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiHistoryServiceTest {
    @Mock
    UserChatHistoryRepository chatHistoryRepository;

    @InjectMocks
    GeminiHistoryService geminiHistoryService;

    @Test
    void getUserChatHistory() {

        UserChatHistory userChatHistory = new UserChatHistory();
        userChatHistory.setUserId(1L);

        List<Content> userChatHistoryList = new LinkedList<>();
        userChatHistory.setUserChatHistoryList(userChatHistoryList);


        when(chatHistoryRepository.findById(anyLong())).thenReturn(Optional.of(userChatHistory));

        assertEquals(geminiHistoryService.getUserChatHistory(1L),userChatHistory);

    }

    @Test
    void createUserChatHistory() {
        //given
        List<Content> userHistoryList = new LinkedList<>();
        UserChatHistory userChatHistory = new UserChatHistory(1L,userHistoryList);

        //when

        geminiHistoryService.createUserChatHistory(1);


        //then
        ArgumentCaptor<UserChatHistory> captor = ArgumentCaptor.forClass(UserChatHistory.class);

        verify(chatHistoryRepository,timeout(500)).save(captor.capture()); // timeout fin

        assertEquals(captor.getValue().userId,userChatHistory.userId);


    }

    @Test
    void addContentUserChatHistory() {
        long userId = 1L;
        List<Content> userHistoryList = new LinkedList<>();
        UserChatHistory previousUserChatHistory = new UserChatHistory(userId,userHistoryList);
        when(chatHistoryRepository.findById(anyLong())).thenReturn(Optional.of(previousUserChatHistory));
        Content content = new Content(List.of(new TextPart("Hello")),"user");



        ArgumentCaptor<UserChatHistory> captor = ArgumentCaptor.forClass(UserChatHistory.class);

        geminiHistoryService.addContentUserChatHistory(userId,content);


        verify(chatHistoryRepository).findById(userId);

        verify(chatHistoryRepository).save(captor.capture());

        String textFromAddedContent = captor.getValue().getUserChatHistoryList().get(0).parts().get(0).text();
        String roleFromAddedContent = captor.getValue().getUserChatHistoryList().get(0).role();

        assertEquals(textFromAddedContent,"Hello" );
        assertEquals(roleFromAddedContent,"user" );

    }

    @Test
    void createUserChatHistoryIfNotExist() {
        long userId = 1L;

        GeminiHistoryService geminiHistoryService1 = spy(geminiHistoryService);
        //UserChatHistory exist
        when(chatHistoryRepository.existsById(anyLong())).thenReturn(true);

        geminiHistoryService1.createUserChatHistoryIfNotExist(userId);

        verify(chatHistoryRepository).existsById(anyLong());
        verify(geminiHistoryService1,never()).createUserChatHistory(anyLong());
        verify(geminiHistoryService1).createUserChatHistoryIfNotExist(userId);
        verifyNoMoreInteractions(geminiHistoryService1);


        //UserChatHistory doesn't exist
        when(chatHistoryRepository.existsById(anyLong())).thenReturn(false);

        geminiHistoryService1.createUserChatHistoryIfNotExist(userId);

        verify(chatHistoryRepository,times(2)).existsById(anyLong());
        verify(geminiHistoryService1).createUserChatHistory(anyLong());


    }

    @Test
    void clearHistory() {

        long userId = 1L;
        when(chatHistoryRepository.existsById(userId)).thenReturn(true);
        geminiHistoryService.clearHistory(userId);

        ArgumentCaptor<UserChatHistory> captor = ArgumentCaptor.forClass(UserChatHistory.class);
        verify(chatHistoryRepository).save(captor.capture());

        assertEquals(captor.getValue().userChatHistoryList.size(),0);
    }
}