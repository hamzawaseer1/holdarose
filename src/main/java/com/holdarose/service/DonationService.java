package com.holdarose.service;

import com.holdarose.service.dto.DonationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.holdarose.domain.Donation}.
 */
public interface DonationService {
    /**
     * Save a donation.
     *
     * @param donationDTO the entity to save.
     * @return the persisted entity.
     */
    DonationDTO save(DonationDTO donationDTO);

    /**
     * Updates a donation.
     *
     * @param donationDTO the entity to update.
     * @return the persisted entity.
     */
    DonationDTO update(DonationDTO donationDTO);

    /**
     * Partially updates a donation.
     *
     * @param donationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DonationDTO> partialUpdate(DonationDTO donationDTO);

    /**
     * Get all the donations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DonationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" donation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DonationDTO> findOne(String id);

    /**
     * Delete the "id" donation.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
