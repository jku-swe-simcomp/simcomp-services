package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;

/**
 * This class contains the constants for the adaptor endpoint.
 * Contains the paths for the endpoints, and methods to build the paths based on different parameters.
 */
public class AdaptorEndpointConstants {
    public static final String HEALTH_CHECK_PATH="/health";
    public static final String INIT_SESSION_PATH="/session/init";
    public static final String REGISTER_ADAPTOR_PATH="/registry/register";
    public static final String UNREGISTER_ADAPTOR_PATH="/registry/unregister";
    public static final String SIMULATION_INSTANCE_PATH = "/simulation/instance";

    public static final String GET_CUSTOM_COMMANDS="/custom-commands";
    private static final String EXECUTE_ACTION_PATH="/%s/action/execute";
    private static final String EXECUTE_SEQUENCE_PATH="/%s/sequence/execute";
    private static final String CLOSE_SESSION_PATH="/session/%s/close";
    private static final String GET_ATTRIBUTE_PATH="/%s/attribute/%s";
    private static final String GET_CUSTOM_COMMAND_EXAMPLE ="/custom-commands/%s/example";
    private AdaptorEndpointConstants(){
        // empty for constants class
    }

    public static String getDomain(ServiceRegistrationConfigDTO configDTO){
        return "http://" + configDTO.getHost() + ":" + configDTO.getPort();
    }
    public static String getGetAttributePathForAttributeName(String sessionId, AttributeKey key) {
        return GET_ATTRIBUTE_PATH.formatted(sessionId, key);
    }

    public static String getInitSessionPathWithInstanceId(String instanceId) {
        return INIT_SESSION_PATH + "?instanceId=" + instanceId;
    }

    public static String getDeleteSimulationInstancePathForInstanceId(String instanceId) {
        return SIMULATION_INSTANCE_PATH + "/" + instanceId;
    }
    public static String getExecuteActionPathForSessionId(String sessionId) {
        return EXECUTE_ACTION_PATH.formatted(sessionId);
    }

    public static String getExecuteSequencePathForSessionId(String sessionId) {
        return EXECUTE_SEQUENCE_PATH.formatted(sessionId);
    }

    public static String getCloseSessionPathForSessionId(String sessionId) {
        return CLOSE_SESSION_PATH.formatted(sessionId);
    }

    public static String getCustomCommandExamplePathForType(String type) {
        return GET_CUSTOM_COMMAND_EXAMPLE.formatted(type);
    }
}
