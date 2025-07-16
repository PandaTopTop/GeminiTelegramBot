package org.dz.prep.telegram_bot.googleai.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;

import java.util.List;

@Converter
public class UserChatHistoryConverter implements AttributeConverter<List<Content>,String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Content> userChatHistory) {

        try {
            return   objectMapper.writeValueAsString(userChatHistory);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting content list to JSON string", e);
        }

    }

    @Override
    public List<Content> convertToEntityAttribute(String dbData) {

        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON string to content list", e);
        }
    }
}
