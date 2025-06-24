package ru.liger.defi.notifier.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liger.defi.notifier.mapper.TriggerMapper;
import ru.liger.defi.notifier.model.TriggerDto;
import ru.liger.defi.notifier.repository.TriggerRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TriggerService {

    private final TriggerMapper triggerMapper;
    private final TriggerRepository triggerRepository;
    @Transactional(readOnly = true)
    public List<TriggerDto> getAllTriggers(Long chatId) {
        return triggerRepository.getTriggersByChatId(chatId)
                .stream().map(triggerMapper::toDto)
                .toList();
    }

    @Transactional
    public void saveTrigger(Long chatId, TriggerDto dto) {
        var trigger = triggerMapper.toEntity(dto, chatId);
        triggerRepository.save(trigger);
    }
}
