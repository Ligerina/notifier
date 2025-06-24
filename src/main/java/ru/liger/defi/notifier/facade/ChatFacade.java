package ru.liger.defi.notifier.facade;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liger.defi.notifier.service.DatabaseInfoService;
import ru.liger.defi.notifier.service.KeyBoardService;

@Slf4j
@Service
@AllArgsConstructor
public class ChatFacade {

    private final KeyBoardService keyBoardService;
    private final DatabaseInfoService databaseInfoService;
    private final TriggerFacade commandService;

    public SendMessage handleMessage(Update update) {
        var chatId = update.getMessage().getChatId();
        var input = update.getMessage().getText();

        if (commandService.userAddingTrigger(chatId)) {
            return commandService.processTriggerCreation(chatId, input);
        }

        switch (update.getMessage().getText()) {
            case "/start":
                return keyBoardService.createWelcomeMessageWithKeyboard(chatId, "Привет! И успехов тебе с ликвидными пулами");
            case "/menu":
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