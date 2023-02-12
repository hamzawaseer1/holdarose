package com.holdarose.service.impl;

import com.holdarose.domain.Foundation;
import com.holdarose.repository.FoundationRepository;
import com.holdarose.service.FoundationService;
import com.holdarose.service.dto.FoundationDTO;
import com.holdarose.service.mapper.FoundationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Foundation}.
 */
@Service
public class FoundationServiceImpl implements FoundationService {

    private final Logger log = LoggerFactory.getLogger(FoundationServiceImpl.class);

    private final FoundationRepository foundationRepository;

    private final FoundationMapper foundationMapper;

    public FoundationServiceImpl(FoundationRepository foundationRepository, FoundationMapper foundationMapper) {
        this.foundationRepository = foundationRepository;
        this.foundationMapper = foundationMapper;
    }

    @Override
    public FoundationDTO save(FoundationDTO foundationDTO) {
        log.debug("Request to save Foundation : {}", foundationDTO);
        Foundation foundation = foundationMapper.toEntity(foundationDTO);
        foundation = foundationRepository.save(foundation);
        return foundationMapper.toDto(foundation);
    }

    @Override
    public FoundationDTO update(FoundationDTO foundationDTO) {
        log.debug("Request to save Foundation : {}", foundationDTO);
        Foundation foundation = foundationMapper.toEntity(foundationDTO);
        foundation = foundationRepository.save(foundation);
        return foundationMapper.toDto(foundation);
    }

    @Override
    public Optional<FoundationDTO> partialUpdate(FoundationDTO foundationDTO) {
        log.debug("Request to partially update Foundation : {}", foundationDTO);

        return foundationRepository
            .findById(foundationDTO.getId())
            .map(existingFoundation -> {
                foundationMapper.partialUpdate(existingFoundation, foundationDTO);

                return existingFoundation;
            })
            .map(foundationRepository::save)
            .map(foundationMapper::toDto);
    }

    @Override
    public Page<FoundationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Foundations");
        return foundationRepository.findAll(pageable).map(foundationMapper::toDto);
    }

    @Override
    public Optional<FoundationDTO> findOne(String id) {
        log.debug("Request to get Foundation : {}", id);
        return foundationRepository.findById(id).map(foundationMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Foundation : {}", id);
        foundationRepository.deleteById(id);
    }
}
