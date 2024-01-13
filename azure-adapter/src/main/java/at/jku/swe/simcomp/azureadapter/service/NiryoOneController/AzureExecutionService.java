package at.jku.swe.simcomp.azureadapter.service.NiryoOneController;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import com.azure.core.http.rest.PagedIterable;
import com.azure.core.models.JsonPatchDocument;
import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component @Slf4j
public class AzureExecutionService {

    private final static DigitalTwinsClient client = buildConnection();

    static final String tentantId = "f11d36c0-5880-41f7-91e5-ac5e42209e77";
    static final String clientId = "a39ddd8f-18bf-41b2-9ab9-fea69e235b86";
    static final String clientSecret = "unI8Q~MP2uwUGg38dOu4ASnrmcCYNC19fCAKPc9r";
    static final String endpoint = "https://Student.api.wcus.digitaltwins.azure.net";


    public static DigitalTwinsClient buildConnection() {
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


    public static List<Double> getAllJointAngles(String digitaltwinid) {
        List<Double> jointAngles = new ArrayList<>();

        PagedIterable<String> pageableResponse = client.query("SELECT * FROM digitaltwins", String.class);

        for (String response : pageableResponse) {
            if (response.contains(digitaltwinid)) {
                double joint1 = getDoubleValueForKey(response, "joint1_angle");
                double joint2 = getDoubleValueForKey(response, "joint2_angle");
                double joint3 = getDoubleValueForKey(response, "joint3_angle");
                double joint4 = getDoubleValueForKey(response, "joint4_angle");
                double joint5 = getDoubleValueForKey(response, "joint5_angle");
                double joint6 = getDoubleValueForKey(response, "joint6_angle");

                jointAngles.add(joint1);
                jointAngles.add(joint2);
                jointAngles.add(joint3);
                jointAngles.add(joint4);
                jointAngles.add(joint5);
                jointAngles.add(joint6);
            }
        }

        return jointAngles;
    }

    public static List<String> getKeys() {
        List<String> keys = new ArrayList<>();

        PagedIterable<String> pageableResponse = client.query("SELECT * FROM digitaltwins", String.class);

        for (String response : pageableResponse) {
            keys.add(getStringValueForKey(response, "/dtId"));
        }

        return keys;
    }

    public static String getStringValueForKey(String jsonString, String key) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return jsonNode.get(key).asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static double getJointAngle(String digitaltwinid, String jointName) {
        double value = 0.0;

        PagedIterable<String> pageableResponse = client.query("SELECT * FROM digitaltwins", String.class);

        for (String response : pageableResponse) {
            if (response.contains(digitaltwinid)) {
                value = getDoubleValueForKey(response, jointName);
            }
        }


        return value;
    }

    public static double getDoubleValueForKey(String jsonString, String key) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return jsonNode.get(key).asDouble();
        } catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }


    public static void setJointAngles(String digitaltwinid, List<Double> jointAngles) throws JsonProcessingException {
        double joint1 = jointAngles.get(0);
        double joint2 = jointAngles.get(1);
        double joint3 = jointAngles.get(2);
        double joint4 = jointAngles.get(3);
        double joint5 = jointAngles.get(4);
        double joint6 = jointAngles.get(5);

        PagedIterable<String> pageableResponse = client.query("SELECT * FROM digitaltwins", String.class);

        for (String response : pageableResponse) {
            if (response.contains(digitaltwinid)) {
                JsonPatchDocument updateOp = new JsonPatchDocument();
                updateOp.appendReplace("/joint1_angle", joint1);
                updateOp.appendReplace("/joint2_angle", joint2);
                updateOp.appendReplace("/joint3_angle", joint3);
                updateOp.appendReplace("/joint4_angle", joint4);
                updateOp.appendReplace("/joint5_angle", joint5);
                updateOp.appendReplace("/joint6_angle", joint6);

                client.updateDigitalTwin(digitaltwinid, updateOp);
            }
        }
    }

    public static void setJointAngle(String digitaltwinid, String jointName, double jointAngle) {
        PagedIterable<String> pageableResponse = client.query("SELECT * FROM digitaltwins", String.class);

        for (String response : pageableResponse) {
            if (response.contains(digitaltwinid)) {
                JsonPatchDocument updateOp = new JsonPatchDocument();
                updateOp.appendReplace(jointName, jointAngle);

                client.updateDigitalTwin(digitaltwinid, updateOp);
            }
        }
    }


    public static void main(String[] args) {
        setJointAngle("NiryoRobot", "/joint1_angle", 0.39);
    }

}
