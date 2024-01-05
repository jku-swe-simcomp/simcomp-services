package at.jku.swe.simcomp.azureadapter.service.NiryoOneController;

import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DigitalTwinsServiceVersion;
import at.jku.swe.simcomp.azureadapter.service.NiryoOneModel.NiryoOneModel;
import com.azure.core.http.rest.PagedIterable;
import com.azure.core.models.JsonPatchDocument;
import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.digitaltwins.core.models.DigitalTwinsModelData;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.microsoft.azure.sdk.iot.service.digitaltwin.serialization.BasicDigitalTwin;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AzureExecutionService {

    private static DigitalTwinsClient client;
    private static String digitalTwinId;


    public AzureExecutionService(DigitalTwinsClient client, String digitalTwinId) {
        this.client = client;
        this.digitalTwinId = digitalTwinId;
    }


    public static DigitalTwinsClient buildConnection() {
        /*
         * NiryoOne Azure App.
         */
        return new DigitalTwinsClientBuilder()
                .credential(
                        new ClientSecretCredentialBuilder()
                                .tenantId("f11d36c0-5880-41f7-91e5-ac5e42209e77")
                                .clientId("a39ddd8f-18bf-41b2-9ab9-fea69e235b86")
                                .clientSecret("unI8Q~MP2uwUGg38dOu4ASnrmcCYNC19fCAKPc9r")
                                .build()
                )
                .endpoint("https://login.microsoftonline.com/organizations/oauth2/v2.0/authorize")
                //https://login.microsoftonline.com/consumers/oauth2/v2.0/token
                //https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize
                .buildClient();
    }


    public void setJointAngle(String jointName, double angle) {
        try {
            String propertyPath = getPropertyPathForJoint(jointName);
            if (propertyPath.isEmpty()) {
                System.out.println("Invalid joint name!");
            }

            JsonPatchDocument patchDocument = new JsonPatchDocument();
            patchDocument.appendReplace(propertyPath, angle);
            client.updateDigitalTwin(digitalTwinId, patchDocument);

            System.out.println("Joint angle for " + jointName + " updated successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getPropertyPathForJoint(String jointName) {
        switch (jointName) {
            case "joint1_angle":
                return "path.to.joint1.property";
            case "joint2_angle":
                return "path.to.joint2.property";
            case "joint3_angle":
                return "path.to.joint3.property";
            case "joint4_angle":
                return "path.to.joint4.property";
            case "joint5_angle":
                return "path.to.joint5.property";
            case "joint6_angle":
                return "path.to.joint6.property";
            default:
                throw new IllegalArgumentException("Unknown joint name: " + jointName);
        }
    }

    public static List<Double> getAllJointAngles() {
        try {
            NiryoOneModel niryoOneModel = client.getDigitalTwin(digitalTwinId, NiryoOneModel.class);
            List<Double> niryoOneAngles = null;
            niryoOneAngles.add(niryoOneModel.getJoint1Angle());
            niryoOneAngles.add(niryoOneModel.getJoint2Angle());
            niryoOneAngles.add(niryoOneModel.getJoint3Angle());
            niryoOneAngles.add(niryoOneModel.getJoint4Angle());
            niryoOneAngles.add(niryoOneModel.getJoint5Angle());
            return niryoOneAngles;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public double getJointAngle(String jointName) {
        try {
            BasicDigitalTwin test = client.getDigitalTwin(digitalTwinId, BasicDigitalTwin.class);
            NiryoOneModel niryoOneModel = client.getDigitalTwin(digitalTwinId, NiryoOneModel.class);

            switch (jointName.toLowerCase()) {
                case "joint1_angle":
                    return niryoOneModel.getJoint1Angle();
                case "joint2_angle":
                    return niryoOneModel.getJoint2Angle();
                case "joint3_angle":
                    return niryoOneModel.getJoint3Angle();
                case "joint4_angle":
                    return niryoOneModel.getJoint4Angle();
                case "joint5_angle":
                    return niryoOneModel.getJoint5Angle();
                case "joint6_angle":
                    return niryoOneModel.getJoint6Angle();
                default:
                    System.out.println("Invalid joint name!");
                    return 0.0;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void setJointAngles(double joint1, double joint2, double joint3, double joint4, double joint5, double joint6) {
        try {
            JsonPatchDocument patchDocument = new JsonPatchDocument();
            patchDocument.appendReplace("/joint1_angle", joint1);
            patchDocument.appendReplace("/joint2_angle", joint2);
            patchDocument.appendReplace("/joint3_angle", joint3);
            patchDocument.appendReplace("/joint4_angle", joint4);
            patchDocument.appendReplace("/joint5_angle", joint5);
            patchDocument.appendReplace("/joint6_angle", joint6);;
            client.updateDigitalTwin(digitalTwinId, patchDocument);

            System.out.println("Joint angles updated successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String digitalTwinId = "dtmi:com:example:NiryoOne;1";

        DigitalTwinsClient digitalTwinsClient = buildConnection();

        PagedIterable<DigitalTwinsModelData> data = digitalTwinsClient.listModels();
        data.stream();

         /*for (DigitalTwinsModelData data : digitalTwinsClient.listModels()) {
             System.out.println(data.toString());
         }

        BasicDigitalTwin basicTwinResult = digitalTwinsClient.getDigitalTwin(
                digitalTwinId,
                BasicDigitalTwin.class);
        System.out.println(basicTwinResult.toString());

        DigitalTwinsModelData model = digitalTwinsClient.getModel(digitalTwinId);

        AzureExecutionService niryoOneController = new AzureExecutionService(digitalTwinsClient, digitalTwinId);

        double joint1Angle = niryoOneController.getJointAngle("joint1_angle");
        System.out.println("Joint 1: " + joint1Angle);

        double joint2Angle = niryoOneController.getJointAngle("joint2_angle");
        System.out.println("Joint 2: " + joint2Angle);

        niryoOneController.setJointAngle("joint1_angle", 45.0);
        niryoOneController.setJointAngle("joint2_angle", 30.0);

        joint1Angle = niryoOneController.getJointAngle("joint1");
        System.out.println("Angle für Joint 1: " + joint1Angle);

        joint2Angle = niryoOneController.getJointAngle("joint2");
        System.out.println("Angle für Joint 2: " + joint2Angle);

        niryoOneController.setJointAngles(90.0, 60.0, 45.0, 30.0, 75.0, 120.0);

        getAllJointAngles();*/
    }

}
