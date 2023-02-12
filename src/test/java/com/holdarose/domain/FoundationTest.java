package com.holdarose.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.holdarose.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoundationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Foundation.class);
        Foundation foundation1 = new Foundation();
        foundation1.setId("id1");
        Foundation foundation2 = new Foundation();
        foundation2.setId(foundation1.getId());
        assertThat(foundation1).isEqualTo(foundation2);
        foundation2.setId("id2");
        assertThat(foundation1).isNotEqualTo(foundation2);
        foundation1.setId(null);
        assertThat(foundation1).isNotEqualTo(foundation2);
    }
}
