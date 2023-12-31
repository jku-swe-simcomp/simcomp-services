package at.jku.swe.simcomp.azureadapter.service.Services;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import reactor.core.publisher.Mono;

public class AzureService {

    public static void createDigitalTwin(String endpoint, String clientId, String clientSecret, String tenantId, String twinId, String twinPayload) {
        DigitalTwinsClient client = createDigitalTwinsClient(endpoint, clientId, clientSecret, tenantId);

        try {
            client.createOrReplaceDigitalTwin(twinId, null, null);
            System.out.println("Created twin with ID: " + twinId);
        } catch (Exception e) {
            System.err.println("Error creating digital twin: " + e.getMessage());
        }
    }

    public static void deleteDigitalTwin(String endpoint, String clientId, String clientSecret, String tenantId, String twinId) {
        DigitalTwinsClient client = createDigitalTwinsClient(endpoint, clientId, clientSecret, tenantId);

        try {
            client.deleteDigitalTwin(twinId);
            System.out.println("Deleted twin with ID: " + twinId);
        } catch (Exception e) {
            System.err.println("Error deleting digital twin: " + e.getMessage());
        }
    }

    private static DigitalTwinsClient createDigitalTwinsClient(String endpoint, String clientId, String clientSecret, String tenantId) {
        TokenCredential credential = new TokenCredential() {
            private final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .tenantId(tenantId)
                    .build();

            @Override
            public Mono<AccessToken> getToken(TokenRequestContext tokenRequestContext) {
                return clientSecretCredential.getToken(tokenRequestContext).flatMap(Mono::just);
            }

            @Override
            public AccessToken getTokenSync(TokenRequestContext request) {
                return clientSecretCredential.getToken(request).block();
            }
        };

        return new DigitalTwinsClientBuilder()
                .credential(credential)
                .endpoint(endpoint)
                .buildClient();
    }
}
