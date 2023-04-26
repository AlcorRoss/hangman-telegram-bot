package com.alcorross.model;

import lombok.Builder;

@Builder
public class UserStatistic {
    private String chatId;
    private int numberOfWins;
    private int numberOfLoses;

    @Override
    public String toString() {
        return """
                Побед: %s
                Поражений: %s
                """.formatted(numberOfWins, numberOfLoses);
    }
}
