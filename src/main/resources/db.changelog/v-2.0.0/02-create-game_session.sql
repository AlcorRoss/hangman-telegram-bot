CREATE TABLE game_session
(
    chat_id        VARCHAR(32) REFERENCES user_statistic (chat_id) PRIMARY KEY,
    lose_counter   INT         NOT NULL,
    win_counter    INT         NOT NULL,
    word           VARCHAR(64) NOT NULL,
    used_character VARCHAR(64) NOT NULL,
    st             VARCHAR(64) NOT NULL
);