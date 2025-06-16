package ru.liger.defi.notifier.service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
@AllArgsConstructor
public class DatabaseInfoService {

    @PersistenceContext
    private final EntityManager entityManager;

    public SendMessage getTableNames(Long chatId) {
        String sql = """
            SELECT table_name
            FROM information_schema.tables
            WHERE table_schema = 'notifier'
              AND table_type = 'BASE TABLE'
        """;

        var query = entityManager.createNativeQuery(sql);
        var tables = query.getResultList();

        String text = tables.isEmpty()
                ? "❌ В базе данных нет таблиц."
                : "📦 Таблицы в базе данных:\n\n" + String.join("\n", tables);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        return message;
    }
}

