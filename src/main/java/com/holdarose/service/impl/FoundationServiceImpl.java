package com.holdarose.service.impl;

import com.holdarose.domain.Authority;
import com.holdarose.domain.Foundation;
import com.holdarose.domain.User;
import com.holdarose.repository.FoundationRepository;
import com.holdarose.security.AuthoritiesConstants;
import com.holdarose.service.FoundationService;
import com.holdarose.service.KeycloakService;
import com.holdarose.service.UserService;
import com.holdarose.service.dto.FoundationDTO;
import com.holdarose.service.mapper.FoundationMapper;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    private final KeycloakService keycloakService;

    private final UserService userService;

    public FoundationServiceImpl(FoundationRepository foundationRepository, FoundationMapper foundationMapper, KeycloakService keycloakService, UserService userService) {
        this.foundationRepository = foundationRepository;
        this.foundationMapper = foundationMapper;
        this.keycloakService = keycloakService;
        this.userService = userService;
    }

    @Override
    public FoundationDTO save(FoundationDTO foundationDTO) {
        log.debug("Request to save Foundation : {}", foundationDTO);
        foundationDTO.getName().replaceAll("\\s+", "").toLowerCase();
        foundationDTO.setId(UUID.randomUUID().toString());
        Foundation foundation = foundationMapper.toEntity(foundationDTO);
        keycloakService.addUser(foundation);
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
    public Page<FoundationDTO> findAll(Pageable pageable, Principal principal) {
        log.debug("Request to get all Foundations");
        User userFromPrincipal = userService.getUserFromPrincipal(principal);
        if (userFromPrincipal.getAuthorities().stream().findFirst().isPresent()) {
            Authority authority = userFromPrincipal.getAuthorities().stream().findFirst().get();
            if (authority.getName().equals(AuthoritiesConstants.ADMIN)) {
                return foundationRepository.findAll(pageable).map(foundationMapper::toDto);
            } else if (authority.getName().equals(AuthoritiesConstants.FOUNDATION_ADMIN)) {
                Foundation foundation = foundationRepository.findByName(userFromPrincipal.getLogin()).get();
                List<FoundationDTO> foundationDTO = List.of(foundationMapper.toDto(foundation));
                return new PageImpl<>(foundationDTO, pageable,foundationDTO.size());
            }
        }
        return null;
    }

    @Override
    public Optional<FoundationDTO> findOne(String id) {
        log.debug("Request to get Foundation : {}", id);
        return foundationRepository.findById(id).map(foundationMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Foundation : {}", id);
        Optional<Foundation> userInDatabase = foundationRepository.findById(id);
        keycloakService.deleteUserFromKeycloak(userInDatabase.get().getName());
        foundationRepository.deleteById(id);
    }
}
