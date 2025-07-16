package org.dz.prep.telegram_bot.googleai;

import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;
import org.dz.prep.telegram_bot.googleai.geminiRecord.GeminiRequest;
import org.dz.prep.telegram_bot.googleai.geminiRecord.GeminiResponse;
import org.dz.prep.telegram_bot.googleai.geminiRecord.TextPart;
import org.dz.prep.telegram_bot.googleai.history.GeminiHistoryService;
import org.dz.prep.telegram_bot.googleai.history.UserChatHistory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiServiceTest {
    @Mock
    public GeminiClient geminiClient;

    @Mock
    public GeminiHistoryService geminiHistory;

    @Spy
    @InjectMocks
    public GeminiService service;

    final static long USER_ID = 1L;

    final static String EXPECTED_TEXT_FROM_REQUEST  = "This is a test";
    final static String EXPECTED_FINAL_RESULT_TEXT = "Answer";


    static GeminiResponse geminiResponse;

    @BeforeAll
    static void setUpTestData() {
        geminiResponse = new GeminiResponse(
                List.of(new GeminiResponse.Candidate(
                        new Content(
                                List.of(new TextPart(EXPECTED_TEXT_FROM_REQUEST)),
                                "admin"),
                        "",
                        0L))
                , new GeminiResponse.UsageMetaData(0, 0, 0),
                ""
        );
    }

    @BeforeEach
    public void setUpMocks() {

        when(geminiClient.getGeminiResponse(any())).thenReturn(geminiResponse);
        when(geminiHistory.getUserChatHistory(anyLong())).thenReturn(new UserChatHistory(USER_ID, new ArrayList<>()));

    }

    @Test
    public void getChatResponse_ShouldInvokeCreateUserChatHistory(){

        service.getChatResponse(USER_ID, EXPECTED_FINAL_RESULT_TEXT);

        verify(geminiHistory).createUserChatHistoryIfNotExist(USER_ID);

    }

    @Test
    public void getChatResponse_ShouldInvokeAddContentUserChatHistoryWithExpectedArguments() {

        service.getChatResponse(USER_ID, EXPECTED_FINAL_RESULT_TEXT);

        ArgumentCaptor<Content> argumentCaptor = ArgumentCaptor.forClass(Content.class);
        verify(geminiHistory, times(2)).addContentUserChatHistory(eq(USER_ID), argumentCaptor.capture());


        String actualTextFromHistory = argumentCaptor.getAllValues().get(0).parts().get(0).text();
        assertEquals(actualTextFromHistory, EXPECTED_FINAL_RESULT_TEXT);


        verify(geminiHistory).addContentUserChatHistory(USER_ID, geminiResponse.candidates().get(0).content());
    }


    @Test
    public void getChatResponse_ShouldInvokeGetGeminiResponse() {

        service.getChatResponse(USER_ID, EXPECTED_FINAL_RESULT_TEXT);
        verify(geminiClient).getGeminiResponse(any(GeminiRequest.class));
    }


    @Test
    public void getChatResponse_ShouldReturnExpectedResult() {

        String finalResult = service.getChatResponse(USER_ID, EXPECTED_FINAL_RESULT_TEXT);
        assertNotNull(finalResult);
        assertEquals(finalResult, EXPECTED_TEXT_FROM_REQUEST);
    }


    public void getChatResponseTest() {
//
////        service = spy(service);
//
//
//       GeminiResponse geminiResponse = new GeminiResponse(
//                            List.of(new GeminiResponse.Candidate(
//                                    new Content(
//                                            List.of(new TextPart("answer")),
//                                            "admin"),
//                                    "",
//                                    0L))
//               , new GeminiResponse.UsageMetaData(0,0,0),
//               ""
//       );
//
//       when(geminiClient.getGeminiResponse(any())).thenReturn(geminiResponse);
//when(geminiHistory.getUserChatHistory(userId)).thenReturn(new UserChatHistory(userId,List.of()));
//
//
//
//
//
//       String finalResult = service.getChatResponse(userId,expectedText);
//
//        verify(geminiHistory).createUserChatHistoryIfNotExist(userId);
//
//        ArgumentCaptor<Content> argumentCaptor = ArgumentCaptor.forClass(Content.class);
//        verify(geminiHistory,times(2)).addContentUserChatHistory(eq(userId),argumentCaptor.capture());
//
//
//
//        String actualTextFromHistory = argumentCaptor.getAllValues().get(0).parts().get(0).text();
//        assertEquals(actualTextFromHistory, expectedText );
//
//
//        verify(geminiClient).getGeminiResponse(any(GeminiRequest.class));
//
//
//        verify(geminiHistory).addContentUserChatHistory(userId,geminiResponse.candidates().get(0).content());
//
//      assertEquals("answer",finalResult);
//
//
//verify(service).getChatResponse(anyLong(),any());
//
//
//
//    }

    }

}