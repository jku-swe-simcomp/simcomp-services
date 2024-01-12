package at.jku.swe.simcomp.azureadapter.service.Services;

import com.azure.core.credential.TokenCredential;
import com.azure.digitaltwins.core.*;
import com.azure.digitaltwins.core.implementation.models.ErrorResponseException;
import com.azure.digitaltwins.core.models.DigitalTwinsModelData;
import com.azure.identity.ClientSecretCredentialBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService.buildConnection;

public class AzureService {

    private final static DigitalTwinsClient client = buildConnection();

    private final static String modelid = "dtmi:com:example:NiryoOne;1";

    static final String tentantId = "f11d36c0-5880-41f7-91e5-ac5e42209e77";
    static final String clientId = "a39ddd8f-18bf-41b2-9ab9-fea69e235b86";
    static final String clientSecret = "unI8Q~MP2uwUGg38dOu4ASnrmcCYNC19fCAKPc9r";
    static final String endpoint = "https://Student.api.wcus.digitaltwins.azure.net";


    public static void createDigitalTwin(String basicDigitalTwinId) {
        BasicDigitalTwin basicTwin = new BasicDigitalTwin(basicDigitalTwinId)
                .setMetadata(
                        new BasicDigitalTwinMetadata()
                                .setModelId(modelid)
                )
                .addToContents("joint1_angle", 0.0)
                .addToContents("joint2_angle", 0.0)
                .addToContents("joint3_angle", 0.0)
                .addToContents("joint4_angle", 0.0)
                .addToContents("joint5_angle", 0.0)
                .addToContents("joint6_angle", 0.0);

        client.createOrReplaceDigitalTwin(basicDigitalTwinId, basicTwin, BasicDigitalTwin.class);
    }

    public static void deleteDigitalTwin(String twinId) {
        client.deleteDigitalTwin(twinId);
    }

    public static void main(String[] args) {
        createDigitalTwin("test");

    }

}
