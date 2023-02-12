package com.holdarose.service.mapper;

import com.holdarose.domain.Child;
import com.holdarose.domain.Fosters;
import com.holdarose.service.dto.ChildDTO;
import com.holdarose.service.dto.FostersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fosters} and its DTO {@link FostersDTO}.
 */
@Mapper(componentModel = "spring")
public interface FostersMapper extends EntityMapper<FostersDTO, Fosters> {
    @Mapping(target = "child", source = "child", qualifiedByName = "childId")
    FostersDTO toDto(Fosters s);

    @Named("childId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChildDTO toDtoChildId(Child child);
}
