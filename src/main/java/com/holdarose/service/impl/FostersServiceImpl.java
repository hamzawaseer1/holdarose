package com.holdarose.service.impl;

import com.holdarose.domain.Fosters;
import com.holdarose.repository.FostersRepository;
import com.holdarose.service.FostersService;
import com.holdarose.service.dto.FostersDTO;
import com.holdarose.service.mapper.FostersMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Fosters}.
 */
@Service
public class FostersServiceImpl implements FostersService {

    private final Logger log = LoggerFactory.getLogger(FostersServiceImpl.class);

    private final FostersRepository fostersRepository;

    private final FostersMapper fostersMapper;

    public FostersServiceImpl(FostersRepository fostersRepository, FostersMapper fostersMapper) {
        this.fostersRepository = fostersRepository;
        this.fostersMapper = fostersMapper;
    }

    @Override
    public FostersDTO save(FostersDTO fostersDTO) {
        log.debug("Request to save Fosters : {}", fostersDTO);
        Fosters fosters = fostersMapper.toEntity(fostersDTO);
        fosters = fostersRepository.save(fosters);
        return fostersMapper.toDto(fosters);
    }

    @Override
    public FostersDTO update(FostersDTO fostersDTO) {
        log.debug("Request to save Fosters : {}", fostersDTO);
        Fosters fosters = fostersMapper.toEntity(fostersDTO);
        fosters = fostersRepository.save(fosters);
        return fostersMapper.toDto(fosters);
    }

    @Override
    public Optional<FostersDTO> partialUpdate(FostersDTO fostersDTO) {
        log.debug("Request to partially update Fosters : {}", fostersDTO);

        return fostersRepository
            .findById(fostersDTO.getId())
            .map(existingFosters -> {
                fostersMapper.partialUpdate(existingFosters, fostersDTO);

                return existingFosters;
            })
            .map(fostersRepository::save)
            .map(fostersMapper::toDto);
    }

    @Override
    public Page<FostersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fosters");
        return fostersRepository.findAll(pageable).map(fostersMapper::toDto);
    }

    @Override
    public Optional<FostersDTO> findOne(String id) {
        log.debug("Request to get Fosters : {}", id);
        return fostersRepository.findById(id).map(fostersMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Fosters : {}", id);

        fostersRepository.deleteById(id);
    }
}
