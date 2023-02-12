package com.holdarose.web.rest;

import com.holdarose.repository.DonationRepository;
import com.holdarose.service.DonationService;
import com.holdarose.service.dto.DonationDTO;
import com.holdarose.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.holdarose.domain.Donation}.
 */
@RestController
@RequestMapping("/api")
public class DonationResource {

    private final Logger log = LoggerFactory.getLogger(DonationResource.class);

    private static final String ENTITY_NAME = "holdaroseDonation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonationService donationService;

    private final DonationRepository donationRepository;

    public DonationResource(DonationService donationService, DonationRepository donationRepository) {
        this.donationService = donationService;
        this.donationRepository = donationRepository;
    }

    /**
     * {@code POST  /donations} : Create a new donation.
     *
     * @param donationDTO the donationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new donationDTO, or with status {@code 400 (Bad Request)} if the donation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/donations")
    public ResponseEntity<DonationDTO> createDonation(@Valid @RequestBody DonationDTO donationDTO) throws URISyntaxException {
        log.debug("REST request to save Donation : {}", donationDTO);
        if (donationDTO.getId() != null) {
            throw new BadRequestAlertException("A new donation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DonationDTO result = donationService.save(donationDTO);
        return ResponseEntity
            .created(new URI("/api/donations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /donations/:id} : Updates an existing donation.
     *
     * @param id the id of the donationDTO to save.
     * @param donationDTO the donationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donationDTO,
     * or with status {@code 400 (Bad Request)} if the donationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the donationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/donations/{id}")
    public ResponseEntity<DonationDTO> updateDonation(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody DonationDTO donationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Donation : {}, {}", id, donationDTO);
        if (donationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DonationDTO result = donationService.update(donationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donationDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /donations/:id} : Partial updates given fields of an existing donation, field will ignore if it is null
     *
     * @param id the id of the donationDTO to save.
     * @param donationDTO the donationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donationDTO,
     * or with status {@code 400 (Bad Request)} if the donationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the donationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the donationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/donations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DonationDTO> partialUpdateDonation(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody DonationDTO donationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Donation partially : {}, {}", id, donationDTO);
        if (donationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DonationDTO> result = donationService.partialUpdate(donationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donationDTO.getId())
        );
    }

    /**
     * {@code GET  /donations} : get all the donations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donations in body.
     */
    @GetMapping("/donations")
    public ResponseEntity<List<DonationDTO>> getAllDonations(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Donations");
        Page<DonationDTO> page = donationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /donations/:id} : get the "id" donation.
     *
     * @param id the id of the donationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/donations/{id}")
    public ResponseEntity<DonationDTO> getDonation(@PathVariable String id) {
        log.debug("REST request to get Donation : {}", id);
        Optional<DonationDTO> donationDTO = donationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(donationDTO);
    }

    /**
     * {@code DELETE  /donations/:id} : delete the "id" donation.
     *
     * @param id the id of the donationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/donations/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable String id) {
        log.debug("REST request to delete Donation : {}", id);
        donationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
