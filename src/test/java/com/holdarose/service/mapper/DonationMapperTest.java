package com.holdarose.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DonationMapperTest {

    private DonationMapper donationMapper;

    @BeforeEach
    public void setUp() {
        donationMapper = new DonationMapperImpl();
    }
}
