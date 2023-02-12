package com.holdarose.service.dto;

import com.holdarose.domain.enumeration.Gender;
import com.holdarose.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.holdarose.domain.Child} entity.
 */
public class ChildDTO implements Serializable {

    private String id;

    private String name;

    private Integer age;

    private byte[] image;

    private String imageContentType;
    private Gender gender;

    private Status status;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        if (!(o instanceof ChildDTO)) {
            return false;
        }

        ChildDTO childDTO = (ChildDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, childDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChildDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", image='" + getImage() + "'" +
            ", gender='" + getGender() + "'" +
            ", status='" + getStatus() + "'" +
            ", foundation=" + getFoundation() +
            "}";
    }
}
