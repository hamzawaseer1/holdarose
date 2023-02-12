package com.holdarose.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.holdarose.domain.AdoptionRequest} entity.
 */
public class AdoptionRequestDTO implements Serializable {

    private String id;

    @NotNull
    private String childName;

    @NotNull
    private String cnic;

    private String fosterName;

    private String fosterJobTitle;

    private String fosterAddress;

    private Boolean approved;

    @NotNull
    private String foundationName;

    private ChildDTO child;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getFosterName() {
        return fosterName;
    }

    public void setFosterName(String fosterName) {
        this.fosterName = fosterName;
    }

    public String getFosterJobTitle() {
        return fosterJobTitle;
    }

    public void setFosterJobTitle(String fosterJobTitle) {
        this.fosterJobTitle = fosterJobTitle;
    }

    public String getFosterAddress() {
        return fosterAddress;
    }

    public void setFosterAddress(String fosterAddress) {
        this.fosterAddress = fosterAddress;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getFoundationName() {
        return foundationName;
    }

    public void setFoundationName(String foundationName) {
        this.foundationName = foundationName;
    }

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdoptionRequestDTO)) {
            return false;
        }

        AdoptionRequestDTO adoptionRequestDTO = (AdoptionRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adoptionRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdoptionRequestDTO{" +
            "id='" + getId() + "'" +
            ", childName='" + getChildName() + "'" +
            ", cnic='" + getCnic() + "'" +
            ", fosterName='" + getFosterName() + "'" +
            ", fosterJobTitle='" + getFosterJobTitle() + "'" +
            ", fosterAddress='" + getFosterAddress() + "'" +
            ", approved='" + getApproved() + "'" +
            ", foundationName='" + getFoundationName() + "'" +
            ", child=" + getChild() +
            "}";
    }
}
