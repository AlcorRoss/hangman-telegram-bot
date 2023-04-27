package com.alcorross.services.commands;

import com.alcorross.dao.UserStatisticDao;
import com.alcorross.model.Bot;
import com.alcorross.services.Keyboard;

public class MyStatistic implements Command {

    private static MyStatistic instance;
    Bot bot = Bot.getInstance();
    Keyboard keyboard = Keyboard.getInstance();
    UserStatisticDao userStatisticDao = UserStatisticDao.getInstance();

    private MyStatistic() {
    }

    public static MyStatistic getInstance() {
        if (instance == null) instance = new MyStatistic();
        return instance;
    }

    @Override
    public void execute(String chatId) {
        var optUserStat = userStatisticDao.getByChatId(chatId);
        optUserStat.ifPresentOrElse(
                userStatistic -> bot
                        .sendMessage(chatId, userStatistic.toString(), keyboard.getNewGameKeyboard()),
                () -> bot
                        .sendMessage(chatId, "У Вас пока нет статистики", keyboard.getNewGameKeyboard())
        );
    }
}
