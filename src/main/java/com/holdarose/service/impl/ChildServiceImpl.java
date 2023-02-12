package com.holdarose.service.impl;

import com.holdarose.domain.Child;
import com.holdarose.repository.ChildRepository;
import com.holdarose.service.ChildService;
import com.holdarose.service.dto.ChildDTO;
import com.holdarose.service.mapper.ChildMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Child}.
 */
@Service
public class ChildServiceImpl implements ChildService {

    private final Logger log = LoggerFactory.getLogger(ChildServiceImpl.class);

    private final ChildRepository childRepository;

    private final ChildMapper childMapper;

    public ChildServiceImpl(ChildRepository childRepository, ChildMapper childMapper) {
        this.childRepository = childRepository;
        this.childMapper = childMapper;
    }

    @Override
    public ChildDTO save(ChildDTO childDTO) {
        log.debug("Request to save Child : {}", childDTO);
        Child child = childMapper.toEntity(childDTO);
        child = childRepository.save(child);
        return childMapper.toDto(child);
    }

    @Override
    public ChildDTO update(ChildDTO childDTO) {
        log.debug("Request to save Child : {}", childDTO);
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
    public Page<ChildDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Children");
        return childRepository.findAll(pageable).map(childMapper::toDto);
    }

    /**
     *  Get all the children where AdoptionRequest is {@code null}.
     *  @return the list of entities.
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
        return childRepository.findById(id).map(childMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Child : {}", id);
        childRepository.deleteById(id);
    }
}
