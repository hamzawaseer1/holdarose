package com.holdarose.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoundationMapperTest {

    private FoundationMapper foundationMapper;

    @BeforeEach
    public void setUp() {
        foundationMapper = new FoundationMapperImpl();
    }
}
