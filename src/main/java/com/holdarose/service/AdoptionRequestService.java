package com.holdarose.service;

import com.holdarose.service.dto.AdoptionRequestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.holdarose.domain.AdoptionRequest}.
 */
public interface AdoptionRequestService {
    /**
     * Save a adoptionRequest.
     *
     * @param adoptionRequestDTO the entity to save.
     * @return the persisted entity.
     */
    AdoptionRequestDTO save(AdoptionRequestDTO adoptionRequestDTO);

    /**
     * Updates a adoptionRequest.
     *
     * @param adoptionRequestDTO the entity to update.
     * @return the persisted entity.
     */
    AdoptionRequestDTO update(AdoptionRequestDTO adoptionRequestDTO);

    /**
     * Partially updates a adoptionRequest.
     *
     * @param adoptionRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdoptionRequestDTO> partialUpdate(AdoptionRequestDTO adoptionRequestDTO);

    /**
     * Get all the adoptionRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AdoptionRequestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" adoptionRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdoptionRequestDTO> findOne(String id);

    /**
     * Delete the "id" adoptionRequest.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
