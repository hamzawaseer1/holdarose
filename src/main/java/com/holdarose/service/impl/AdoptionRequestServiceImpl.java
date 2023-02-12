package com.holdarose.service.impl;

import com.holdarose.domain.AdoptionRequest;
import com.holdarose.repository.AdoptionRequestRepository;
import com.holdarose.service.AdoptionRequestService;
import com.holdarose.service.dto.AdoptionRequestDTO;
import com.holdarose.service.mapper.AdoptionRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link AdoptionRequest}.
 */
@Service
public class AdoptionRequestServiceImpl implements AdoptionRequestService {

    private final Logger log = LoggerFactory.getLogger(AdoptionRequestServiceImpl.class);

    private final AdoptionRequestRepository adoptionRequestRepository;

    private final AdoptionRequestMapper adoptionRequestMapper;

    public AdoptionRequestServiceImpl(AdoptionRequestRepository adoptionRequestRepository, AdoptionRequestMapper adoptionRequestMapper) {
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.adoptionRequestMapper = adoptionRequestMapper;
    }

    @Override
    public AdoptionRequestDTO save(AdoptionRequestDTO adoptionRequestDTO) {
        log.debug("Request to save AdoptionRequest : {}", adoptionRequestDTO);
        AdoptionRequest adoptionRequest = adoptionRequestMapper.toEntity(adoptionRequestDTO);
        adoptionRequest = adoptionRequestRepository.save(adoptionRequest);
        return adoptionRequestMapper.toDto(adoptionRequest);
    }

    @Override
    public AdoptionRequestDTO update(AdoptionRequestDTO adoptionRequestDTO) {
        log.debug("Request to save AdoptionRequest : {}", adoptionRequestDTO);
        AdoptionRequest adoptionRequest = adoptionRequestMapper.toEntity(adoptionRequestDTO);
        adoptionRequest = adoptionRequestRepository.save(adoptionRequest);
        return adoptionRequestMapper.toDto(adoptionRequest);
    }

    @Override
    public Optional<AdoptionRequestDTO> partialUpdate(AdoptionRequestDTO adoptionRequestDTO) {
        log.debug("Request to partially update AdoptionRequest : {}", adoptionRequestDTO);

        return adoptionRequestRepository
            .findById(adoptionRequestDTO.getId())
            .map(existingAdoptionRequest -> {
                adoptionRequestMapper.partialUpdate(existingAdoptionRequest, adoptionRequestDTO);

                return existingAdoptionRequest;
            })
            .map(adoptionRequestRepository::save)
            .map(adoptionRequestMapper::toDto);
    }

    @Override
    public Page<AdoptionRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdoptionRequests");
        return adoptionRequestRepository.findAll(pageable).map(adoptionRequestMapper::toDto);
    }

    @Override
    public Optional<AdoptionRequestDTO> findOne(String id) {
        log.debug("Request to get AdoptionRequest : {}", id);
        return adoptionRequestRepository.findById(id).map(adoptionRequestMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete AdoptionRequest : {}", id);
        adoptionRequestRepository.deleteById(id);
    }
}
