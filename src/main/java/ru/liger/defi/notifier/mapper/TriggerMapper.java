package ru.liger.defi.notifier.mapper;

import org.mapstruct.*;
import ru.liger.defi.notifier.entity.Trigger;
import ru.liger.defi.notifier.model.TriggerDto;

@Mapper(componentModel = "spring")
public interface TriggerMapper {

    @Mapping(target = "chatId", source = "chatId")
    Trigger toEntity(TriggerDto dto, Long chatId);

    TriggerDto toDto(Trigger dto);
}