package ru.liger.defi.notifier.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class KeyBoardService {

    public SendMessage createMainMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        // Центрировать текст можно только "визуально", добавив пробелы или Markdown (курсив, жирный и т.д.)
        message.setText("*Выбери действие*");
        message.setParseMode("Markdown"); // для форматирования текста

        // Создаем две кнопки — каждая в отдельной строке (каждая в своём List)
        InlineKeyboardButton viewTriggersButton = new InlineKeyboardButton();
        viewTriggersButton.setText("Посмотреть список активных тригеров");
        viewTriggersButton.setCallbackData("VIEW_TRIGGERS");

        InlineKeyboardButton addTriggerButton = new InlineKeyboardButton();
        addTriggerButton.setText("Добавить тригер");
        addTriggerButton.setCallbackData("ADD_TRIGGER");

        // Каждая кнопка — отдельная строка (отдельный список)
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(viewTriggersButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(addTriggerButton);

        rows.add(row1);
        rows.add(row2);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(rows);

        message.setReplyMarkup(keyboardMarkup);

        return message;
    }


}
