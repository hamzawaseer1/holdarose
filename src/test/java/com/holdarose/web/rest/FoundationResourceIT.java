package com.holdarose.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.holdarose.IntegrationTest;
import com.holdarose.domain.Foundation;
import com.holdarose.repository.FoundationRepository;
import com.holdarose.service.dto.FoundationDTO;
import com.holdarose.service.mapper.FoundationMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link FoundationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FoundationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/foundations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FoundationRepository foundationRepository;

    @Autowired
    private FoundationMapper foundationMapper;

    @Autowired
    private MockMvc restFoundationMockMvc;

    private Foundation foundation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Foundation createEntity() {
        Foundation foundation = new Foundation()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION);
        return foundation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Foundation createUpdatedEntity() {
        Foundation foundation = new Foundation()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION);
        return foundation;
    }

    @BeforeEach
    public void initTest() {
        foundationRepository.deleteAll();
        foundation = createEntity();
    }

    @Test
    void createFoundation() throws Exception {
        int databaseSizeBeforeCreate = foundationRepository.findAll().size();
        // Create the Foundation
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation);
        restFoundationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeCreate + 1);
        Foundation testFoundation = foundationList.get(foundationList.size() - 1);
        assertThat(testFoundation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFoundation.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFoundation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFoundation.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    void createFoundationWithExistingId() throws Exception {
        // Create the Foundation with an existing ID
        foundation.setId("existing_id");
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation);

        int databaseSizeBeforeCreate = foundationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoundationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = foundationRepository.findAll().size();
        // set the field null
        foundation.setName(null);

        // Create the Foundation, which fails.
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation);

        restFoundationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = foundationRepository.findAll().size();
        // set the field null
        foundation.setEmail(null);

        // Create the Foundation, which fails.
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation);

        restFoundationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFoundations() throws Exception {
        // Initialize the database
        foundationRepository.save(foundation);

        // Get all the foundationList
        restFoundationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foundation.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    void getFoundation() throws Exception {
        // Initialize the database
        foundationRepository.save(foundation);

        // Get the foundation
        restFoundationMockMvc
            .perform(get(ENTITY_API_URL_ID, foundation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(foundation.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    void getNonExistingFoundation() throws Exception {
        // Get the foundation
        restFoundationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewFoundation() throws Exception {
        // Initialize the database
        foundationRepository.save(foundation);

        int databaseSizeBeforeUpdate = foundationRepository.findAll().size();

        // Update the foundation
        Foundation updatedFoundation = foundationRepository.findById(foundation.getId()).get();
        updatedFoundation.name(UPDATED_NAME).email(UPDATED_EMAIL).description(UPDATED_DESCRIPTION).location(UPDATED_LOCATION);
        FoundationDTO foundationDTO = foundationMapper.toDto(updatedFoundation);

        restFoundationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foundationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeUpdate);
        Foundation testFoundation = foundationList.get(foundationList.size() - 1);
        assertThat(testFoundation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFoundation.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFoundation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFoundation.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    void putNonExistingFoundation() throws Exception {
        int databaseSizeBeforeUpdate = foundationRepository.findAll().size();
        foundation.setId(UUID.randomUUID().toString());

        // Create the Foundation
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoundationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foundationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFoundation() throws Exception {
        int databaseSizeBeforeUpdate = foundationRepository.findAll().size();
        foundation.setId(UUID.randomUUID().toString());

        // Create the Foundation
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoundationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFoundation() throws Exception {
        int databaseSizeBeforeUpdate = foundationRepository.findAll().size();
        foundation.setId(UUID.randomUUID().toString());

        // Create the Foundation
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoundationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFoundationWithPatch() throws Exception {
        // Initialize the database
        foundationRepository.save(foundation);

        int databaseSizeBeforeUpdate = foundationRepository.findAll().size();

        // Update the foundation using partial update
        Foundation partialUpdatedFoundation = new Foundation();
        partialUpdatedFoundation.setId(foundation.getId());

        partialUpdatedFoundation.name(UPDATED_NAME).email(UPDATED_EMAIL).location(UPDATED_LOCATION);

        restFoundationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoundation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoundation))
            )
            .andExpect(status().isOk());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeUpdate);
        Foundation testFoundation = foundationList.get(foundationList.size() - 1);
        assertThat(testFoundation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFoundation.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFoundation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFoundation.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    void fullUpdateFoundationWithPatch() throws Exception {
        // Initialize the database
        foundationRepository.save(foundation);

        int databaseSizeBeforeUpdate = foundationRepository.findAll().size();

        // Update the foundation using partial update
        Foundation partialUpdatedFoundation = new Foundation();
        partialUpdatedFoundation.setId(foundation.getId());

        partialUpdatedFoundation.name(UPDATED_NAME).email(UPDATED_EMAIL).description(UPDATED_DESCRIPTION).location(UPDATED_LOCATION);

        restFoundationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoundation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoundation))
            )
            .andExpect(status().isOk());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeUpdate);
        Foundation testFoundation = foundationList.get(foundationList.size() - 1);
        assertThat(testFoundation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFoundation.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFoundation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFoundation.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    void patchNonExistingFoundation() throws Exception {
        int databaseSizeBeforeUpdate = foundationRepository.findAll().size();
        foundation.setId(UUID.randomUUID().toString());

        // Create the Foundation
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoundationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, foundationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFoundation() throws Exception {
        int databaseSizeBeforeUpdate = foundationRepository.findAll().size();
        foundation.setId(UUID.randomUUID().toString());

        // Create the Foundation
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoundationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFoundation() throws Exception {
        int databaseSizeBeforeUpdate = foundationRepository.findAll().size();
        foundation.setId(UUID.randomUUID().toString());

        // Create the Foundation
        FoundationDTO foundationDTO = foundationMapper.toDto(foundation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoundationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foundationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Foundation in the database
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFoundation() throws Exception {
        // Initialize the database
        foundationRepository.save(foundation);

        int databaseSizeBeforeDelete = foundationRepository.findAll().size();

        // Delete the foundation
        restFoundationMockMvc
            .perform(delete(ENTITY_API_URL_ID, foundation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Foundation> foundationList = foundationRepository.findAll();
        assertThat(foundationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
