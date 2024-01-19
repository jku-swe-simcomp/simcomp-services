package at.jku.swe.simpcomp.azureadapter.service_tests.helperclasses_tests;

import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DeserializerService;
import at.jku.swe.simcomp.azureadapter.service.NiryoOneModel.NiryoOneModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeserializerServiceTest {

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        when(objectMapperMock.readValue(anyString(), NiryoOneModel.class)).thenReturn(createMockNiryoOneModel());

        DeserializerService deserializerService = new DeserializerService();

        NiryoOneModel result = deserializerService.deserialize("{\"key\":\"value\"}");

        assertEquals(createMockNiryoOneModel(), result);
    }

    private NiryoOneModel createMockNiryoOneModel() {
        NiryoOneModel niryoOneModel = new NiryoOneModel();
        niryoOneModel.setJoint1Angle(0.3);
        return niryoOneModel;
    }
}
