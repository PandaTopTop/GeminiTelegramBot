package org.dz.prep.telegram_bot.googleai.history;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.dz.prep.telegram_bot.googleai.geminiRecord.Content;

import java.util.List;

public record UserChatHistory(List<Content> userChatHistory)  {


}
