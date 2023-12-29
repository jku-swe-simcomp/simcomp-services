package at.jku.swe.simcomp.azureadapter.service.HelperClasses;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneModel.NiryoOneModel;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeserializerService {

    private final ObjectMapper objectMapper;

    public DeserializerService() {
        this.objectMapper = new ObjectMapper();
    }

    public NiryoOneModel deserialize(String json) {
        NiryoOneModel niryoOneModel = null;
        try {
            niryoOneModel = objectMapper.readValue(json, NiryoOneModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return niryoOneModel;
    }
}
