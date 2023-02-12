package com.holdarose.service.impl;

import com.holdarose.domain.Donation;
import com.holdarose.repository.DonationRepository;
import com.holdarose.service.DonationService;
import com.holdarose.service.dto.DonationDTO;
import com.holdarose.service.mapper.DonationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Donation}.
 */
@Service
public class DonationServiceImpl implements DonationService {

    private final Logger log = LoggerFactory.getLogger(DonationServiceImpl.class);

    private final DonationRepository donationRepository;

    private final DonationMapper donationMapper;

    public DonationServiceImpl(DonationRepository donationRepository, DonationMapper donationMapper) {
        this.donationRepository = donationRepository;
        this.donationMapper = donationMapper;
    }

    @Override
    public DonationDTO save(DonationDTO donationDTO) {
        log.debug("Request to save Donation : {}", donationDTO);
        Donation donation = donationMapper.toEntity(donationDTO);
        donation = donationRepository.save(donation);
        return donationMapper.toDto(donation);
    }

    @Override
    public DonationDTO update(DonationDTO donationDTO) {
        log.debug("Request to save Donation : {}", donationDTO);
        Donation donation = donationMapper.toEntity(donationDTO);
        donation = donationRepository.save(donation);
        return donationMapper.toDto(donation);
    }

    @Override
    public Optional<DonationDTO> partialUpdate(DonationDTO donationDTO) {
        log.debug("Request to partially update Donation : {}", donationDTO);

        return donationRepository
            .findById(donationDTO.getId())
            .map(existingDonation -> {
                donationMapper.partialUpdate(existingDonation, donationDTO);

                return existingDonation;
            })
            .map(donationRepository::save)
            .map(donationMapper::toDto);
    }

    @Override
    public Page<DonationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Donations");
        return donationRepository.findAll(pageable).map(donationMapper::toDto);
    }

    @Override
    public Optional<DonationDTO> findOne(String id) {
        log.debug("Request to get Donation : {}", id);
        return donationRepository.findById(id).map(donationMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Donation : {}", id);
        donationRepository.deleteById(id);
    }
}
