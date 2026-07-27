package mu.welldev.service.util;

import lombok.experimental.UtilityClass;
import org.keycloak.representations.idm.CredentialRepresentation;

@UtilityClass
public class Credentials {

    public CredentialRepresentation createCredentialRepresentation(String password) {
        var credential = new CredentialRepresentation();

        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        return credential;
    }
}
