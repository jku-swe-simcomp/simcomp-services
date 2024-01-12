package at.jku.swe.simcomp.azureadapter.service.Services;

import com.azure.digitaltwins.core.*;

import static at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService.buildConnection;

public class AzureService {

    private final static DigitalTwinsClient client = buildConnection();

    private final static String modelid = "dtmi:com:example:NiryoOne;1";


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
        deleteDigitalTwin("test");

    }

}
