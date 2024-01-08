package at.jku.swe.simcomp.azureadapter.service.Services;

import com.azure.core.credential.TokenCredential;
import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.digitaltwins.core.implementation.models.ErrorResponseException;
import com.azure.digitaltwins.core.models.DigitalTwinsModelData;
import com.azure.identity.ClientSecretCredentialBuilder;

public class AzureService {

    public static Iterable<DigitalTwinsModelData> createModels(String endpoint, String clientId, String clientSecret, String tenantId, Iterable<String> dtdlModels) {
        DigitalTwinsClient digitalTwinsClient = new DigitalTwinsClientBuilder()
                .credential(new ClientSecretCredentialBuilder()
                        .tenantId(tenantId)
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .build())
                .endpoint(endpoint)
                .buildClient();

        return digitalTwinsClient.createModels(dtdlModels);
    }

    public static void deleteDigitalTwin(String endpoint, String clientId, String clientSecret, String tenantId, String twinId) {
        TokenCredential credential = new ClientSecretCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        DigitalTwinsClient digitalTwinsClient = new DigitalTwinsClientBuilder()
                .credential(credential)
                .endpoint(endpoint)
                .buildClient();

        try {
            digitalTwinsClient.deleteDigitalTwin(twinId);
            System.out.printf("Digital Twin '%s' wurde erfolgreich gelöscht.%n", twinId);
        } catch (ErrorResponseException ex) {
            System.out.printf("Fehler beim Löschen des Digital Twins '%s': %s%n", twinId, ex.getMessage());
        }
    }

}
