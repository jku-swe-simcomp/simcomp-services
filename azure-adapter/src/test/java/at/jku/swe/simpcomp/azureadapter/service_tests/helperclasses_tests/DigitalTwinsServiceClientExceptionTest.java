package at.jku.swe.simpcomp.azureadapter.service_tests.helperclasses_tests;

import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DigitalTwinsServiceClientException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DigitalTwinsServiceClientExceptionTest {

    @Test
    public void testDefaultConstructor() {
        DigitalTwinsServiceClientException exception = new DigitalTwinsServiceClientException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testConstructorWithMessage() {
        DigitalTwinsServiceClientException exception = new DigitalTwinsServiceClientException("Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Cause Exception");
        DigitalTwinsServiceClientException exception = new DigitalTwinsServiceClientException("Test Message", cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithCause() {
        Throwable cause = new RuntimeException("Cause Exception");
        DigitalTwinsServiceClientException exception = new DigitalTwinsServiceClientException(cause);
        assertNotEquals("Cause Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
