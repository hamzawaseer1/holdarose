package com.holdarose.service;

import com.holdarose.service.dto.FoundationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.holdarose.domain.Foundation}.
 */
public interface FoundationService {
    /**
     * Save a foundation.
     *
     * @param foundationDTO the entity to save.
     * @return the persisted entity.
     */
    FoundationDTO save(FoundationDTO foundationDTO);

    /**
     * Updates a foundation.
     *
     * @param foundationDTO the entity to update.
     * @return the persisted entity.
     */
    FoundationDTO update(FoundationDTO foundationDTO);

    /**
     * Partially updates a foundation.
     *
     * @param foundationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FoundationDTO> partialUpdate(FoundationDTO foundationDTO);

    /**
     * Get all the foundations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FoundationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" foundation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FoundationDTO> findOne(String id);

    /**
     * Delete the "id" foundation.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
