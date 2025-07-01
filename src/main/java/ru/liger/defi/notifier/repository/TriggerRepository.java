package ru.liger.defi.notifier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liger.defi.notifier.entity.Trigger;

import java.util.List;
import java.util.UUID;

public interface TriggerRepository extends JpaRepository<Trigger, UUID> {

    List<Trigger> getTriggersByChatIdAndDeletedIsFalse(Long chatId);

    Trigger getTriggerById(UUID id);

}
