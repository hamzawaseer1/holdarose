package com.holdarose.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.holdarose.IntegrationTest;
import com.holdarose.domain.Fosters;
import com.holdarose.repository.FostersRepository;
import com.holdarose.service.dto.FostersDTO;
import com.holdarose.service.mapper.FostersMapper;
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
 * Integration tests for the {@link FostersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FostersResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CNIC = "AAAAAAAAAA";
    private static final String UPDATED_CNIC = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fosters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FostersRepository fostersRepository;

    @Autowired
    private FostersMapper fostersMapper;

    @Autowired
    private MockMvc restFostersMockMvc;

    private Fosters fosters;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fosters createEntity() {
        Fosters fosters = new Fosters()
            .name(DEFAULT_NAME)
            .cnic(DEFAULT_CNIC)
            .email(DEFAULT_EMAIL)
            .jobTitle(DEFAULT_JOB_TITLE)
            .location(DEFAULT_LOCATION);
        return fosters;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fosters createUpdatedEntity() {
        Fosters fosters = new Fosters()
            .name(UPDATED_NAME)
            .cnic(UPDATED_CNIC)
            .email(UPDATED_EMAIL)
            .jobTitle(UPDATED_JOB_TITLE)
            .location(UPDATED_LOCATION);
        return fosters;
    }

    @BeforeEach
    public void initTest() {
        fostersRepository.deleteAll();
        fosters = createEntity();
    }

    @Test
    void createFosters() throws Exception {
        int databaseSizeBeforeCreate = fostersRepository.findAll().size();
        // Create the Fosters
        FostersDTO fostersDTO = fostersMapper.toDto(fosters);
        restFostersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fostersDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeCreate + 1);
        Fosters testFosters = fostersList.get(fostersList.size() - 1);
        assertThat(testFosters.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFosters.getCnic()).isEqualTo(DEFAULT_CNIC);
        assertThat(testFosters.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFosters.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testFosters.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    void createFostersWithExistingId() throws Exception {
        // Create the Fosters with an existing ID
        fosters.setId("existing_id");
        FostersDTO fostersDTO = fostersMapper.toDto(fosters);

        int databaseSizeBeforeCreate = fostersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFostersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fostersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFosters() throws Exception {
        // Initialize the database
        fostersRepository.save(fosters);

        // Get all the fostersList
        restFostersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fosters.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cnic").value(hasItem(DEFAULT_CNIC)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    void getFosters() throws Exception {
        // Initialize the database
        fostersRepository.save(fosters);

        // Get the fosters
        restFostersMockMvc
            .perform(get(ENTITY_API_URL_ID, fosters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fosters.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cnic").value(DEFAULT_CNIC))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    void getNonExistingFosters() throws Exception {
        // Get the fosters
        restFostersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewFosters() throws Exception {
        // Initialize the database
        fostersRepository.save(fosters);

        int databaseSizeBeforeUpdate = fostersRepository.findAll().size();

        // Update the fosters
        Fosters updatedFosters = fostersRepository.findById(fosters.getId()).get();
        updatedFosters.name(UPDATED_NAME).cnic(UPDATED_CNIC).email(UPDATED_EMAIL).jobTitle(UPDATED_JOB_TITLE).location(UPDATED_LOCATION);
        FostersDTO fostersDTO = fostersMapper.toDto(updatedFosters);

        restFostersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fostersDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fostersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeUpdate);
        Fosters testFosters = fostersList.get(fostersList.size() - 1);
        assertThat(testFosters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFosters.getCnic()).isEqualTo(UPDATED_CNIC);
        assertThat(testFosters.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFosters.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testFosters.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    void putNonExistingFosters() throws Exception {
        int databaseSizeBeforeUpdate = fostersRepository.findAll().size();
        fosters.setId(UUID.randomUUID().toString());

        // Create the Fosters
        FostersDTO fostersDTO = fostersMapper.toDto(fosters);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFostersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fostersDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fostersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFosters() throws Exception {
        int databaseSizeBeforeUpdate = fostersRepository.findAll().size();
        fosters.setId(UUID.randomUUID().toString());

        // Create the Fosters
        FostersDTO fostersDTO = fostersMapper.toDto(fosters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFostersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fostersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFosters() throws Exception {
        int databaseSizeBeforeUpdate = fostersRepository.findAll().size();
        fosters.setId(UUID.randomUUID().toString());

        // Create the Fosters
        FostersDTO fostersDTO = fostersMapper.toDto(fosters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFostersMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fostersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFostersWithPatch() throws Exception {
        // Initialize the database
        fostersRepository.save(fosters);

        int databaseSizeBeforeUpdate = fostersRepository.findAll().size();

        // Update the fosters using partial update
        Fosters partialUpdatedFosters = new Fosters();
        partialUpdatedFosters.setId(fosters.getId());

        partialUpdatedFosters.name(UPDATED_NAME).cnic(UPDATED_CNIC).location(UPDATED_LOCATION);

        restFostersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFosters.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFosters))
            )
            .andExpect(status().isOk());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeUpdate);
        Fosters testFosters = fostersList.get(fostersList.size() - 1);
        assertThat(testFosters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFosters.getCnic()).isEqualTo(UPDATED_CNIC);
        assertThat(testFosters.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFosters.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testFosters.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    void fullUpdateFostersWithPatch() throws Exception {
        // Initialize the database
        fostersRepository.save(fosters);

        int databaseSizeBeforeUpdate = fostersRepository.findAll().size();

        // Update the fosters using partial update
        Fosters partialUpdatedFosters = new Fosters();
        partialUpdatedFosters.setId(fosters.getId());

        partialUpdatedFosters
            .name(UPDATED_NAME)
            .cnic(UPDATED_CNIC)
            .email(UPDATED_EMAIL)
            .jobTitle(UPDATED_JOB_TITLE)
            .location(UPDATED_LOCATION);

        restFostersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFosters.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFosters))
            )
            .andExpect(status().isOk());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeUpdate);
        Fosters testFosters = fostersList.get(fostersList.size() - 1);
        assertThat(testFosters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFosters.getCnic()).isEqualTo(UPDATED_CNIC);
        assertThat(testFosters.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFosters.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testFosters.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    void patchNonExistingFosters() throws Exception {
        int databaseSizeBeforeUpdate = fostersRepository.findAll().size();
        fosters.setId(UUID.randomUUID().toString());

        // Create the Fosters
        FostersDTO fostersDTO = fostersMapper.toDto(fosters);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFostersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fostersDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fostersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFosters() throws Exception {
        int databaseSizeBeforeUpdate = fostersRepository.findAll().size();
        fosters.setId(UUID.randomUUID().toString());

        // Create the Fosters
        FostersDTO fostersDTO = fostersMapper.toDto(fosters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFostersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fostersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFosters() throws Exception {
        int databaseSizeBeforeUpdate = fostersRepository.findAll().size();
        fosters.setId(UUID.randomUUID().toString());

        // Create the Fosters
        FostersDTO fostersDTO = fostersMapper.toDto(fosters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFostersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fostersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fosters in the database
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFosters() throws Exception {
        // Initialize the database
        fostersRepository.save(fosters);

        int databaseSizeBeforeDelete = fostersRepository.findAll().size();

        // Delete the fosters
        restFostersMockMvc
            .perform(delete(ENTITY_API_URL_ID, fosters.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fosters> fostersList = fostersRepository.findAll();
        assertThat(fostersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
