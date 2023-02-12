package com.holdarose.service.mapper;

import com.holdarose.domain.Donation;
import com.holdarose.domain.Foundation;
import com.holdarose.service.dto.DonationDTO;
import com.holdarose.service.dto.FoundationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Donation} and its DTO {@link DonationDTO}.
 */
@Mapper(componentModel = "spring")
public interface DonationMapper extends EntityMapper<DonationDTO, Donation> {
    @Mapping(target = "foundation", source = "foundation", qualifiedByName = "foundationId")
    DonationDTO toDto(Donation s);

    @Named("foundationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FoundationDTO toDtoFoundationId(Foundation foundation);
}
