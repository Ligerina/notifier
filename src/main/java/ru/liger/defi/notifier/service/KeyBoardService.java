package ru.liger.defi.notifier.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public SendMessage createAppInfoMessage(Long chatId) {
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");

        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        int cpuCores = runtime.availableProcessors();

        long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        Duration uptime = Duration.ofMillis(uptimeMillis);
        String formattedUptime = formatDuration(uptime);

        String containerId = Optional.ofNullable(System.getenv("HOSTNAME")).orElse("неизвестно");

        String ip = "неизвестно";
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch (Exception ignored) {
        }

        String text = """
                🖥 *Информация о приложении:*

                🧩 Java: %s
                🖥 OS: %s %s
                📦 Контейнер ID: `%s`
                📅 Аптайм: %s
                💾 Память: %d MB / %d MB
                🧠 CPU: %d ядер
                🌐 IP: %s
                """.formatted(
                javaVersion, osName, osArch,
                containerId,
                formattedUptime,
                usedMemory, maxMemory,
                cpuCores,
                ip
        );

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode("Markdown");

        return message;
    }

    private static String formatDuration(Duration d) {
        long hours = d.toHours();
        long minutes = d.toMinutesPart();
        long seconds = d.toSecondsPart();
        return "%d ч %d мин %d сек".formatted(hours, minutes, seconds);
    }


}
