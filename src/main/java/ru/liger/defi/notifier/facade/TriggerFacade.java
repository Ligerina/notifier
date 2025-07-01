package ru.liger.defi.notifier.facade;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.liger.defi.notifier.model.PositionStep;
import ru.liger.defi.notifier.model.TriggerDto;
import ru.liger.defi.notifier.service.TriggerService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@AllArgsConstructor
public class TriggerFacade {

    Map<Long, PositionStep> userSteps = new ConcurrentHashMap<>();
    Map<Long, TriggerDto> userTriggers = new ConcurrentHashMap<>();

    private final TriggerService triggerService;

    public SendMessage startTriggerCreation(Long chatId, String callbackData) {
        // 1. Устанавливаем первый шаг
        userSteps.put(chatId, PositionStep.POSITION_NAME);

        // 2. Создаём заготовку объекта TriggerDto
        TriggerDto dto = new TriggerDto();
        userTriggers.put(chatId, dto);

        // 3. Возвращаем сообщение пользователю
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("🆕 Начинаем создание триггера.\nВведите *название позиции*:");
        message.setParseMode("Markdown");

        return message;
    }


    public SendMessage processTriggerCreation(Long chatId, String input) {
        PositionStep step = userSteps.get(chatId);
        TriggerDto dto = userTriggers.get(chatId);

        String responseText;

        try {
            switch (step) {
                case POSITION_NAME -> {
                    dto.setPositionName(input);
                    userSteps.put(chatId, PositionStep.ASSET_NAME);
                    responseText = "Введите *название волатильного актива* (`BTC` или `ETH`):";
                }
                case ASSET_NAME -> {
                    String asset = input.trim().toUpperCase();
                    if (!asset.equals("BTC") && !asset.equals("ETH")) {
                        responseText = "❌ Введите *только* `BTC` или `ETH`.";
                        break;
                    }
                    dto.setAssetName(asset);
                    userSteps.put(chatId, PositionStep.UPPER_BOUND);
                    responseText = "Введите *верхнюю границу* (число):";
                }
                case UPPER_BOUND -> {
                    dto.setUpperBound(new BigDecimal(input));
                    userSteps.put(chatId, PositionStep.LOWER_BOUND);
                    responseText = "Введите *нижнюю границу* (число):";
                }
                case LOWER_BOUND -> {
                    dto.setLowerBound(new BigDecimal(input));
                    triggerService.saveTrigger(chatId, dto);
                    responseText = "✅ Позиция успешно создана!";
                    userSteps.remove(chatId);
                    userTriggers.remove(chatId);
                }
                default -> responseText = "❌ Неизвестный шаг.";
            }
        } catch (NumberFormatException e) {
            responseText = "❌ Введите корректное число.";
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(responseText);
        message.setParseMode("Markdown");
        return message;
    }

    public boolean userAddingTrigger(Long chatId) {
        return userSteps.containsKey(chatId);
    }

    public SendMessage getAllTriggersByChatId(Long chatId) {
        var triggers = triggerService.getAllTriggers(chatId);

        String text;
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        if (triggers.isEmpty()) {
            text = "📭 У вас пока нет активных триггеров.";
        } else {
            StringBuilder sb = new StringBuilder("📌 *Ваши активные триггеры:*\n\n");

            int index = 1;
            for (TriggerDto trigger : triggers) {
                sb.append(index).append(". ")
                        .append("*").append(trigger.getPositionName()).append("* — ")
                        .append(trigger.getAssetName()).append("\n")
                        .append("🔼 Верхняя граница: ").append(trigger.getUpperBound()).append("\n")
                        .append("🔽 Нижняя граница: ").append(trigger.getLowerBound()).append("\n\n");

                // 🗑 Кнопка "Удалить"
                InlineKeyboardButton deleteButton = new InlineKeyboardButton();
                deleteButton.setText("🗑 Удалить " + index);
                deleteButton.setCallbackData("DELETE_TRIGGER_" + trigger.getId());

                keyboard.add(List.of(deleteButton));
                index++;
            }

            text = sb.toString();
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode("Markdown");

        if (!keyboard.isEmpty()) {
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(keyboard);
            message.setReplyMarkup(markup);
        }

        return message;
    }


    public void deleteTriggerById(UUID triggerId) {
        triggerService.deleteTriggerById(triggerId);
    }
}