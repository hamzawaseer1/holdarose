package com.holdarose.service.mapper;

import com.holdarose.domain.AdoptionRequest;
import com.holdarose.domain.Child;
import com.holdarose.service.dto.AdoptionRequestDTO;
import com.holdarose.service.dto.ChildDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdoptionRequest} and its DTO {@link AdoptionRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdoptionRequestMapper extends EntityMapper<AdoptionRequestDTO, AdoptionRequest> {
    @Mapping(target = "child", source = "child", qualifiedByName = "childId")
    AdoptionRequestDTO toDto(AdoptionRequest s);

    @Named("childId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChildDTO toDtoChildId(Child child);
}
