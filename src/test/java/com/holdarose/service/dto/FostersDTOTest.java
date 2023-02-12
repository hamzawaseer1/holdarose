package com.holdarose.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.holdarose.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FostersDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FostersDTO.class);
        FostersDTO fostersDTO1 = new FostersDTO();
        fostersDTO1.setId("id1");
        FostersDTO fostersDTO2 = new FostersDTO();
        assertThat(fostersDTO1).isNotEqualTo(fostersDTO2);
        fostersDTO2.setId(fostersDTO1.getId());
        assertThat(fostersDTO1).isEqualTo(fostersDTO2);
        fostersDTO2.setId("id2");
        assertThat(fostersDTO1).isNotEqualTo(fostersDTO2);
        fostersDTO1.setId(null);
        assertThat(fostersDTO1).isNotEqualTo(fostersDTO2);
    }
}
