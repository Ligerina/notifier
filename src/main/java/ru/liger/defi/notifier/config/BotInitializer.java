package ru.liger.defi.notifier.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liger.defi.notifier.entrypoint.TelegramBotEntryPoint;

@Component
@AllArgsConstructor
@Slf4j
public class BotInitializer {
    private final TelegramBotEntryPoint telegramBotFacade;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        var telegramBotApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotApi.registerBot(telegramBotFacade);
        } catch (Exception exception) {
            log.error("catch error on initlz: {}", exception.getMessage());
        }
    }
}
