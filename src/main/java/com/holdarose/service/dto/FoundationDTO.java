package com.holdarose.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.holdarose.domain.Foundation} entity.
 */
public class FoundationDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    private String description;

    private String location;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoundationDTO)) {
            return false;
        }

        FoundationDTO foundationDTO = (FoundationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, foundationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoundationDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
