package at.jku.swe.simcomp.azureadapter.service.AzureController;

import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DigitalTwinsClientBuilder;
import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DigitalTwinsModelData;
import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import com.microsoft.azure.sdk.iot.service.digitaltwin.DigitalTwinClient;
import reactor.core.publisher.Mono;

public class AzureController {

    public static void createDigitalTwin(String endpoint, String credential, String twinId, String twinPayload) {
        DigitalTwinClient client = new DigitalTwinsClientBuilder()
                .credential(new TokenCredential() {
                    @Override
                    public Mono<AccessToken> getToken(TokenRequestContext tokenRequestContext) {
                        return null;
                    }

                    @Override
                    public AccessToken getTokenSync(TokenRequestContext request) {
                        return TokenCredential.super.getTokenSync(request);
                    }
                })
                .endpoint(endpoint)
                .buildClient();

        try {
            DigitalTwinsModelData createdTwin =  client.getDigitalTwin(twinId, DigitalTwinsModelData.class);
            System.out.println("Created twin: " + createdTwin);
        } catch (Exception e) {
            System.err.println("Error creating digital twin: " + e.getMessage());
        }
    }

    public static void deleteDigitalTwin(String endpoint, String credential, String twinId) {
        DigitalTwinClient client = createDigitalTwinClient(endpoint, credential);
        /*
         * TODO: Implement function.
         */
        /*try {
            client.deleteDigitalTwinWithResponse(twinId, null, Context.DNS_URL);
            System.out.println("Deleted twin with ID: " + twinId);
        } catch (DigitalTwinsException e) {
            System.err.println("Error deleting digital twin: " + e.getMessage());
        }*/
    }

    private static DigitalTwinClient createDigitalTwinClient(String endpoint, String credential) {
        return new DigitalTwinsClientBuilder()
                .credential(new TokenCredential() {
                    @Override
                    public Mono<AccessToken> getToken(TokenRequestContext tokenRequestContext) {
                        return null; // Placeholder; replace this with actual token retrieval logic
                    }

                    @Override
                    public AccessToken getTokenSync(TokenRequestContext request) {
                        return TokenCredential.super.getTokenSync(request);
                    }
                })
                .endpoint(endpoint)
                .buildClient();
    }

}
