package com.holdarose.service;

import com.holdarose.service.dto.FostersDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.holdarose.domain.Fosters}.
 */
public interface FostersService {
    /**
     * Save a fosters.
     *
     * @param fostersDTO the entity to save.
     * @return the persisted entity.
     */
    FostersDTO save(FostersDTO fostersDTO);

    /**
     * Updates a fosters.
     *
     * @param fostersDTO the entity to update.
     * @return the persisted entity.
     */
    FostersDTO update(FostersDTO fostersDTO);

    /**
     * Partially updates a fosters.
     *
     * @param fostersDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FostersDTO> partialUpdate(FostersDTO fostersDTO);

    /**
     * Get all the fosters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FostersDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fosters.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FostersDTO> findOne(String id);

    /**
     * Delete the "id" fosters.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
