package ru.liger.defi.notifier.facade;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liger.defi.notifier.config.BotConfig;
import ru.liger.defi.notifier.service.ChatService;

@Slf4j
@Service
@AllArgsConstructor
public class TelegramBotFacade extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final ChatService chatService;

    @Override
    public void onUpdateReceived(Update update) {
        log.info("update received - {}", update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            var response =  chatService.handleMessage(update);
            sendMessage(response);
        }

    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    private void sendMessage(SendMessage response) {
        try {
            execute(response);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
