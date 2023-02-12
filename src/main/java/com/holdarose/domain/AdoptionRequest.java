package com.holdarose.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AdoptionRequest.
 */
@Document(collection = "adoption_request")
public class AdoptionRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("child_name")
    private String childName;

    @NotNull
    @Field("cnic")
    private String cnic;

    @Field("foster_name")
    private String fosterName;

    @Field("foster_job_title")
    private String fosterJobTitle;

    @Field("foster_address")
    private String fosterAddress;

    @Field("approved")
    private Boolean approved;

    @NotNull
    @Field("foundation_name")
    private String foundationName;

    @DBRef
    @Field("child")
    private Child child;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public AdoptionRequest id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChildName() {
        return this.childName;
    }

    public AdoptionRequest childName(String childName) {
        this.setChildName(childName);
        return this;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getCnic() {
        return this.cnic;
    }

    public AdoptionRequest cnic(String cnic) {
        this.setCnic(cnic);
        return this;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getFosterName() {
        return this.fosterName;
    }

    public AdoptionRequest fosterName(String fosterName) {
        this.setFosterName(fosterName);
        return this;
    }

    public void setFosterName(String fosterName) {
        this.fosterName = fosterName;
    }

    public String getFosterJobTitle() {
        return this.fosterJobTitle;
    }

    public AdoptionRequest fosterJobTitle(String fosterJobTitle) {
        this.setFosterJobTitle(fosterJobTitle);
        return this;
    }

    public void setFosterJobTitle(String fosterJobTitle) {
        this.fosterJobTitle = fosterJobTitle;
    }

    public String getFosterAddress() {
        return this.fosterAddress;
    }

    public AdoptionRequest fosterAddress(String fosterAddress) {
        this.setFosterAddress(fosterAddress);
        return this;
    }

    public void setFosterAddress(String fosterAddress) {
        this.fosterAddress = fosterAddress;
    }

    public Boolean getApproved() {
        return this.approved;
    }

    public AdoptionRequest approved(Boolean approved) {
        this.setApproved(approved);
        return this;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getFoundationName() {
        return this.foundationName;
    }

    public AdoptionRequest foundationName(String foundationName) {
        this.setFoundationName(foundationName);
        return this;
    }

    public void setFoundationName(String foundationName) {
        this.foundationName = foundationName;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public AdoptionRequest child(Child child) {
        this.setChild(child);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdoptionRequest)) {
            return false;
        }
        return id != null && id.equals(((AdoptionRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdoptionRequest{" +
            "id=" + getId() +
            ", childName='" + getChildName() + "'" +
            ", cnic='" + getCnic() + "'" +
            ", fosterName='" + getFosterName() + "'" +
            ", fosterJobTitle='" + getFosterJobTitle() + "'" +
            ", fosterAddress='" + getFosterAddress() + "'" +
            ", approved='" + getApproved() + "'" +
            ", foundationName='" + getFoundationName() + "'" +
            "}";
    }
}
