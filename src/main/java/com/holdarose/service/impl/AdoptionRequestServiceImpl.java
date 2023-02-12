package com.holdarose.service.impl;

import com.holdarose.domain.AdoptionRequest;
import com.holdarose.domain.User;
import com.holdarose.repository.AdoptionRequestRepository;
import com.holdarose.repository.ChildRepository;
import com.holdarose.repository.FoundationRepository;
import com.holdarose.service.AdoptionRequestService;
import com.holdarose.service.UserService;
import com.holdarose.service.dto.AdoptionRequestDTO;
import com.holdarose.service.mapper.AdoptionRequestMapper;
import com.holdarose.service.mapper.ChildMapper;
import com.holdarose.service.mapper.FoundationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AdoptionRequest}.
 */
@Service
public class AdoptionRequestServiceImpl implements AdoptionRequestService {

    private final Logger log = LoggerFactory.getLogger(AdoptionRequestServiceImpl.class);

    private final AdoptionRequestRepository adoptionRequestRepository;

    private final AdoptionRequestMapper adoptionRequestMapper;

    private final ChildServiceImpl childService;

    private final UserService userService;

    public AdoptionRequestServiceImpl(AdoptionRequestRepository adoptionRequestRepository, AdoptionRequestMapper adoptionRequestMapper, FoundationRepository foundationRepository, FoundationMapper foundationMapper, ChildRepository childRepository, ChildMapper childMapper, ChildServiceImpl childService, UserService userService) {
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.adoptionRequestMapper = adoptionRequestMapper;
        this.childService = childService;
        this.userService = userService;
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
    public Page<AdoptionRequestDTO> findAll(Pageable pageable, Principal principal) {
        log.debug("Request to get all AdoptionRequests");
        User userFromPrincipal = userService.getUserFromPrincipal(principal);
        String foundationName = userFromPrincipal.getLogin();
        List<AdoptionRequestDTO> adoptionRequestDTO = adoptionRequestRepository.findAdoptionRequestByApprovedFalseAndFoundationName(foundationName)
            .stream().map(adoptionRequestMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(adoptionRequestDTO, pageable, adoptionRequestDTO.size());
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
