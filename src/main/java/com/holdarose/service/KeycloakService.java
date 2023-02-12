package com.holdarose.service;

import com.holdarose.domain.Foundation;
import com.holdarose.security.AuthoritiesConstants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakService {
    private static final Logger log = LoggerFactory.getLogger(KeycloakService.class);

    private static final String INITIAL_PASSWORD = "12345";

    private final RealmResource realm;

    public KeycloakService(RealmResource realmResource) {
        this.realm = realmResource;
    }

    public void addUser(Foundation foundation) {

        if (foundation != null) {
            try {
                UserRepresentation user = new UserRepresentation();
                user.setUsername(foundation.getName());
                user.setEmail(foundation.getEmail());
                user.setFirstName(foundation.getName());

                user.setEnabled(true);
                user.setEmailVerified(true);

                user.setCredentials(createInitialCredentials());
                this.realm.users().create(user);

                user = this.realm.users().search(user.getUsername()).get(0);

                List<RoleRepresentation> roleRepresentations = new ArrayList<>();
                roleRepresentations.add(this.realm.roles().get(AuthoritiesConstants.FOUNDATION_ADMIN).toRepresentation());

                RoleMappingResource roleMappingResource = this.realm.users().get(user.getId()).roles();
                roleMappingResource.realmLevel().add(roleRepresentations);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    private List<CredentialRepresentation> createInitialCredentials() {
        List<CredentialRepresentation> credentials = new ArrayList<>();
        CredentialRepresentation cr = new CredentialRepresentation();
        cr.setType(CredentialRepresentation.PASSWORD);
        cr.setValue(INITIAL_PASSWORD);
        cr.setTemporary(true);
        credentials.add(cr);
        return credentials;
    }

    public void deleteUserFromKeycloak(String username){
        UserRepresentation userRepresentation = this.realm.users().search(username).get(0);
        this.realm.users().delete(userRepresentation.getId());
    }
}
