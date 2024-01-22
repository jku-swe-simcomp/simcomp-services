package at.jku.swe.simpcomp.azureadapter.service_tests.helperclasses_tests;

import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DeleteDigitalTwinOptions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteDigitalTwinOptionsTest {

    @Test
    public void testGetIfMatch() {
        DeleteDigitalTwinOptions deleteOptions = new DeleteDigitalTwinOptions();

        deleteOptions.setIfMatch("etag123");

        assertEquals("etag123", deleteOptions.getIfMatch());
    }

    @Test
    public void testSetIfMatch() {
        DeleteDigitalTwinOptions deleteOptions = new DeleteDigitalTwinOptions();

        DeleteDigitalTwinOptions result = deleteOptions.setIfMatch("etag456");

        assertEquals("etag456", deleteOptions.getIfMatch());
        assertEquals(deleteOptions, result);
    }
}