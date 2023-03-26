package com.holdarose.service.impl;

import com.holdarose.domain.Authority;
import com.holdarose.domain.Child;
import com.holdarose.domain.Foundation;
import com.holdarose.domain.User;
import com.holdarose.domain.enumeration.Status;
import com.holdarose.repository.ChildRepository;
import com.holdarose.repository.FoundationRepository;
import com.holdarose.security.AuthoritiesConstants;
import com.holdarose.service.ChildService;
import com.holdarose.service.UserService;
import com.holdarose.service.dto.ChildDTO;
import com.holdarose.service.dto.FoundationDTO;
import com.holdarose.service.mapper.ChildMapper;
import com.holdarose.service.mapper.FoundationMapper;
import com.holdarose.web.rest.ChildResource;
import com.holdarose.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.holdarose.web.rest.ChildResource.ENTITY_NAME;

/**
 * Service Implementation for managing {@link Child}.
 */
@Service
public class ChildServiceImpl implements ChildService {

    private final Logger log = LoggerFactory.getLogger(ChildServiceImpl.class);

    private final ChildRepository childRepository;

    private final ChildMapper childMapper;

    private final FoundationRepository foundationRepository;

    private final FoundationMapper foundationMapper;

    private final UserService userService;

    public ChildServiceImpl(ChildRepository childRepository, ChildMapper childMapper, FoundationRepository foundationRepository, FoundationMapper foundationMapper, UserService userService) {
        this.childRepository = childRepository;
        this.childMapper = childMapper;
        this.foundationRepository = foundationRepository;
        this.foundationMapper = foundationMapper;
        this.userService = userService;
    }

    @Override
    public ChildDTO save(ChildDTO childDTO) {
        log.debug("Request to save Child : {}", childDTO);
        childDTO.setName(childDTO.getName().toUpperCase());
        Child child = childMapper.toEntity(childDTO);
        child.status(Status.AVAILABLE);
        child = childRepository.save(child);
        return childMapper.toDto(child);
    }

    @Override
    public ChildDTO update(ChildDTO childDTO) {
        log.debug("Request to save Child : {}", childDTO);
        childDTO.setStatus(Status.AVAILABLE);
        Child child = childMapper.toEntity(childDTO);
        child = childRepository.save(child);
        return childMapper.toDto(child);
    }

    @Override
    public Optional<ChildDTO> partialUpdate(ChildDTO childDTO) {
        log.debug("Request to partially update Child : {}", childDTO);

        return childRepository
            .findById(childDTO.getId())
            .map(existingChild -> {
                childMapper.partialUpdate(existingChild, childDTO);

                return existingChild;
            })
            .map(childRepository::save)
            .map(childMapper::toDto);
    }

    @Override
    public Page<ChildDTO> findAll(Pageable pageable, Principal principal) {
        log.debug("Request to get all Children");
        if (principal != null) {
            User userFromPrincipal = userService.getUserFromPrincipal(principal);
            Set<Authority> authorities = userFromPrincipal.getAuthorities();
            Optional<Authority> first = authorities.stream().filter(authority -> authority.getName().equals(AuthoritiesConstants.FOUNDATION_ADMIN)).findFirst();
            if (first.isPresent()) {
                String loginUser = userFromPrincipal.getLogin();
                Optional<Foundation> foundation = foundationRepository.findByName(loginUser);
                if (foundation.isPresent()) {

                    Optional<FoundationDTO> foundationDTO = foundation.map(foundationMapper::toDto);
                    List<ChildDTO> collect = childRepository
                        .findChildByFoundation(foundation.get(), pageable)
                        .map(childMapper::toDto)
                        .map(childDTO -> {
                            childDTO.setFoundation(foundationDTO.orElseThrow(()-> new BadRequestAlertException("Foundation not found", ENTITY_NAME, "foundationNotFound")));
                            return childDTO;
                        })
                        .stream().collect(Collectors.toList());
                    return new PageImpl<>(collect, pageable, collect.size());
                }
            }
        }
        Page<Child> all = childRepository.findAll(pageable);
        List<ChildDTO> collect = all.stream()
            .map(child -> {
                ChildDTO childDTO = childMapper.toDto(child);
                Optional<FoundationDTO> foundationDTO = foundationRepository.findById(childDTO.getFoundation().getId())
                    .map(foundationMapper::toDto);
                childDTO.setFoundation(foundationDTO.get());
                return childDTO;
            }).collect(Collectors.toList());
        return new PageImpl<>(collect, pageable, collect.size());
    }

    /**
     * Get all the children where AdoptionRequest is {@code null}.
     *
     * @return the list of entities.
     */

    public List<ChildDTO> findAllWhereAdoptionRequestIsNull() {
        log.debug("Request to get all children where AdoptionRequest is null");
        return StreamSupport
            .stream(childRepository.findAll().spliterator(), false)
            .filter(child -> child.getAdoptionRequest() == null)
            .map(childMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<ChildDTO> findOne(String id) {
        log.debug("Request to get Child : {}", id);
        Optional<Child> childFromDBbyId = childRepository.findById(id);
        Optional<Foundation> foundation = foundationRepository.findById(childFromDBbyId.get().getFoundation().getId());
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation.get());
        Optional<ChildDTO> childDTO = childRepository.findById(id).map(childMapper::toDto);
        childDTO.get().setFoundation(foundationDTO);
        return childDTO;
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Child : {}", id);
        childRepository.deleteById(id);
    }
}
