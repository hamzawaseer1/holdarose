package com.holdarose.web.rest;

import com.holdarose.repository.FostersRepository;
import com.holdarose.service.FostersService;
import com.holdarose.service.dto.FostersDTO;
import com.holdarose.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.holdarose.domain.Fosters}.
 */
@RestController
@RequestMapping("/api")
public class FostersResource {

    private final Logger log = LoggerFactory.getLogger(FostersResource.class);

    private static final String ENTITY_NAME = "holdaroseFosters";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FostersService fostersService;

    private final FostersRepository fostersRepository;

    public FostersResource(FostersService fostersService, FostersRepository fostersRepository) {
        this.fostersService = fostersService;
        this.fostersRepository = fostersRepository;
    }

    /**
     * {@code POST  /fosters} : Create a new fosters.
     *
     * @param fostersDTO the fostersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fostersDTO, or with status {@code 400 (Bad Request)} if the fosters has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fosters")
    public ResponseEntity<FostersDTO> createFosters(@RequestBody FostersDTO fostersDTO) throws URISyntaxException {
        log.debug("REST request to save Fosters : {}", fostersDTO);
        if (fostersDTO.getId() != null) {
            throw new BadRequestAlertException("A new fosters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FostersDTO result = fostersService.save(fostersDTO);
        return ResponseEntity
            .created(new URI("/api/fosters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /fosters/:id} : Updates an existing fosters.
     *
     * @param id the id of the fostersDTO to save.
     * @param fostersDTO the fostersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fostersDTO,
     * or with status {@code 400 (Bad Request)} if the fostersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fostersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fosters/{id}")
    public ResponseEntity<FostersDTO> updateFosters(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FostersDTO fostersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Fosters : {}, {}", id, fostersDTO);
        if (fostersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fostersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fostersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FostersDTO result = fostersService.update(fostersDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fostersDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /fosters/:id} : Partial updates given fields of an existing fosters, field will ignore if it is null
     *
     * @param id the id of the fostersDTO to save.
     * @param fostersDTO the fostersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fostersDTO,
     * or with status {@code 400 (Bad Request)} if the fostersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fostersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fostersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fosters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FostersDTO> partialUpdateFosters(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FostersDTO fostersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fosters partially : {}, {}", id, fostersDTO);
        if (fostersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fostersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fostersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FostersDTO> result = fostersService.partialUpdate(fostersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fostersDTO.getId())
        );
    }

    /**
     * {@code GET  /fosters} : get all the fosters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fosters in body.
     */
    @GetMapping("/fosters")
    public ResponseEntity<List<FostersDTO>> getAllFosters(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Fosters");
        Page<FostersDTO> page = fostersService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fosters/:id} : get the "id" fosters.
     *
     * @param id the id of the fostersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fostersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fosters/{id}")
    public ResponseEntity<FostersDTO> getFosters(@PathVariable String id) {
        log.debug("REST request to get Fosters : {}", id);
        Optional<FostersDTO> fostersDTO = fostersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fostersDTO);
    }

    /**
     * {@code DELETE  /fosters/:id} : delete the "id" fosters.
     *
     * @param id the id of the fostersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fosters/{id}")
    public ResponseEntity<Void> deleteFosters(@PathVariable String id) {
        log.debug("REST request to delete Fosters : {}", id);
        fostersService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
