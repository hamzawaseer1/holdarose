package com.holdarose.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.holdarose.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoundationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoundationDTO.class);
        FoundationDTO foundationDTO1 = new FoundationDTO();
        foundationDTO1.setId("id1");
        FoundationDTO foundationDTO2 = new FoundationDTO();
        assertThat(foundationDTO1).isNotEqualTo(foundationDTO2);
        foundationDTO2.setId(foundationDTO1.getId());
        assertThat(foundationDTO1).isEqualTo(foundationDTO2);
        foundationDTO2.setId("id2");
        assertThat(foundationDTO1).isNotEqualTo(foundationDTO2);
        foundationDTO1.setId(null);
        assertThat(foundationDTO1).isNotEqualTo(foundationDTO2);
    }
}
