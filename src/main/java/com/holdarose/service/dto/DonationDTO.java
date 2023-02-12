package com.holdarose.service.dto;

import com.holdarose.domain.enumeration.PaymentMethod;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.holdarose.domain.Donation} entity.
 */
public class DonationDTO implements Serializable {

    private String id;

    private String name;

    @NotNull
    private String cnic;

    private String address;

    private String foundationName;

    private String donationAmount;

    private PaymentMethod paymentMethod;

    private FoundationDTO foundation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFoundationName() {
        return foundationName;
    }

    public void setFoundationName(String foundationName) {
        this.foundationName = foundationName;
    }

    public String getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(String donationAmount) {
        this.donationAmount = donationAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public FoundationDTO getFoundation() {
        return foundation;
    }

    public void setFoundation(FoundationDTO foundation) {
        this.foundation = foundation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DonationDTO)) {
            return false;
        }

        DonationDTO donationDTO = (DonationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, donationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonationDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", cnic='" + getCnic() + "'" +
            ", address='" + getAddress() + "'" +
            ", foundationName='" + getFoundationName() + "'" +
            ", donationAmount='" + getDonationAmount() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", foundation=" + getFoundation() +
            "}";
    }
}
