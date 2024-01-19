package at.jku.swe.simpcomp.azureadapter.service_tests.helperclasses_tests;

import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DigitalTwinsModelData;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DigitalTwinsModelDataTest {

    @Test
    public void testGetterMethods() {
        DigitalTwinsModelData modelData = createSampleModelData();

        assertEquals(createDisplayNameMap(), modelData.getDisplayNameLanguageMap());
        assertEquals(createDescriptionMap(), modelData.getDescriptionLanguageMap());
        assertEquals("model123", modelData.getModelId());
        assertEquals(OffsetDateTime.parse("2022-01-19T12:00:00Z"), modelData.getUploadedOn());
        assertEquals(true, modelData.isDecommissioned());
        assertEquals("sample DTDL model", modelData.getDtdlModel());
    }

    private DigitalTwinsModelData createSampleModelData() {
        return new DigitalTwinsModelData(
                "model123",
                "sample DTDL model",
                createDisplayNameMap(),
                createDescriptionMap(),
                OffsetDateTime.parse("2022-01-19T12:00:00Z"),
                true
        );
    }

    private Map<String, String> createDisplayNameMap() {
        Map<String, String> displayNameMap = new HashMap<>();
        displayNameMap.put("en", "English Display Name");
        displayNameMap.put("de", "German Display Name");
        return displayNameMap;
    }

    private Map<String, String> createDescriptionMap() {
        Map<String, String> descriptionMap = new HashMap<>();
        descriptionMap.put("en", "English Description");
        descriptionMap.put("de", "German Description");
        return descriptionMap;
    }
}
