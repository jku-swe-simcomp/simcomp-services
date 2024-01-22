package at.jku.swe.simpcomp.azureadapter.service_tests.helperclasses_tests;

import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DigitalTwinsServiceVersion;
import com.azure.core.util.ServiceVersion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DigitalTwinsServiceVersionTest {

    @Test
    public void testGetVersion() {
        assertEquals("2020-10-31", DigitalTwinsServiceVersion.V2020_10_31.getVersion());
        assertEquals("2022-05-31", DigitalTwinsServiceVersion.V2022_05_31.getVersion());
    }

    @Test
    public void testGetLatest() {
        ServiceVersion latestVersion = DigitalTwinsServiceVersion.getLatest();
        assertEquals(DigitalTwinsServiceVersion.V2022_05_31, latestVersion);
    }
}
