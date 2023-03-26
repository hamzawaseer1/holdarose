package com.holdarose.service.impl;

import com.holdarose.domain.AdoptionRequest;
import com.holdarose.domain.Child;
import com.holdarose.domain.Fosters;
import com.holdarose.domain.User;
import com.holdarose.domain.enumeration.Status;
import com.holdarose.repository.AdoptionRequestRepository;
import com.holdarose.repository.ChildRepository;
import com.holdarose.repository.FostersRepository;
import com.holdarose.service.AdoptionRequestService;
import com.holdarose.service.UserService;
import com.holdarose.service.dto.AdoptionRequestDTO;
import com.holdarose.service.mapper.AdoptionRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AdoptionRequest}.
 */
@Service
public class AdoptionRequestServiceImpl implements AdoptionRequestService {

    private final Logger log = LoggerFactory.getLogger(AdoptionRequestServiceImpl.class);

    private final AdoptionRequestRepository adoptionRequestRepository;

    private final AdoptionRequestMapper adoptionRequestMapper;

    private final ChildRepository childRepository;

    private final UserService userService;

    private final FostersRepository fostersRepository;

    public AdoptionRequestServiceImpl(AdoptionRequestRepository adoptionRequestRepository,
                                      AdoptionRequestMapper adoptionRequestMapper,
                                      ChildRepository childRepository,
                                      UserService userService,
                                      FostersRepository fostersRepository) {
        this.adoptionRequestRepository = adoptionRequestRepository;
        this.adoptionRequestMapper = adoptionRequestMapper;
        this.childRepository = childRepository;
        this.userService = userService;
        this.fostersRepository = fostersRepository;
    }

    @Override
    public AdoptionRequestDTO save(AdoptionRequestDTO adoptionRequestDTO) {
        log.debug("Request to save AdoptionRequest : {}", adoptionRequestDTO);
        Optional<Child> childFromDB = childRepository.findById(adoptionRequestDTO.getChild().getId());
        if (childFromDB.isPresent()) {
            childFromDB.get().setStatus(Status.OCCUPIED);
            childRepository.save(childFromDB.get());
        }
        AdoptionRequest adoptionRequest = adoptionRequestMapper.toEntity(adoptionRequestDTO);
        adoptionRequest = adoptionRequestRepository.save(adoptionRequest);
        return adoptionRequestMapper.toDto(adoptionRequest);
    }

    @Override
    public AdoptionRequestDTO update(AdoptionRequestDTO adoptionRequestDTO) {
        log.debug("Request to save AdoptionRequest : {}", adoptionRequestDTO);
        Optional<Child> childFromDb = childRepository.findById(adoptionRequestDTO.getChild().getId());
        if (childFromDb.isPresent() && Boolean.TRUE.equals(adoptionRequestDTO.getApproved())) {
            childFromDb.get().setStatus(Status.ADOPTED);
            childRepository.save(childFromDb.get());
            Fosters foster = new Fosters();
            foster.setName(adoptionRequestDTO.getFosterName());
            foster.setCnic(adoptionRequestDTO.getCnic());
            foster.setId(UUID.randomUUID().toString());
            foster.setLocation(adoptionRequestDTO.getFoundationName());
            foster.setJobTitle(adoptionRequestDTO.getFosterJobTitle());
            foster.setChild(childFromDb.get());
            fostersRepository.save(foster);
        }
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
        Optional<AdoptionRequest> byId = adoptionRequestRepository.findById(id);
        if (byId.isPresent()) {
            Child child = byId.get().getChild();
            child.setStatus(Status.AVAILABLE);
            childRepository.save(child);
            adoptionRequestRepository.deleteById(id);
        }
    }
}
