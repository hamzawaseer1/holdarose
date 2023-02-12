package com.holdarose.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.holdarose.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdoptionRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdoptionRequest.class);
        AdoptionRequest adoptionRequest1 = new AdoptionRequest();
        adoptionRequest1.setId("id1");
        AdoptionRequest adoptionRequest2 = new AdoptionRequest();
        adoptionRequest2.setId(adoptionRequest1.getId());
        assertThat(adoptionRequest1).isEqualTo(adoptionRequest2);
        adoptionRequest2.setId("id2");
        assertThat(adoptionRequest1).isNotEqualTo(adoptionRequest2);
        adoptionRequest1.setId(null);
        assertThat(adoptionRequest1).isNotEqualTo(adoptionRequest2);
    }
}
