package com.holdarose.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.holdarose.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FostersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fosters.class);
        Fosters fosters1 = new Fosters();
        fosters1.setId("id1");
        Fosters fosters2 = new Fosters();
        fosters2.setId(fosters1.getId());
        assertThat(fosters1).isEqualTo(fosters2);
        fosters2.setId("id2");
        assertThat(fosters1).isNotEqualTo(fosters2);
        fosters1.setId(null);
        assertThat(fosters1).isNotEqualTo(fosters2);
    }
}
