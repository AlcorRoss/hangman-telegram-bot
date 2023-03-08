package com.alcorross;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMessage {

    public void checkMessage(Message message) {
        Bot bot = new Bot();
        String chatId = message.getChatId().toString();
        boolean flag = message.getText().equals("/start") || message.getText().equals("Новая игра");
        boolean flag2 = Bot.getQUEUE().contains(chatId);

        if (flag && flag2) {
            bot.sendMessage(chatId, "Эй, сперва закончи текущий раунд!");
        } else if (flag) {
            if (Bot.getQUEUE().size() <= 20) {
                Bot.getQUEUE().add(chatId); //TODO добавить логирование, новый игрок, количество игроков
                new Thread(() -> bot.gameplay(chatId)).start();
            } else bot.sendMessage(chatId, "В настоящий момент превышено " +
                    "количество пользователей, возвращайся позднее!");
        } else if (flag2) {
            Bot.getMESSAGES().put(chatId, message.getText());
        }
    }

    public boolean checkCharacter(String character) {
        Pattern pattern = Pattern.compile("[а-яёА-ЯЁ]+");
        Matcher matcher = pattern.matcher(character);
        return character.length() == 1 && matcher.find();
    }
}
