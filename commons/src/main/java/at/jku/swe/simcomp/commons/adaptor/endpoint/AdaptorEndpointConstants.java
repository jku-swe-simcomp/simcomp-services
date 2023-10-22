package at.jku.swe.simcomp.commons.adaptor.endpoint;

public class AdaptorEndpointConstants {
    public static final String HEALTH_CHECK_PATH="/health";
    public static final String INIT_SESSION_PATH="/session/init";
    public static final String REGISTER_ADAPTOR_PATH="/registry/register";
    public static final String UNREGISTER_ADAPTOR_PATH="/registry/unregister";
    private static final String EXECUTE_ACTION_PATH="/%s/action/execute";
    private static final String EXECUTE_SEQUENCE_PATH="/%s/sequence/execute";
    private static final String CLOSE_SESSION_PATH="/session/%s/close";
    private static final String GET_ATTRIBUTE_PATH="/%s/attribute/%s";
    private AdaptorEndpointConstants(){
        // empty for constants class
    }
    public static String getGetAttributePathForAttributeName(String sessionId, String name) {
        return GET_ATTRIBUTE_PATH.formatted(sessionId, name);
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
}