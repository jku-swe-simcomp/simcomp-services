package at.jku.swe.simcomp.azureadapter.service.NiryoOneController;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.models.JsonPatchDocument;
import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The AzureExecutionService class is responsible for interacting with the Azure Digital Twins service.
 * It provides methods for building connections, retrieving joint angles, updating joint angles, and other related operations.
 */
@Component
@Slf4j
public class AzureExecutionService {

    /**
     * The Azure Digital Twins client used for interacting with the service.
     */
    public static DigitalTwinsClient client;

    /**
     * Azure AD tenant ID.
     */
    static String tentantId;

    /**
     * Azure AD client ID.
     */
    static String clientId;

    /**
     * Azure AD client secret.
     */
    static String clientSecret;

    /**
     * Azure Digital Twins service endpoint.
     */
    static String endpoint;

    /**
     * Constructs an AzureExecutionService with the specified configuration values.
     *
     * @param tentantId    Azure AD tenant ID.
     * @param clientId     Azure AD client ID.
     * @param clientSecret Azure AD client secret.
     * @param endpoint     Azure Digital Twins service endpoint.
     */
    public AzureExecutionService(@Value("${azure.tentantid}") String tentantId,
                                 @Value("${azure.clientid}$") String clientId,
                                 @Value("${azure.clientSecret}$") String clientSecret,
                                 @Value("${azure.endpoint}$") String endpoint) {
        AzureExecutionService.tentantId = tentantId;
        AzureExecutionService.clientId = clientId;
        AzureExecutionService.clientSecret = clientSecret;
        AzureExecutionService.endpoint = endpoint;
        client = buildConnection();
    }

    /**
     *
     * @return The test DigitalTwinsClient instance.
     */
    public static DigitalTwinsClient testBuildConnection() {
        return new DigitalTwinsClientBuilder()
                .credential(
                        new ClientSecretCredentialBuilder()
                                .tenantId("f11d36c0-5880-41f7-91e5-ac5e42209e77")
                                .clientId("a39ddd8f-18bf-41b2-9ab9-fea69e235b86")
                                .clientSecret("unI8Q~MP2uwUGg38dOu4ASnrmcCYNC19fCAKPc9r")
                                .build()
                )
                .endpoint("https://Student.api.wcus.digitaltwins.azure.net")
                .buildClient();
    }

    /**
     * Builds and returns a connection to the Azure Digital Twins service.
     *
     * @return The DigitalTwinsClient instance.
     */
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

    /**
     * Retrieves a list of all joint angles for a specified digital twin ID.
     *
     * @param digitaltwinid The ID of the digital twin.
     * @return A list of joint angles for the specified digital twin.
     */
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

    /**
     * Retrieves a list of keys (IDs) for all digital twins.
     *
     * @return A list of digital twin keys (IDs).
     */
    public static List<String> getKeys() {
        List<String> keys = new ArrayList<>();

        PagedIterable<String> pageableResponse = client.query("SELECT * FROM digitaltwins", String.class);

        for (String response : pageableResponse) {
            keys.add(getStringValueForKey(response, "/dtId"));
        }

        return keys;
    }

    /**
     * Retrieves the string value for a specified key from a JSON string.
     *
     * @param jsonString The JSON string.
     * @param key         The key for which to retrieve the value.
     * @return The string value associated with the specified key.
     */
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

    /**
     * Retrieves the joint angle for a specified digital twin and joint name.
     *
     * @param digitaltwinid The ID of the digital twin.
     * @param jointName     The name of the joint for which to retrieve the angle.
     * @return The joint angle value.
     */
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

    /**
     * Retrieves the double value for a specified key from a JSON string.
     *
     * @param jsonString The JSON string.
     * @param key         The key for which to retrieve the value.
     * @return The double value associated with the specified key.
     */
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

    /**
     * Sets the joint angle for a specified digital twin and joint name.
     *
     * @param digitaltwinid The ID of the digital twin.
     * @param jointName     The name of the joint for which to set the angle.
     * @param jointAngle    The new joint angle value.
     */
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
}