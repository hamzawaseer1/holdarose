package com.holdarose.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.holdarose.IntegrationTest;
import com.holdarose.domain.AdoptionRequest;
import com.holdarose.repository.AdoptionRequestRepository;
import com.holdarose.service.dto.AdoptionRequestDTO;
import com.holdarose.service.mapper.AdoptionRequestMapper;
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
 * Integration tests for the {@link AdoptionRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdoptionRequestResourceIT {

    private static final String DEFAULT_CHILD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHILD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CNIC = "AAAAAAAAAA";
    private static final String UPDATED_CNIC = "BBBBBBBBBB";

    private static final String DEFAULT_FOSTER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FOSTER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FOSTER_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_FOSTER_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FOSTER_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_FOSTER_ADDRESS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_APPROVED = false;
    private static final Boolean UPDATED_APPROVED = true;

    private static final String DEFAULT_FOUNDATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FOUNDATION_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/adoption-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    @Autowired
    private AdoptionRequestMapper adoptionRequestMapper;

    @Autowired
    private MockMvc restAdoptionRequestMockMvc;

    private AdoptionRequest adoptionRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdoptionRequest createEntity() {
        AdoptionRequest adoptionRequest = new AdoptionRequest()
            .childName(DEFAULT_CHILD_NAME)
            .cnic(DEFAULT_CNIC)
            .fosterName(DEFAULT_FOSTER_NAME)
            .fosterJobTitle(DEFAULT_FOSTER_JOB_TITLE)
            .fosterAddress(DEFAULT_FOSTER_ADDRESS)
            .approved(DEFAULT_APPROVED)
            .foundationName(DEFAULT_FOUNDATION_NAME);
        return adoptionRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdoptionRequest createUpdatedEntity() {
        AdoptionRequest adoptionRequest = new AdoptionRequest()
            .childName(UPDATED_CHILD_NAME)
            .cnic(UPDATED_CNIC)
            .fosterName(UPDATED_FOSTER_NAME)
            .fosterJobTitle(UPDATED_FOSTER_JOB_TITLE)
            .fosterAddress(UPDATED_FOSTER_ADDRESS)
            .approved(UPDATED_APPROVED)
            .foundationName(UPDATED_FOUNDATION_NAME);
        return adoptionRequest;
    }

    @BeforeEach
    public void initTest() {
        adoptionRequestRepository.deleteAll();
        adoptionRequest = createEntity();
    }

    @Test
    void createAdoptionRequest() throws Exception {
        int databaseSizeBeforeCreate = adoptionRequestRepository.findAll().size();
        // Create the AdoptionRequest
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);
        restAdoptionRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeCreate + 1);
        AdoptionRequest testAdoptionRequest = adoptionRequestList.get(adoptionRequestList.size() - 1);
        assertThat(testAdoptionRequest.getChildName()).isEqualTo(DEFAULT_CHILD_NAME);
        assertThat(testAdoptionRequest.getCnic()).isEqualTo(DEFAULT_CNIC);
        assertThat(testAdoptionRequest.getFosterName()).isEqualTo(DEFAULT_FOSTER_NAME);
        assertThat(testAdoptionRequest.getFosterJobTitle()).isEqualTo(DEFAULT_FOSTER_JOB_TITLE);
        assertThat(testAdoptionRequest.getFosterAddress()).isEqualTo(DEFAULT_FOSTER_ADDRESS);
        assertThat(testAdoptionRequest.getApproved()).isEqualTo(DEFAULT_APPROVED);
        assertThat(testAdoptionRequest.getFoundationName()).isEqualTo(DEFAULT_FOUNDATION_NAME);
    }

    @Test
    void createAdoptionRequestWithExistingId() throws Exception {
        // Create the AdoptionRequest with an existing ID
        adoptionRequest.setId("existing_id");
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);

        int databaseSizeBeforeCreate = adoptionRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdoptionRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkChildNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = adoptionRequestRepository.findAll().size();
        // set the field null
        adoptionRequest.setChildName(null);

        // Create the AdoptionRequest, which fails.
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);

        restAdoptionRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCnicIsRequired() throws Exception {
        int databaseSizeBeforeTest = adoptionRequestRepository.findAll().size();
        // set the field null
        adoptionRequest.setCnic(null);

        // Create the AdoptionRequest, which fails.
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);

        restAdoptionRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFoundationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = adoptionRequestRepository.findAll().size();
        // set the field null
        adoptionRequest.setFoundationName(null);

        // Create the AdoptionRequest, which fails.
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);

        restAdoptionRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAdoptionRequests() throws Exception {
        // Initialize the database
        adoptionRequestRepository.save(adoptionRequest);

        // Get all the adoptionRequestList
        restAdoptionRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adoptionRequest.getId())))
            .andExpect(jsonPath("$.[*].childName").value(hasItem(DEFAULT_CHILD_NAME)))
            .andExpect(jsonPath("$.[*].cnic").value(hasItem(DEFAULT_CNIC)))
            .andExpect(jsonPath("$.[*].fosterName").value(hasItem(DEFAULT_FOSTER_NAME)))
            .andExpect(jsonPath("$.[*].fosterJobTitle").value(hasItem(DEFAULT_FOSTER_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].fosterAddress").value(hasItem(DEFAULT_FOSTER_ADDRESS)))
            .andExpect(jsonPath("$.[*].approved").value(hasItem(DEFAULT_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].foundationName").value(hasItem(DEFAULT_FOUNDATION_NAME)));
    }

    @Test
    void getAdoptionRequest() throws Exception {
        // Initialize the database
        adoptionRequestRepository.save(adoptionRequest);

        // Get the adoptionRequest
        restAdoptionRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, adoptionRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adoptionRequest.getId()))
            .andExpect(jsonPath("$.childName").value(DEFAULT_CHILD_NAME))
            .andExpect(jsonPath("$.cnic").value(DEFAULT_CNIC))
            .andExpect(jsonPath("$.fosterName").value(DEFAULT_FOSTER_NAME))
            .andExpect(jsonPath("$.fosterJobTitle").value(DEFAULT_FOSTER_JOB_TITLE))
            .andExpect(jsonPath("$.fosterAddress").value(DEFAULT_FOSTER_ADDRESS))
            .andExpect(jsonPath("$.approved").value(DEFAULT_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.foundationName").value(DEFAULT_FOUNDATION_NAME));
    }

    @Test
    void getNonExistingAdoptionRequest() throws Exception {
        // Get the adoptionRequest
        restAdoptionRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewAdoptionRequest() throws Exception {
        // Initialize the database
        adoptionRequestRepository.save(adoptionRequest);

        int databaseSizeBeforeUpdate = adoptionRequestRepository.findAll().size();

        // Update the adoptionRequest
        AdoptionRequest updatedAdoptionRequest = adoptionRequestRepository.findById(adoptionRequest.getId()).get();
        updatedAdoptionRequest
            .childName(UPDATED_CHILD_NAME)
            .cnic(UPDATED_CNIC)
            .fosterName(UPDATED_FOSTER_NAME)
            .fosterJobTitle(UPDATED_FOSTER_JOB_TITLE)
            .fosterAddress(UPDATED_FOSTER_ADDRESS)
            .approved(UPDATED_APPROVED)
            .foundationName(UPDATED_FOUNDATION_NAME);
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(updatedAdoptionRequest);

        restAdoptionRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adoptionRequestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeUpdate);
        AdoptionRequest testAdoptionRequest = adoptionRequestList.get(adoptionRequestList.size() - 1);
        assertThat(testAdoptionRequest.getChildName()).isEqualTo(UPDATED_CHILD_NAME);
        assertThat(testAdoptionRequest.getCnic()).isEqualTo(UPDATED_CNIC);
        assertThat(testAdoptionRequest.getFosterName()).isEqualTo(UPDATED_FOSTER_NAME);
        assertThat(testAdoptionRequest.getFosterJobTitle()).isEqualTo(UPDATED_FOSTER_JOB_TITLE);
        assertThat(testAdoptionRequest.getFosterAddress()).isEqualTo(UPDATED_FOSTER_ADDRESS);
        assertThat(testAdoptionRequest.getApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testAdoptionRequest.getFoundationName()).isEqualTo(UPDATED_FOUNDATION_NAME);
    }

    @Test
    void putNonExistingAdoptionRequest() throws Exception {
        int databaseSizeBeforeUpdate = adoptionRequestRepository.findAll().size();
        adoptionRequest.setId(UUID.randomUUID().toString());

        // Create the AdoptionRequest
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdoptionRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adoptionRequestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAdoptionRequest() throws Exception {
        int databaseSizeBeforeUpdate = adoptionRequestRepository.findAll().size();
        adoptionRequest.setId(UUID.randomUUID().toString());

        // Create the AdoptionRequest
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdoptionRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAdoptionRequest() throws Exception {
        int databaseSizeBeforeUpdate = adoptionRequestRepository.findAll().size();
        adoptionRequest.setId(UUID.randomUUID().toString());

        // Create the AdoptionRequest
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdoptionRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAdoptionRequestWithPatch() throws Exception {
        // Initialize the database
        adoptionRequestRepository.save(adoptionRequest);

        int databaseSizeBeforeUpdate = adoptionRequestRepository.findAll().size();

        // Update the adoptionRequest using partial update
        AdoptionRequest partialUpdatedAdoptionRequest = new AdoptionRequest();
        partialUpdatedAdoptionRequest.setId(adoptionRequest.getId());

        partialUpdatedAdoptionRequest
            .childName(UPDATED_CHILD_NAME)
            .fosterName(UPDATED_FOSTER_NAME)
            .fosterJobTitle(UPDATED_FOSTER_JOB_TITLE);

        restAdoptionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdoptionRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdoptionRequest))
            )
            .andExpect(status().isOk());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeUpdate);
        AdoptionRequest testAdoptionRequest = adoptionRequestList.get(adoptionRequestList.size() - 1);
        assertThat(testAdoptionRequest.getChildName()).isEqualTo(UPDATED_CHILD_NAME);
        assertThat(testAdoptionRequest.getCnic()).isEqualTo(DEFAULT_CNIC);
        assertThat(testAdoptionRequest.getFosterName()).isEqualTo(UPDATED_FOSTER_NAME);
        assertThat(testAdoptionRequest.getFosterJobTitle()).isEqualTo(UPDATED_FOSTER_JOB_TITLE);
        assertThat(testAdoptionRequest.getFosterAddress()).isEqualTo(DEFAULT_FOSTER_ADDRESS);
        assertThat(testAdoptionRequest.getApproved()).isEqualTo(DEFAULT_APPROVED);
        assertThat(testAdoptionRequest.getFoundationName()).isEqualTo(DEFAULT_FOUNDATION_NAME);
    }

    @Test
    void fullUpdateAdoptionRequestWithPatch() throws Exception {
        // Initialize the database
        adoptionRequestRepository.save(adoptionRequest);

        int databaseSizeBeforeUpdate = adoptionRequestRepository.findAll().size();

        // Update the adoptionRequest using partial update
        AdoptionRequest partialUpdatedAdoptionRequest = new AdoptionRequest();
        partialUpdatedAdoptionRequest.setId(adoptionRequest.getId());

        partialUpdatedAdoptionRequest
            .childName(UPDATED_CHILD_NAME)
            .cnic(UPDATED_CNIC)
            .fosterName(UPDATED_FOSTER_NAME)
            .fosterJobTitle(UPDATED_FOSTER_JOB_TITLE)
            .fosterAddress(UPDATED_FOSTER_ADDRESS)
            .approved(UPDATED_APPROVED)
            .foundationName(UPDATED_FOUNDATION_NAME);

        restAdoptionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdoptionRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdoptionRequest))
            )
            .andExpect(status().isOk());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeUpdate);
        AdoptionRequest testAdoptionRequest = adoptionRequestList.get(adoptionRequestList.size() - 1);
        assertThat(testAdoptionRequest.getChildName()).isEqualTo(UPDATED_CHILD_NAME);
        assertThat(testAdoptionRequest.getCnic()).isEqualTo(UPDATED_CNIC);
        assertThat(testAdoptionRequest.getFosterName()).isEqualTo(UPDATED_FOSTER_NAME);
        assertThat(testAdoptionRequest.getFosterJobTitle()).isEqualTo(UPDATED_FOSTER_JOB_TITLE);
        assertThat(testAdoptionRequest.getFosterAddress()).isEqualTo(UPDATED_FOSTER_ADDRESS);
        assertThat(testAdoptionRequest.getApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testAdoptionRequest.getFoundationName()).isEqualTo(UPDATED_FOUNDATION_NAME);
    }

    @Test
    void patchNonExistingAdoptionRequest() throws Exception {
        int databaseSizeBeforeUpdate = adoptionRequestRepository.findAll().size();
        adoptionRequest.setId(UUID.randomUUID().toString());

        // Create the AdoptionRequest
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdoptionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adoptionRequestDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAdoptionRequest() throws Exception {
        int databaseSizeBeforeUpdate = adoptionRequestRepository.findAll().size();
        adoptionRequest.setId(UUID.randomUUID().toString());

        // Create the AdoptionRequest
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdoptionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAdoptionRequest() throws Exception {
        int databaseSizeBeforeUpdate = adoptionRequestRepository.findAll().size();
        adoptionRequest.setId(UUID.randomUUID().toString());

        // Create the AdoptionRequest
        AdoptionRequestDTO adoptionRequestDTO = adoptionRequestMapper.toDto(adoptionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdoptionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adoptionRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdoptionRequest in the database
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAdoptionRequest() throws Exception {
        // Initialize the database
        adoptionRequestRepository.save(adoptionRequest);

        int databaseSizeBeforeDelete = adoptionRequestRepository.findAll().size();

        // Delete the adoptionRequest
        restAdoptionRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, adoptionRequest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdoptionRequest> adoptionRequestList = adoptionRequestRepository.findAll();
        assertThat(adoptionRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
