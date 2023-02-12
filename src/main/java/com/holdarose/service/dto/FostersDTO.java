package com.holdarose.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.holdarose.domain.Fosters} entity.
 */
public class FostersDTO implements Serializable {

    private String id;

    private String name;

    private String cnic;

    private String email;

    private String jobTitle;

    private String location;

    private ChildDTO child;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        if (!(o instanceof FostersDTO)) {
            return false;
        }

        FostersDTO fostersDTO = (FostersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fostersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FostersDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", cnic='" + getCnic() + "'" +
            ", email='" + getEmail() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", location='" + getLocation() + "'" +
            ", child=" + getChild() +
            "}";
    }
}
