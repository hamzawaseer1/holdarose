package com.holdarose.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.holdarose.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdoptionRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdoptionRequestDTO.class);
        AdoptionRequestDTO adoptionRequestDTO1 = new AdoptionRequestDTO();
        adoptionRequestDTO1.setId("id1");
        AdoptionRequestDTO adoptionRequestDTO2 = new AdoptionRequestDTO();
        assertThat(adoptionRequestDTO1).isNotEqualTo(adoptionRequestDTO2);
        adoptionRequestDTO2.setId(adoptionRequestDTO1.getId());
        assertThat(adoptionRequestDTO1).isEqualTo(adoptionRequestDTO2);
        adoptionRequestDTO2.setId("id2");
        assertThat(adoptionRequestDTO1).isNotEqualTo(adoptionRequestDTO2);
        adoptionRequestDTO1.setId(null);
        assertThat(adoptionRequestDTO1).isNotEqualTo(adoptionRequestDTO2);
    }
}
