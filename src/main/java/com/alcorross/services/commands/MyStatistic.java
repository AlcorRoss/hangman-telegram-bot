package com.alcorross.services.commands;

import com.alcorross.dao.UserStatisticDao;
import com.alcorross.model.Bot;
import com.alcorross.model.GameSession;
import com.alcorross.services.Gameplay;
import com.alcorross.services.Keyboard;

import java.util.Optional;

public class MyStatistic implements Command {

    private static MyStatistic instance;
    Bot bot = Bot.getInstance();
    Keyboard keyboard = Keyboard.getInstance();
    UserStatisticDao userStatisticDao = UserStatisticDao.getInstance();
    Gameplay gameplay = Gameplay.getInstance();

    private MyStatistic() {
    }

    public static MyStatistic getInstance() {
        if (instance == null) instance = new MyStatistic();
        return instance;
    }

    @Override
    public void execute(String chatId) {
        Optional<GameSession> optionalGameSession = Optional.ofNullable(gameplay.getCurrentSessions().get(chatId));

        optionalGameSession.ifPresentOrElse(
                gameSession -> bot.sendMessage(chatId, "Эй, сперва закончите текущий раунд!",
                        keyboard.getKeyboard(gameSession.getUsedCharacter())),
                () -> {
                    var optUserStat = userStatisticDao.getByChatId(chatId);
                    optUserStat.ifPresentOrElse(
                            userStatistic -> bot
                                    .sendMessage(chatId, userStatistic.toString(), keyboard.getNewGameKeyboard()),
                            () -> bot
                                    .sendMessage(chatId, "У Вас пока нет статистики",
                                            keyboard.getNewGameKeyboard()));
                }
        );
    }
}






















