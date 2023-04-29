CREATE TABLE user_statistic
(
    chat_id         VARCHAR(32) PRIMARY KEY,
    number_of_wins  INT NOT NULL,
    number_of_loses INT NOT NULL
);