package com.holdarose.web.rest;

import com.holdarose.repository.AdoptionRequestRepository;
import com.holdarose.service.AdoptionRequestService;
import com.holdarose.service.dto.AdoptionRequestDTO;
import com.holdarose.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
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
 * REST controller for managing {@link com.holdarose.domain.AdoptionRequest}.
 */
@RestController
@RequestMapping("/api")
public class AdoptionRequestResource {

    private final Logger log = LoggerFactory.getLogger(AdoptionRequestResource.class);

    private static final String ENTITY_NAME = "holdaroseAdoptionRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdoptionRequestService adoptionRequestService;

    private final AdoptionRequestRepository adoptionRequestRepository;

    public AdoptionRequestResource(AdoptionRequestService adoptionRequestService, AdoptionRequestRepository adoptionRequestRepository) {
        this.adoptionRequestService = adoptionRequestService;
        this.adoptionRequestRepository = adoptionRequestRepository;
    }

    /**
     * {@code POST  /adoption-requests} : Create a new adoptionRequest.
     *
     * @param adoptionRequestDTO the adoptionRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adoptionRequestDTO, or with status {@code 400 (Bad Request)} if the adoptionRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/adoption-requests")
    public ResponseEntity<AdoptionRequestDTO> createAdoptionRequest(@Valid @RequestBody AdoptionRequestDTO adoptionRequestDTO)
        throws URISyntaxException {
        log.debug("REST request to save AdoptionRequest : {}", adoptionRequestDTO);
        if (adoptionRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new adoptionRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdoptionRequestDTO result = adoptionRequestService.save(adoptionRequestDTO);
        return ResponseEntity
            .created(new URI("/api/adoption-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /adoption-requests/:id} : Updates an existing adoptionRequest.
     *
     * @param id the id of the adoptionRequestDTO to save.
     * @param adoptionRequestDTO the adoptionRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adoptionRequestDTO,
     * or with status {@code 400 (Bad Request)} if the adoptionRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adoptionRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/adoption-requests/{id}")
    public ResponseEntity<AdoptionRequestDTO> updateAdoptionRequest(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody AdoptionRequestDTO adoptionRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AdoptionRequest : {}, {}", id, adoptionRequestDTO);
        if (adoptionRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adoptionRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adoptionRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdoptionRequestDTO result = adoptionRequestService.update(adoptionRequestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adoptionRequestDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /adoption-requests/:id} : Partial updates given fields of an existing adoptionRequest, field will ignore if it is null
     *
     * @param id the id of the adoptionRequestDTO to save.
     * @param adoptionRequestDTO the adoptionRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adoptionRequestDTO,
     * or with status {@code 400 (Bad Request)} if the adoptionRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adoptionRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adoptionRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/adoption-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdoptionRequestDTO> partialUpdateAdoptionRequest(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody AdoptionRequestDTO adoptionRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdoptionRequest partially : {}, {}", id, adoptionRequestDTO);
        if (adoptionRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adoptionRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adoptionRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdoptionRequestDTO> result = adoptionRequestService.partialUpdate(adoptionRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adoptionRequestDTO.getId())
        );
    }

    /**
     * {@code GET  /adoption-requests} : get all the adoptionRequests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adoptionRequests in body.
     */
    @GetMapping("/adoption-requests")
    public ResponseEntity<List<AdoptionRequestDTO>> getAllAdoptionRequests(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        Principal principal
    ) {
        log.debug("REST request to get a page of AdoptionRequests");
        Page<AdoptionRequestDTO> page = adoptionRequestService.findAll(pageable, principal);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /adoption-requests/:id} : get the "id" adoptionRequest.
     *
     * @param id the id of the adoptionRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adoptionRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/adoption-requests/{id}")
    public ResponseEntity<AdoptionRequestDTO> getAdoptionRequest(@PathVariable String id) {
        log.debug("REST request to get AdoptionRequest : {}", id);
        Optional<AdoptionRequestDTO> adoptionRequestDTO = adoptionRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adoptionRequestDTO);
    }

    /**
     * {@code DELETE  /adoption-requests/:id} : delete the "id" adoptionRequest.
     *
     * @param id the id of the adoptionRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/adoption-requests/{id}")
    public ResponseEntity<Void> deleteAdoptionRequest(@PathVariable String id) {
        log.debug("REST request to delete AdoptionRequest : {}", id);
        adoptionRequestService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
