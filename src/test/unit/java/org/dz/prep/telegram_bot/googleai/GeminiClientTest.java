package org.dz.prep.telegram_bot.googleai;

import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;
import org.dz.prep.telegram_bot.googleai.geminiRecord.GeminiRequest;
import org.dz.prep.telegram_bot.googleai.geminiRecord.GeminiResponse;
import org.dz.prep.telegram_bot.googleai.geminiRecord.TextPart;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GeminiClientTest {

    private String tokenMock = "token";

    @Mock
    RestTemplate restTemplate;

    GeminiClient geminiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    geminiClient =new GeminiClient(tokenMock, restTemplate);
    }


    public static Object[] testData(){
      GeminiRequest geminiRequest =  GeminiRequest.builder().build();

      GeminiResponse geminiResponse1 =  new GeminiResponse(
              List.of(new GeminiResponse.Candidate(
                        new Content(
                                List.of(
                                        new TextPart("Answer")),
                                "user"),
                      "",0L)),
              new GeminiResponse.UsageMetaData(0, 0, 0),
              "");

      GeminiResponse geminiResponse2 =  new GeminiResponse(
              List.of(new GeminiResponse.Candidate(
                        new Content(
                                List.of(
                                        new TextPart("Hello")),
                                "user"),
                      "",0L)),
              new GeminiResponse.UsageMetaData(0, 0, 0),
              "");


        return new Object[]{
                new Object[]{  geminiRequest, geminiResponse1, "Answer"
                },
                new Object[]{geminiRequest, geminiResponse2, "Hello"
                }

        };
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void getGeminiResponseTest(GeminiRequest geminiRequest,GeminiResponse expectedGeminiResponse,String expectedAnswer) {



        when(restTemplate.postForObject(anyString(), any(HttpEntity.class),eq( GeminiResponse.class))).thenReturn(expectedGeminiResponse);



        GeminiResponse actualGeminiResponse = geminiClient.getGeminiResponse(geminiRequest);

        assertNotNull(actualGeminiResponse);
        String actualText = actualGeminiResponse.candidates().get(0).content().parts().get(0).text();
        assertEquals(actualText,
                expectedAnswer,
                "The response text does not match the expected value");


    }

}