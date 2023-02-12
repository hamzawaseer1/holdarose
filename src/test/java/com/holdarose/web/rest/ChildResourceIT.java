package com.holdarose.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.holdarose.IntegrationTest;
import com.holdarose.domain.Child;
import com.holdarose.domain.enumeration.Gender;
import com.holdarose.domain.enumeration.Status;
import com.holdarose.repository.ChildRepository;
import com.holdarose.service.dto.ChildDTO;
import com.holdarose.service.mapper.ChildMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ChildResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChildResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Status DEFAULT_STATUS = Status.AVAILABLE;
    private static final Status UPDATED_STATUS = Status.OCCUPIED;

    private static final String ENTITY_API_URL = "/api/children";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private MockMvc restChildMockMvc;

    private Child child;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Child createEntity() {
        Child child = new Child()
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .gender(DEFAULT_GENDER)
            .status(DEFAULT_STATUS);
        return child;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Child createUpdatedEntity() {
        Child child = new Child()
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .gender(UPDATED_GENDER)
            .status(UPDATED_STATUS);
        return child;
    }

    @BeforeEach
    public void initTest() {
        childRepository.deleteAll();
        child = createEntity();
    }

    @Test
    void createChild() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().size();
        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);
        restChildMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeCreate + 1);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChild.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testChild.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testChild.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testChild.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testChild.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createChildWithExistingId() throws Exception {
        // Create the Child with an existing ID
        child.setId("existing_id");
        ChildDTO childDTO = childMapper.toDto(child);

        int databaseSizeBeforeCreate = childRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChildMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllChildren() throws Exception {
        // Initialize the database
        childRepository.save(child);

        // Get all the childList
        restChildMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    void getChild() throws Exception {
        // Initialize the database
        childRepository.save(child);

        // Get the child
        restChildMockMvc
            .perform(get(ENTITY_API_URL_ID, child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(child.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingChild() throws Exception {
        // Get the child
        restChildMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewChild() throws Exception {
        // Initialize the database
        childRepository.save(child);

        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child
        Child updatedChild = childRepository.findById(child.getId()).get();
        updatedChild
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .gender(UPDATED_GENDER)
            .status(UPDATED_STATUS);
        ChildDTO childDTO = childMapper.toDto(updatedChild);

        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, childDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChild.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testChild.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testChild.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testChild.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testChild.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, childDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateChildWithPatch() throws Exception {
        // Initialize the database
        childRepository.save(child);

        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child using partial update
        Child partialUpdatedChild = new Child();
        partialUpdatedChild.setId(child.getId());

        partialUpdatedChild.name(UPDATED_NAME).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChild.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChild))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChild.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testChild.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testChild.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testChild.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testChild.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void fullUpdateChildWithPatch() throws Exception {
        // Initialize the database
        childRepository.save(child);

        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child using partial update
        Child partialUpdatedChild = new Child();
        partialUpdatedChild.setId(child.getId());

        partialUpdatedChild
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .gender(UPDATED_GENDER)
            .status(UPDATED_STATUS);

        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChild.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChild))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChild.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testChild.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testChild.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testChild.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testChild.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, childDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(UUID.randomUUID().toString());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteChild() throws Exception {
        // Initialize the database
        childRepository.save(child);

        int databaseSizeBeforeDelete = childRepository.findAll().size();

        // Delete the child
        restChildMockMvc
            .perform(delete(ENTITY_API_URL_ID, child.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
