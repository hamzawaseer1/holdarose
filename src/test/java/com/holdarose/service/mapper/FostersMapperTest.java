package com.holdarose.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FostersMapperTest {

    private FostersMapper fostersMapper;

    @BeforeEach
    public void setUp() {
        fostersMapper = new FostersMapperImpl();
    }
}
