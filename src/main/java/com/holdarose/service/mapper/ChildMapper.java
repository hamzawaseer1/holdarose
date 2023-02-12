package com.holdarose.service.mapper;

import com.holdarose.domain.Child;
import com.holdarose.domain.Foundation;
import com.holdarose.service.dto.ChildDTO;
import com.holdarose.service.dto.FoundationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Child} and its DTO {@link ChildDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChildMapper extends EntityMapper<ChildDTO, Child> {
    @Mapping(target = "foundation", source = "foundation", qualifiedByName = "foundationId")
    ChildDTO toDto(Child s);

    @Named("foundationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FoundationDTO toDtoFoundationId(Foundation foundation);
}
