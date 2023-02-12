package com.holdarose.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.holdarose.IntegrationTest;
import com.holdarose.domain.Donation;
import com.holdarose.domain.enumeration.PaymentMethod;
import com.holdarose.repository.DonationRepository;
import com.holdarose.service.dto.DonationDTO;
import com.holdarose.service.mapper.DonationMapper;
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
 * Integration tests for the {@link DonationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DonationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CNIC = "AAAAAAAAAA";
    private static final String UPDATED_CNIC = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_FOUNDATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FOUNDATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DONATION_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_DONATION_AMOUNT = "BBBBBBBBBB";

    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.BY_HAND;
    private static final PaymentMethod UPDATED_PAYMENT_METHOD = PaymentMethod.THROUGH_BANK;

    private static final String ENTITY_API_URL = "/api/donations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationMapper donationMapper;

    @Autowired
    private MockMvc restDonationMockMvc;

    private Donation donation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donation createEntity() {
        Donation donation = new Donation()
            .name(DEFAULT_NAME)
            .cnic(DEFAULT_CNIC)
            .address(DEFAULT_ADDRESS)
            .foundationName(DEFAULT_FOUNDATION_NAME)
            .donationAmount(DEFAULT_DONATION_AMOUNT)
            .paymentMethod(DEFAULT_PAYMENT_METHOD);
        return donation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donation createUpdatedEntity() {
        Donation donation = new Donation()
            .name(UPDATED_NAME)
            .cnic(UPDATED_CNIC)
            .address(UPDATED_ADDRESS)
            .foundationName(UPDATED_FOUNDATION_NAME)
            .donationAmount(UPDATED_DONATION_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD);
        return donation;
    }

    @BeforeEach
    public void initTest() {
        donationRepository.deleteAll();
        donation = createEntity();
    }

    @Test
    void createDonation() throws Exception {
        int databaseSizeBeforeCreate = donationRepository.findAll().size();
        // Create the Donation
        DonationDTO donationDTO = donationMapper.toDto(donation);
        restDonationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeCreate + 1);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDonation.getCnic()).isEqualTo(DEFAULT_CNIC);
        assertThat(testDonation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testDonation.getFoundationName()).isEqualTo(DEFAULT_FOUNDATION_NAME);
        assertThat(testDonation.getDonationAmount()).isEqualTo(DEFAULT_DONATION_AMOUNT);
        assertThat(testDonation.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
    }

    @Test
    void createDonationWithExistingId() throws Exception {
        // Create the Donation with an existing ID
        donation.setId("existing_id");
        DonationDTO donationDTO = donationMapper.toDto(donation);

        int databaseSizeBeforeCreate = donationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCnicIsRequired() throws Exception {
        int databaseSizeBeforeTest = donationRepository.findAll().size();
        // set the field null
        donation.setCnic(null);

        // Create the Donation, which fails.
        DonationDTO donationDTO = donationMapper.toDto(donation);

        restDonationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDonations() throws Exception {
        // Initialize the database
        donationRepository.save(donation);

        // Get all the donationList
        restDonationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donation.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cnic").value(hasItem(DEFAULT_CNIC)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].foundationName").value(hasItem(DEFAULT_FOUNDATION_NAME)))
            .andExpect(jsonPath("$.[*].donationAmount").value(hasItem(DEFAULT_DONATION_AMOUNT)))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())));
    }

    @Test
    void getDonation() throws Exception {
        // Initialize the database
        donationRepository.save(donation);

        // Get the donation
        restDonationMockMvc
            .perform(get(ENTITY_API_URL_ID, donation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donation.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cnic").value(DEFAULT_CNIC))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.foundationName").value(DEFAULT_FOUNDATION_NAME))
            .andExpect(jsonPath("$.donationAmount").value(DEFAULT_DONATION_AMOUNT))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()));
    }

    @Test
    void getNonExistingDonation() throws Exception {
        // Get the donation
        restDonationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewDonation() throws Exception {
        // Initialize the database
        donationRepository.save(donation);

        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Update the donation
        Donation updatedDonation = donationRepository.findById(donation.getId()).get();
        updatedDonation
            .name(UPDATED_NAME)
            .cnic(UPDATED_CNIC)
            .address(UPDATED_ADDRESS)
            .foundationName(UPDATED_FOUNDATION_NAME)
            .donationAmount(UPDATED_DONATION_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD);
        DonationDTO donationDTO = donationMapper.toDto(updatedDonation);

        restDonationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDonation.getCnic()).isEqualTo(UPDATED_CNIC);
        assertThat(testDonation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDonation.getFoundationName()).isEqualTo(UPDATED_FOUNDATION_NAME);
        assertThat(testDonation.getDonationAmount()).isEqualTo(UPDATED_DONATION_AMOUNT);
        assertThat(testDonation.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
    }

    @Test
    void putNonExistingDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(UUID.randomUUID().toString());

        // Create the Donation
        DonationDTO donationDTO = donationMapper.toDto(donation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(UUID.randomUUID().toString());

        // Create the Donation
        DonationDTO donationDTO = donationMapper.toDto(donation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(UUID.randomUUID().toString());

        // Create the Donation
        DonationDTO donationDTO = donationMapper.toDto(donation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDonationWithPatch() throws Exception {
        // Initialize the database
        donationRepository.save(donation);

        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Update the donation using partial update
        Donation partialUpdatedDonation = new Donation();
        partialUpdatedDonation.setId(donation.getId());

        partialUpdatedDonation.name(UPDATED_NAME).address(UPDATED_ADDRESS).paymentMethod(UPDATED_PAYMENT_METHOD);

        restDonationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonation))
            )
            .andExpect(status().isOk());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDonation.getCnic()).isEqualTo(DEFAULT_CNIC);
        assertThat(testDonation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDonation.getFoundationName()).isEqualTo(DEFAULT_FOUNDATION_NAME);
        assertThat(testDonation.getDonationAmount()).isEqualTo(DEFAULT_DONATION_AMOUNT);
        assertThat(testDonation.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
    }

    @Test
    void fullUpdateDonationWithPatch() throws Exception {
        // Initialize the database
        donationRepository.save(donation);

        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Update the donation using partial update
        Donation partialUpdatedDonation = new Donation();
        partialUpdatedDonation.setId(donation.getId());

        partialUpdatedDonation
            .name(UPDATED_NAME)
            .cnic(UPDATED_CNIC)
            .address(UPDATED_ADDRESS)
            .foundationName(UPDATED_FOUNDATION_NAME)
            .donationAmount(UPDATED_DONATION_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD);

        restDonationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonation))
            )
            .andExpect(status().isOk());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDonation.getCnic()).isEqualTo(UPDATED_CNIC);
        assertThat(testDonation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDonation.getFoundationName()).isEqualTo(UPDATED_FOUNDATION_NAME);
        assertThat(testDonation.getDonationAmount()).isEqualTo(UPDATED_DONATION_AMOUNT);
        assertThat(testDonation.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
    }

    @Test
    void patchNonExistingDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(UUID.randomUUID().toString());

        // Create the Donation
        DonationDTO donationDTO = donationMapper.toDto(donation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, donationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(UUID.randomUUID().toString());

        // Create the Donation
        DonationDTO donationDTO = donationMapper.toDto(donation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(UUID.randomUUID().toString());

        // Create the Donation
        DonationDTO donationDTO = donationMapper.toDto(donation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDonation() throws Exception {
        // Initialize the database
        donationRepository.save(donation);

        int databaseSizeBeforeDelete = donationRepository.findAll().size();

        // Delete the donation
        restDonationMockMvc
            .perform(delete(ENTITY_API_URL_ID, donation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
