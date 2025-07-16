DROP TABLE IF EXISTS public.user_chat_history;

CREATE TABLE IF NOT EXISTS public.user_chat_history
(
    user_id bigint NOT NULL,
    chat_history json,
    CONSTRAINT user_chat_history_pkey PRIMARY KEY (user_id)
    )