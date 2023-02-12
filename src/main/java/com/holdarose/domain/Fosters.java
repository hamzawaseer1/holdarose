package com.holdarose.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Fosters.
 */
@Document(collection = "fosters")
public class Fosters implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("cnic")
    private String cnic;

    @Field("email")
    private String email;

    @Field("job_title")
    private String jobTitle;

    @Field("location")
    private String location;

    @DBRef
    @Field("child")
    @JsonIgnoreProperties(value = { "fosters", "adoptionRequest", "foundation" }, allowSetters = true)
    private Child child;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Fosters id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Fosters name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return this.cnic;
    }

    public Fosters cnic(String cnic) {
        this.setCnic(cnic);
        return this;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getEmail() {
        return this.email;
    }

    public Fosters email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public Fosters jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return this.location;
    }

    public Fosters location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Fosters child(Child child) {
        this.setChild(child);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fosters)) {
            return false;
        }
        return id != null && id.equals(((Fosters) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fosters{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cnic='" + getCnic() + "'" +
            ", email='" + getEmail() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
