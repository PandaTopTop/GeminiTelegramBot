package org.dz.prep.telegram_bot.googleai.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatHistoryRepository extends JpaRepository<UserChatHistory,Long> {

}
