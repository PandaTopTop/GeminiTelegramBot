package org.dz.prep.telegram_bot.googleai.geminiRecord;

import lombok.Builder;

import java.util.List;
@Builder
public record Content(List<TextPart> parts, String role) {
}
