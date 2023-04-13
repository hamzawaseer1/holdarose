package com.holdarose.web.rest;

import com.holdarose.repository.FoundationRepository;
import com.holdarose.service.FoundationService;
import com.holdarose.service.dto.FoundationDTO;
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
 * REST controller for managing {@link com.holdarose.domain.Foundation}.
 */
@RestController
@RequestMapping("/api")
public class FoundationResource {

    private final Logger log = LoggerFactory.getLogger(FoundationResource.class);

    private static final String ENTITY_NAME = "holdaroseFoundation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoundationService foundationService;

    private final FoundationRepository foundationRepository;

    public FoundationResource(FoundationService foundationService, FoundationRepository foundationRepository) {
        this.foundationService = foundationService;
        this.foundationRepository = foundationRepository;
    }

    /**
     * {@code POST  /foundations} : Create a new foundation.
     *
     * @param foundationDTO the foundationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new foundationDTO, or with status {@code 400 (Bad Request)} if the foundation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/foundations")
    public ResponseEntity<FoundationDTO> createFoundation(@Valid @RequestBody FoundationDTO foundationDTO) throws URISyntaxException {
        log.debug("REST request to save Foundation : {}", foundationDTO);
        if (foundationDTO.getId() != null) {
            throw new BadRequestAlertException("A new foundation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FoundationDTO result = foundationService.save(foundationDTO);
        return ResponseEntity
            .created(new URI("/api/foundations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/foundations/{id}")
    public ResponseEntity<FoundationDTO> updateFoundation(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FoundationDTO foundationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Foundation : {}, {}", id, foundationDTO);
        if (foundationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foundationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foundationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FoundationDTO result = foundationService.update(foundationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foundationDTO.getId()))
            .body(result);
    }

    @PatchMapping(value = "/foundations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FoundationDTO> partialUpdateFoundation(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FoundationDTO foundationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Foundation partially : {}, {}", id, foundationDTO);
        if (foundationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foundationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foundationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FoundationDTO> result = foundationService.partialUpdate(foundationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foundationDTO.getId())
        );
    }


    @GetMapping("/foundations")
    public ResponseEntity<List<FoundationDTO>> getAllFoundations(@org.springdoc.api.annotations.ParameterObject Pageable pageable
    , Principal principal) {
        log.debug("REST request to get a page of Foundations");
        Page<FoundationDTO> page = foundationService.findAll(pageable, principal);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @GetMapping("/foundations/{id}")
    public ResponseEntity<FoundationDTO> getFoundation(@PathVariable String id) {
        log.debug("REST request to get Foundation : {}", id);
        Optional<FoundationDTO> foundationDTO = foundationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(foundationDTO);
    }


    @DeleteMapping("/foundations/{id}")
    public ResponseEntity<Void> deleteFoundation(@PathVariable String id) {
        log.debug("REST request to delete Foundation : {}", id);
        foundationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
