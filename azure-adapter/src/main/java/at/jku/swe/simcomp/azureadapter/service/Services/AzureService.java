package at.jku.swe.simcomp.azureadapter.service.Services;

import com.azure.digitaltwins.core.*;
import static at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService.buildConnection;

/**
 * The AzureService class provides methods for interacting with Azure Digital Twins.
 */
public class AzureService {

    private final static DigitalTwinsClient client = buildConnection();

    private final static String modelId = "dtmi:com:example:NiryoOne;1";

    /**
     * Creates a new Digital Twin with the specified basicDigitalTwinId.
     *
     * @param basicDigitalTwinId The ID of the basic Digital Twin to be created.
     */
    public static void createDigitalTwin(String basicDigitalTwinId) {
        BasicDigitalTwin basicTwin = new BasicDigitalTwin(basicDigitalTwinId)
                .setMetadata(
                        new BasicDigitalTwinMetadata()
                                .setModelId(modelId)
                )
                .addToContents("joint1_angle", 0.0)
                .addToContents("joint2_angle", 0.0)
                .addToContents("joint3_angle", 0.0)
                .addToContents("joint4_angle", 0.0)
                .addToContents("joint5_angle", 0.0)
                .addToContents("joint6_angle", 0.0);

        client.createOrReplaceDigitalTwin(basicDigitalTwinId, basicTwin, BasicDigitalTwin.class);
    }

    /**
     * Deletes the Digital Twin with the specified twinId.
     *
     * @param twinId The ID of the Digital Twin to be deleted.
     */
    public static void deleteDigitalTwin(String twinId) {
        client.deleteDigitalTwin(twinId);
    }
}