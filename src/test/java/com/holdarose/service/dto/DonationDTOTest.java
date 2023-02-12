package com.holdarose.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.holdarose.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DonationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DonationDTO.class);
        DonationDTO donationDTO1 = new DonationDTO();
        donationDTO1.setId("id1");
        DonationDTO donationDTO2 = new DonationDTO();
        assertThat(donationDTO1).isNotEqualTo(donationDTO2);
        donationDTO2.setId(donationDTO1.getId());
        assertThat(donationDTO1).isEqualTo(donationDTO2);
        donationDTO2.setId("id2");
        assertThat(donationDTO1).isNotEqualTo(donationDTO2);
        donationDTO1.setId(null);
        assertThat(donationDTO1).isNotEqualTo(donationDTO2);
    }
}
