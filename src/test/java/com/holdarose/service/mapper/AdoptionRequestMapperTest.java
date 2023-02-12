package com.holdarose.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdoptionRequestMapperTest {

    private AdoptionRequestMapper adoptionRequestMapper;

    @BeforeEach
    public void setUp() {
        adoptionRequestMapper = new AdoptionRequestMapperImpl();
    }
}
