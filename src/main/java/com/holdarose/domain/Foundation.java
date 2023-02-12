package com.holdarose.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Foundation.
 */
@Document(collection = "foundation")
public class Foundation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("email")
    private String email;

    @Field("description")
    private String description;

    @Field("location")
    private String location;

    @DBRef
    @Field("donation")
    @JsonIgnoreProperties(value = { "foundation" }, allowSetters = true)
    private Set<Donation> donations = new HashSet<>();

    @DBRef
    @Field("child")
    @JsonIgnoreProperties(value = { "fosters", "adoptionRequest", "foundation" }, allowSetters = true)
    private Set<Child> children = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Foundation id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Foundation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Foundation email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return this.description;
    }

    public Foundation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public Foundation location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Donation> getDonations() {
        return this.donations;
    }

    public void setDonations(Set<Donation> donations) {
        if (this.donations != null) {
            this.donations.forEach(i -> i.setFoundation(null));
        }
        if (donations != null) {
            donations.forEach(i -> i.setFoundation(this));
        }
        this.donations = donations;
    }

    public Foundation donations(Set<Donation> donations) {
        this.setDonations(donations);
        return this;
    }

    public Foundation addDonation(Donation donation) {
        this.donations.add(donation);
        donation.setFoundation(this);
        return this;
    }

    public Foundation removeDonation(Donation donation) {
        this.donations.remove(donation);
        donation.setFoundation(null);
        return this;
    }

    public Set<Child> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Child> children) {
        if (this.children != null) {
            this.children.forEach(i -> i.setFoundation(null));
        }
        if (children != null) {
            children.forEach(i -> i.setFoundation(this));
        }
        this.children = children;
    }

    public Foundation children(Set<Child> children) {
        this.setChildren(children);
        return this;
    }

    public Foundation addChild(Child child) {
        this.children.add(child);
        child.setFoundation(this);
        return this;
    }

    public Foundation removeChild(Child child) {
        this.children.remove(child);
        child.setFoundation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Foundation)) {
            return false;
        }
        return id != null && id.equals(((Foundation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Foundation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
