package at.jku.swe.simcomp.azureadapter.service.NiryoOneController;

import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DigitalTwinsClientBuilder;
import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DigitalTwinsServiceVersion;
import at.jku.swe.simcomp.azureadapter.service.NiryoOneModel.NiryoOneModel;
import com.azure.core.models.JsonPatchDocument;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.microsoft.azure.sdk.iot.service.digitaltwin.DigitalTwinClient;
import com.microsoft.azure.sdk.iot.service.digitaltwin.serialization.BasicDigitalTwin;
import java.util.ArrayList;
import java.util.Collections;


public class NiryoOneController {

    private DigitalTwinClient client;
    private String digitalTwinId;


    public NiryoOneController(DigitalTwinClient client, String digitalTwinId) {
        this.client = client;
        this.digitalTwinId = digitalTwinId;
    }

    public NiryoOneController() {
    }

    public void buildConnection() {
        client = new DigitalTwinsClientBuilder()
                .credential(
                        new ClientSecretCredentialBuilder()
                                .tenantId("f11d36c0-5880-41f7-91e5-ac5e42209e77"/*TODO: Evaluate the correctness*/)
                                .clientId("ad79209a-1716-4df0-a8fa-e5028895c4f1"/*TODO: Evaluate the correctness*/)
                                .clientSecret("4Rn8Q~X5Hslc8I0rbgywK.um5q8oXpvCNp2WLaJR"/*TODO: Evaluate the correctness*/)
                                .build()
                )
                .endpoint("Endpoint=sb://iothub-ns-robothubst-25388492-678c22e1f2.servicebus.windows.net/;SharedAccessKeyName=iothubowner;SharedAccessKey=RcguWKFyDw4rPqFJBwSQq77heNWyXT6PLAIoTLuyH28=;EntityPath=robothubstudent"
                        /*TODO: Evaluate the correctness*/)
                .buildClient();
    }

    public void buildConnection2() {
        DefaultAzureCredentialBuilder credentialBuilder = new DefaultAzureCredentialBuilder();
        String digitalTwinsEndpoint = "Endpoint=sb://iothub-ns-robothubst-25388492-678c22e1f2.servicebus.windows.net/;SharedAccessKeyName=iothubowner;SharedAccessKey=RcguWKFyDw4rPqFJBwSQq77heNWyXT6PLAIoTLuyH28=;EntityPath=robothubstudent";
        DigitalTwinsClientBuilder clientBuilder = new DigitalTwinsClientBuilder()
                .credential(credentialBuilder.build())
                .endpoint(digitalTwinsEndpoint)
                .serviceVersion(DigitalTwinsServiceVersion.getLatest());
        DigitalTwinClient syncClient = clientBuilder.buildClient();
        try {
            BasicDigitalTwin digitalTwin = syncClient.getDigitalTwin("HostName=RobotHubStudent.azure-devices.net;DeviceId=TestRobot;SharedAccessKey=DLmglEWvJzNTjBdIHk9WW8xfHkhoeYvDPAIoTDYQHns="/*TODO: Evaluate the correctness*/, BasicDigitalTwin.class);
            System.out.println("Digital Twin retrieved: " + digitalTwin);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }


    public void setJointAngle(String jointName, double angle) {
        try {
            String propertyPath = getPropertyPathForJoint(jointName);
            if (propertyPath.isEmpty()) {
                System.out.println("Invalid joint name!");
                return;
            }

            JsonPatchDocument patchDocument = new JsonPatchDocument();
            patchDocument.appendReplace(propertyPath, angle);
            client.updateDigitalTwin(digitalTwinId, new ArrayList<>(Collections.singleton(patchDocument)));

            System.out.println("Joint angle for " + jointName + " updated successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getPropertyPathForJoint(String jointName) {
        switch (jointName) {
            case "joint1":
                return "path.to.joint1.property";
            case "joint2":
                return "path.to.joint2.property";
            case "joint3":
                return "path.to.joint3.property";
            case "joint4":
                return "path.to.joint4.property";
            case "joint5":
                return "path.to.joint5.property";
            case "joint6":
                return "path.to.joint6.property";
            default:
                throw new IllegalArgumentException("Unknown joint name: " + jointName);
        }
    }

    public void getAllJointAngles() {
        try {
            NiryoOneModel niryoOneModel = client.getDigitalTwin(digitalTwinId, NiryoOneModel.class);

            System.out.println("Joint 1 Angle: " + niryoOneModel.getJoint1Angle());
            System.out.println("Joint 2 Angle: " + niryoOneModel.getJoint2Angle());
            System.out.println("Joint 3 Angle: " + niryoOneModel.getJoint3Angle());
            System.out.println("Joint 4 Angle: " + niryoOneModel.getJoint4Angle());
            System.out.println("Joint 5 Angle: " + niryoOneModel.getJoint5Angle());
            System.out.println("Joint 6 Angle: " + niryoOneModel.getJoint6Angle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public double getJointAngle(String jointName) {
        try {
            NiryoOneModel niryoOneModel = client.getDigitalTwin(digitalTwinId, NiryoOneModel.class);

            switch (jointName.toLowerCase()) {
                case "joint1":
                    return niryoOneModel.getJoint1Angle();
                case "joint2":
                    return niryoOneModel.getJoint2Angle();
                case "joint3":
                    return niryoOneModel.getJoint3Angle();
                case "joint4":
                    return niryoOneModel.getJoint4Angle();
                case "joint5":
                    return niryoOneModel.getJoint5Angle();
                case "joint6":
                    return niryoOneModel.getJoint6Angle();
                default:
                    System.out.println("Invalid joint name!");
                    return 0.0;
            }
        } catch (Exception e) {
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
            client.updateDigitalTwin(digitalTwinId, new ArrayList<>(Collections.singleton(patchDocument)));

            System.out.println("Joint angles updated successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
