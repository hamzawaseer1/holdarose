package com.holdarose.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.holdarose.domain.enumeration.Gender;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Child.
 */
@Document(collection = "child")
public class Child implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("age")
    private Integer age;

    @Field("image")
    private byte[] image;

    @Field("image_content_type")
    private String imageContentType;

    @Field("gender")
    private Gender gender;

    @DBRef
    @Field("fosters")
    @JsonIgnoreProperties(value = { "child" }, allowSetters = true)
    private Set<Fosters> fosters = new HashSet<>();

    @DBRef
    private AdoptionRequest adoptionRequest;

    @DBRef
    @Field("foundation")
    @JsonIgnoreProperties(value = { "donations", "children" }, allowSetters = true)
    private Foundation foundation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Child id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Child name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public Child age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Child image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Child imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Child gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<Fosters> getFosters() {
        return this.fosters;
    }

    public void setFosters(Set<Fosters> fosters) {
        if (this.fosters != null) {
            this.fosters.forEach(i -> i.setChild(null));
        }
        if (fosters != null) {
            fosters.forEach(i -> i.setChild(this));
        }
        this.fosters = fosters;
    }

    public Child fosters(Set<Fosters> fosters) {
        this.setFosters(fosters);
        return this;
    }

    public Child addFosters(Fosters fosters) {
        this.fosters.add(fosters);
        fosters.setChild(this);
        return this;
    }

    public Child removeFosters(Fosters fosters) {
        this.fosters.remove(fosters);
        fosters.setChild(null);
        return this;
    }

    public AdoptionRequest getAdoptionRequest() {
        return this.adoptionRequest;
    }

    public void setAdoptionRequest(AdoptionRequest adoptionRequest) {
        if (this.adoptionRequest != null) {
            this.adoptionRequest.setChild(null);
        }
        if (adoptionRequest != null) {
            adoptionRequest.setChild(this);
        }
        this.adoptionRequest = adoptionRequest;
    }

    public Child adoptionRequest(AdoptionRequest adoptionRequest) {
        this.setAdoptionRequest(adoptionRequest);
        return this;
    }

    public Foundation getFoundation() {
        return this.foundation;
    }

    public void setFoundation(Foundation foundation) {
        this.foundation = foundation;
    }

    public Child foundation(Foundation foundation) {
        this.setFoundation(foundation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Child)) {
            return false;
        }
        return id != null && id.equals(((Child) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Child{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
