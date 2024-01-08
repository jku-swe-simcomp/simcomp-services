package at.jku.swe.simcomp.azureadapter.service.NiryoOneController;

import com.azure.core.models.JsonPatchDocument;
import com.azure.core.util.logging.ClientLogger;
import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.identity.ClientSecretCredentialBuilder;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AzureExecutionService {

    private static DigitalTwinsClient client = buildConnection();
    private static String digitalTwinId = "dtmi:com:example:NiryoOne;1";

    static final String tentantId = "f11d36c0-5880-41f7-91e5-ac5e42209e77";
    static final String clientId = "a39ddd8f-18bf-41b2-9ab9-fea69e235b86";
    static final String clientSecret = "unI8Q~MP2uwUGg38dOu4ASnrmcCYNC19fCAKPc9r";
    static final String endpoint = "https://Student.api.wcus.digitaltwins.azure.net";


    public static DigitalTwinsClient buildConnection() {
        /*
         * NiryoOne Azure App.
         */
        return new DigitalTwinsClientBuilder()
                .credential(
                        new ClientSecretCredentialBuilder()
                                .tenantId(tentantId)
                                .clientId(clientId)
                                .clientSecret(clientSecret)
                                .build()
                )
                .endpoint(endpoint)
                .buildClient();
    }


    public static void setJointAngle(String digitalTwinId, String jointName, double angle) {
        try {
            if (client == null) {
                client = buildConnection();
            }

            String propertyPath = "joint" + jointName + "_angle";
            JsonPatchDocument patchDocument = new JsonPatchDocument();
            patchDocument.appendReplace(propertyPath, angle);

            client.updateDigitalTwin(digitalTwinId, patchDocument);

            System.out.printf("The angle of the joint '%s' was successfully set to %.2f degrees.%n", jointName, angle);

        } catch (Exception ex) {
            ClientLogger logger = new ClientLogger(AzureExecutionService.class);
            logger.error("Error when setting the joint angle: " + ex.getMessage());
        }
    }

    private String getPropertyPathForJoint(String jointName) {
        return "joint" + jointName + "_angle";
    }

    public static List<Double> getAllJointAngles() {
        try {
            if (client == null) {
                client = buildConnection();
            }

            String model = client.getModel(digitalTwinId).getDtdlModel();
            JSONObject jsonObject = new JSONObject(Integer.parseInt(model));

            List<Double> jointAngles = new ArrayList<>();
            String[] jointNames = {"joint1_angle", "joint2_angle", "joint3_angle", "joint4_angle", "joint5_angle", "joint6_angle"};

            for (String jointName : jointNames) {
                if (jsonObject.containsKey(jointName)) {
                    double angle = Double.parseDouble(jsonObject.getAsString(jointName));
                    jointAngles.add(angle);
                }
            }

            return jointAngles;

        } catch (Exception ex) {
            ClientLogger logger = new ClientLogger(AzureExecutionService.class);
            logger.error("Error when retrieving the joint angles: " + ex.getMessage());
            return Collections.emptyList();
        }
    }



    public double getJointAngle(String jointName) {
        double angle = 0.0;
        try {
            if (client == null) {
                client = buildConnection();
            }

            String model = client.getModel(digitalTwinId).getDtdlModel();
            JSONObject jsonObject = new JSONObject(Integer.parseInt(model));

            switch (jointName) {
            case "joint1_angle":
                angle = (double) jsonObject.get("joint1_angle");
            case "joint2_angle":
                angle = (double) jsonObject.get("joint2_angle");
            case "joint3_angle":
                angle = (double) jsonObject.get("joint3_angle");
            case "joint4_angle":
                angle = (double) jsonObject.get("joint4_angle");
            case "joint5_angle":
                angle = (double) jsonObject.get("joint5_angle");
            case "joint6_angle":
                angle = (double) jsonObject.get("joint6_angle");
            default:
                angle = 0.0;
            }

        } catch (Exception ex) {
            ClientLogger logger = new ClientLogger(AzureExecutionService.class);
            logger.error("Error when retrieving the joint angles: " + ex.getMessage());
        }
        return angle;
    }


    public void setJointAngles(List<Double> jointAngles) {
        try {
            if (client == null) {
                client = buildConnection();
            }

            for (int i = 0; i < 6 && i < jointAngles.size(); i++) {
                double angle = jointAngles.get(i);

                String propertyPath = "joint" + (i + 1) + "_angle";
                JsonPatchDocument patchDocument = new JsonPatchDocument();
                patchDocument.appendReplace(propertyPath, angle);

                client.updateDigitalTwin(digitalTwinId, patchDocument);
            }

        } catch (Exception ex) {
            ClientLogger logger = new ClientLogger(AzureExecutionService.class);
            logger.error("Errors when setting the joint angles: " + ex.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {

    }

}
