package com.holdarose.service.mapper;

import com.holdarose.domain.Foundation;
import com.holdarose.service.dto.FoundationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Foundation} and its DTO {@link FoundationDTO}.
 */
@Mapper(componentModel = "spring")
public interface FoundationMapper extends EntityMapper<FoundationDTO, Foundation> {}
