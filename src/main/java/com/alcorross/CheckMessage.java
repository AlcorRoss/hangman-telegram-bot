package com.alcorross;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CheckMessage {

    private static CheckMessage checkMessageInstance;

    private CheckMessage() {
    }

    public static CheckMessage getCheckMessageInstance() {
        if (checkMessageInstance == null) checkMessageInstance = new CheckMessage();
        return checkMessageInstance;
    }

    public void checkMessage(Message message) {
        Bot bot = Bot.getBotInstance();
        String chatId = message.getChatId().toString();
        boolean flag = message.getText().equals("/start") || message.getText().equals("Новая игра");
        boolean flag2 = Bot.getCURRENT_SESSIONS().contains(chatId);

        if (flag && flag2) {
            bot.sendMessage(chatId, "Эй, сперва закончи текущий раунд!", null);
        } else if (flag) {
            synchronized (Bot.getCURRENT_SESSIONS()) {
                if (Bot.getCURRENT_SESSIONS().size() <= 20) {
                    Bot.getCURRENT_SESSIONS().add(chatId);
                    log.info("Start new game. Quantity of players: " + Bot.getCURRENT_SESSIONS().size());
                    new Thread(() -> Gameplay.getGameplayInstance().gameplay(chatId)).start();
                } else {
                    bot.sendMessage(chatId, "В настоящий момент превышено " +
                            "количество пользователей, возвращайся позднее!", null);
                    log.info("Failed to start a new game. Exceeded the number of players." +
                            " Quantity of players: " + Bot.getCURRENT_SESSIONS().size());
                }
            }
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
