package org.dz.prep.telegram_bot.googleai.history;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Type;

import java.util.List;
@Entity
@Table(name = "user_chat_history")
@AllArgsConstructor
@NoArgsConstructor
@Data

public class UserChatHistory  {

    @Id Long userId;


    @Convert(converter = UserChatHistoryConverter.class)
    @Column(name = "chat_history", columnDefinition = "JSON")
    @ColumnTransformer(write = "?::json")
    List<Content> userChatHistoryList;

}
