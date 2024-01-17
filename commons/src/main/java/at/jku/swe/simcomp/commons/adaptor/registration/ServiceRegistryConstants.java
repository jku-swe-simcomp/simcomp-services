package at.jku.swe.simcomp.commons.adaptor.registration;

/**
 * Constants for the service registry.
 * This class is not meant to be instantiated.
 */
public class ServiceRegistryConstants {
    public static final String SERVICE_REGISTRY_ENDPOINT_ENVIRONMENT_VARIABLE_NAME = "SERVICE_REGISTRY_ENDPOINT";
    private static final String UNREGISTER_PATH = "/%s";
    private ServiceRegistryConstants(){
        // private constructor to prevent instantiation
    }

    public static String getUnregisterPathForAdaptorName(String adaptorName) {
        return UNREGISTER_PATH.formatted(adaptorName);
    }
}
