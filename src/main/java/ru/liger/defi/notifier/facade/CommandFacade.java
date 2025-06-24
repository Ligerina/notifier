package ru.liger.defi.notifier.facade;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liger.defi.notifier.builder.CryptoRateMessageBuilder;

@Slf4j
@Service
@AllArgsConstructor
public class CommandFacade {

    private final CryptoRateMessageBuilder cryptoRateMessageBuilder;
    private final TriggerFacade triggerFacade;


    public SendMessage handleMessage(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        return switch (callbackData) {
            case "SHOW_PRICES" -> cryptoRateMessageBuilder.buildMessage(chatId);
            case "ADD_TRIGGER" -> triggerFacade.startTriggerCreation(chatId, callbackData);
            case "VIEW_TRIGGERS" -> triggerFacade.getAllTriggersByChatId(chatId);
            default -> {
                SendMessage message = new SendMessage();
                message.setChatId(chatId.toString());
                message.setText("Неизвестное действие.");
                yield message;
            }
        };
    }

}
