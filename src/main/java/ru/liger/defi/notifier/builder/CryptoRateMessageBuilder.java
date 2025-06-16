package ru.liger.defi.notifier.builder;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.liger.defi.notifier.service.CryptoRateService;

import java.math.BigDecimal;
import java.util.Map;

@Service
@AllArgsConstructor
public class CryptoRateMessageBuilder {

    private final CryptoRateService rateService;

    public SendMessage buildMessage(Long chatId) {
        Map<String, BigDecimal> rates = rateService.getRates();


        String text = """
                📊 Актуальные курсы:
                BTC: $%.2f
                ETH: $%.2f
                """.formatted(rates.get("BTC"), rates.get("ETH"));

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode("Markdown");
        return message;
    }
}

