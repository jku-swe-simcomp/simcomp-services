package at.jku.swe.simcomp.azureadapter.service.HelperClasses;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneModel.NiryoOneModel;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The DeserializerService class provides methods for deserializing JSON data into a NiryoOneModel object.
 */
public class DeserializerService {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a DeserializerService and initializes the ObjectMapper object.
     */
    public DeserializerService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Deserializes a JSON string into a NiryoOneModel object.
     *
     * @param json The JSON string to be deserialized.
     * @return A NiryoOneModel object deserialized from the provided JSON string.
     *         Returns null if deserialization fails.
     */
    public NiryoOneModel deserialize(String json) {
        NiryoOneModel niryoOneModel = null;
        try {
            niryoOneModel = objectMapper.readValue(json, NiryoOneModel.class);
        } catch (Exception e) {
            // If an exception occurs during deserialization, print the stack trace.
            e.printStackTrace();
        }
        return niryoOneModel;
    }
}
