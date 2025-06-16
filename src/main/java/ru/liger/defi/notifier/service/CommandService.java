package ru.liger.defi.notifier.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liger.defi.notifier.builder.CryptoRateMessageBuilder;

@Slf4j
@Service
@AllArgsConstructor
public class CommandService {

    private final CryptoRateMessageBuilder cryptoRateMessageBuilder;

    public SendMessage handleMessage(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        return switch (callbackData) {
            case "SHOW_PRICES" -> cryptoRateMessageBuilder.buildMessage(chatId);
            // можно добавлять ещё callback'и тут
            default -> {
                SendMessage message = new SendMessage();
                message.setChatId(chatId.toString());
                message.setText("Неизвестное действие.");
                yield message;
            }
        };
    }

}
