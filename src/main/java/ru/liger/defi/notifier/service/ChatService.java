package ru.liger.defi.notifier.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
@AllArgsConstructor
public class ChatService {

    private final KeyBoardService keyBoardService;
    private final DatabaseInfoService databaseInfoService;

    public SendMessage handleMessage(Update update) {
        var chatId = update.getMessage().getChatId();

        switch (update.getMessage().getText()) {
            case "/start":
                return keyBoardService.createMainMenu(chatId);
            case "/version":
                return keyBoardService.createAppInfoMessage(chatId);
            case "/db_data":
                return databaseInfoService.getTableNames(chatId);
            default:
                var response = new SendMessage();
                response.setChatId(chatId.toString());
                response.setText("Такой команды нет");
                return response;
        }
    }
}