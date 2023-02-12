package com.holdarose.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.holdarose.domain.enumeration.PaymentMethod;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Donation.
 */
@Document(collection = "donation")
public class Donation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @NotNull
    @Field("cnic")
    private String cnic;

    @Field("address")
    private String address;

    @Field("foundation_name")
    private String foundationName;

    @Field("donation_amount")
    private String donationAmount;

    @Field("payment_method")
    private PaymentMethod paymentMethod;

    @DBRef
    @Field("foundation")
    @JsonIgnoreProperties(value = { "donations", "children" }, allowSetters = true)
    private Foundation foundation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Donation id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Donation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return this.cnic;
    }

    public Donation cnic(String cnic) {
        this.setCnic(cnic);
        return this;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getAddress() {
        return this.address;
    }

    public Donation address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFoundationName() {
        return this.foundationName;
    }

    public Donation foundationName(String foundationName) {
        this.setFoundationName(foundationName);
        return this;
    }

    public void setFoundationName(String foundationName) {
        this.foundationName = foundationName;
    }

    public String getDonationAmount() {
        return this.donationAmount;
    }

    public Donation donationAmount(String donationAmount) {
        this.setDonationAmount(donationAmount);
        return this;
    }

    public void setDonationAmount(String donationAmount) {
        this.donationAmount = donationAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public Donation paymentMethod(PaymentMethod paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Foundation getFoundation() {
        return this.foundation;
    }

    public void setFoundation(Foundation foundation) {
        this.foundation = foundation;
    }

    public Donation foundation(Foundation foundation) {
        this.setFoundation(foundation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Donation)) {
            return false;
        }
        return id != null && id.equals(((Donation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Donation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cnic='" + getCnic() + "'" +
            ", address='" + getAddress() + "'" +
            ", foundationName='" + getFoundationName() + "'" +
            ", donationAmount='" + getDonationAmount() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            "}";
    }
}
