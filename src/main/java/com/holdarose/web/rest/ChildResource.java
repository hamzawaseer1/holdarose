package com.holdarose.web.rest;

import com.holdarose.repository.ChildRepository;
import com.holdarose.service.ChildService;
import com.holdarose.service.dto.ChildDTO;
import com.holdarose.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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


@RestController
@RequestMapping("/api")
public class ChildResource {

    private final Logger log = LoggerFactory.getLogger(ChildResource.class);

    public static final String ENTITY_NAME = "holdaroseChild";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChildService childService;

    private final ChildRepository childRepository;

    public ChildResource(ChildService childService, ChildRepository childRepository) {
        this.childService = childService;
        this.childRepository = childRepository;
    }


    @PostMapping("/children")
    public ResponseEntity<ChildDTO> createChild(@RequestBody ChildDTO childDTO) throws URISyntaxException {
        log.debug("REST request to save Child : {}", childDTO);
        if (childDTO.getId() != null) {
            throw new BadRequestAlertException("A new child cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChildDTO result = childService.save(childDTO);
        return ResponseEntity
            .created(new URI("/api/children/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }


    @PutMapping("/children/{id}")
    public ResponseEntity<ChildDTO> updateChild(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ChildDTO childDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Child : {}, {}", id, childDTO);
        if (childDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, childDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!childRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChildDTO result = childService.update(childDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, childDTO.getId()))
            .body(result);
    }


    @PatchMapping(value = "/children/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChildDTO> partialUpdateChild(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ChildDTO childDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Child partially : {}, {}", id, childDTO);
        if (childDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, childDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!childRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChildDTO> result = childService.partialUpdate(childDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, childDTO.getId())
        );
    }


    @GetMapping("/children")
    public ResponseEntity<List<ChildDTO>> getAllChildren(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        Principal principal
    ) {
        if ("adoptionrequest-is-null".equals(filter)) {
            log.debug("REST request to get all Childs where adoptionRequest is null");
            return new ResponseEntity<>(childService.findAllWhereAdoptionRequestIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Children");
        Page<ChildDTO> page = childService.findAll(pageable, principal);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @GetMapping("/children/{id}")
    public ResponseEntity<ChildDTO> getChild(@PathVariable String id) {
        log.debug("REST request to get Child : {}", id);
        Optional<ChildDTO> childDTO = childService.findOne(id);
        return ResponseUtil.wrapOrNotFound(childDTO);
    }


    @DeleteMapping("/children/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable String id) {
        log.debug("REST request to delete Child : {}", id);
        childService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
