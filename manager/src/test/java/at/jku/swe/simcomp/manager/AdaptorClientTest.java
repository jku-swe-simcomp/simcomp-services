package at.jku.swe.simcomp.manager;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.rest.exception.CommandExecutionFailedException;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdaptorClientTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AdaptorClient adaptorClient;

    @Test
    void testGetSession() {
        // arrange
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = buildServiceRegistrationConfigDTO();
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(new ResponseEntity<>("1234", HttpStatus.OK));

        // act
        Optional<String> session = adaptorClient.getSession(serviceRegistrationConfigDTO);

        // assert
        assertTrue(session.isPresent());
        assertEquals("1234", session.get());
    }


    @Test
    void testGetSession_when_non200_thenEmptyOptional() {
        // arrange
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = buildServiceRegistrationConfigDTO();
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR));

        // act
        Optional<String> session = adaptorClient.getSession(serviceRegistrationConfigDTO);

        // assert
        assertTrue(session.isEmpty());
    }

    @Test
    void testGetSession_when_exceptionDuringHTTPCall_thenEmptyOptional() {
        // arrange
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = buildServiceRegistrationConfigDTO();
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenThrow(new RuntimeException("error"));

        // act
        Optional<String> session = adaptorClient.getSession(serviceRegistrationConfigDTO);

        // assert
        assertTrue(session.isEmpty());
    }

    @Test
    void testGetSessionWithSpecificInstance() {
        // arrange
        String instance = "instance";
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = buildServiceRegistrationConfigDTO();
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(new ResponseEntity<>("1234", HttpStatus.OK));

        // act
        Optional<String> session = adaptorClient.getSession(serviceRegistrationConfigDTO, instance);

        // assert
        assertTrue(session.isPresent());
        assertEquals("1234", session.get());
    }


    @Test
    void testGetSessionWithSpecificInstance_when_non200_thenEmptyOptional() {
        // arrange
        String instance = "instance";
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = buildServiceRegistrationConfigDTO();
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR));

        // act
        Optional<String> session = adaptorClient.getSession(serviceRegistrationConfigDTO, instance);

        // assert
        assertTrue(session.isEmpty());
    }

    @Test
    void testGetSessionWithSpecificInstance_when_exceptionDuringHTTPCall_thenEmptyOptional() {
        // arrange
        String instance = "instance";
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = buildServiceRegistrationConfigDTO();
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenThrow(new RuntimeException("error"));

        // act
        Optional<String> session = adaptorClient.getSession(serviceRegistrationConfigDTO, instance);

        // assert
        assertTrue(session.isEmpty());
    }

    @Test
    void testCloseSession(){
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = buildServiceRegistrationConfigDTO();
        adaptorClient.closeSession(serviceRegistrationConfigDTO, "1234");
        verify(restTemplate, times(1)).delete(anyString());
    }

    @Test
    void testCloseSession_whenException_thenIgnored(){
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = buildServiceRegistrationConfigDTO();
        doThrow(new RuntimeException("error")).when(restTemplate).delete(anyString());
        assertDoesNotThrow(() -> adaptorClient.closeSession(serviceRegistrationConfigDTO, "1234"));
        verify(restTemplate, times(1)).delete(anyString());
    }

    @Test
    void testExecuteCommand_success() throws CommandExecutionFailedException, JsonProcessingException {
        // arrange
        ServiceRegistrationConfigDTO adaptorConfig = buildServiceRegistrationConfigDTO();
        ExecutionCommand command = buildExecutionCommand();
        String adaptorSessionKey = "1234";
        String responseBody = "{ \"report\": \"success\" }";
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));
        when(objectMapper.readValue(responseBody, ExecutionResultDTO.class))
                .thenReturn(new ExecutionResultDTO("success"));

        // act
        ExecutionResultDTO result = adaptorClient.executeCommand(adaptorConfig, command, adaptorSessionKey);

        // assert
        assertNotNull(result);
        assertEquals("success", result.getReport());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
        verify(objectMapper, times(1)).readValue(responseBody, ExecutionResultDTO.class);
    }

    @Test
    void testExecuteCommand_when_badRequestResponse_thenException() throws CommandExecutionFailedException, JsonProcessingException {
        // arrange
        ServiceRegistrationConfigDTO adaptorConfig = buildServiceRegistrationConfigDTO();
        ExecutionCommand command = buildExecutionCommand();
        String adaptorSessionKey = "1234";
        String responseBody = "{ \"status\": 400, \"message\": \"Bad Request\" }";
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST));
        when(objectMapper.readValue(responseBody, HttpErrorDTO.class))
                .thenReturn(new HttpErrorDTO(400, "Bad Request"));

        // act
        CommandExecutionFailedException exception = assertThrows(CommandExecutionFailedException.class,
                () -> adaptorClient.executeCommand(adaptorConfig, command, adaptorSessionKey));

        // assert
        assertEquals("Bad Request", exception.getMessage());
        assertEquals(400, exception.getCode());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
        verify(objectMapper, times(1)).readValue(responseBody, HttpErrorDTO.class);
    }

    @Test
    void testExecuteCommand_when_wrongJsonSchema_thenException() throws CommandExecutionFailedException {
        // arrange
        ServiceRegistrationConfigDTO adaptorConfig = buildServiceRegistrationConfigDTO();
        ExecutionCommand command = buildExecutionCommand();
        String adaptorSessionKey = "1234";
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR));

        // act
        CommandExecutionFailedException exception = assertThrows(CommandExecutionFailedException.class,
                () -> adaptorClient.executeCommand(adaptorConfig, command, adaptorSessionKey));

        // assert
        assertEquals(500, exception.getCode());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testExecuteCommand_when_unexpectedException_thenDeclaredException() throws CommandExecutionFailedException {
        // arrange
        ServiceRegistrationConfigDTO adaptorConfig = buildServiceRegistrationConfigDTO();
        ExecutionCommand command = buildExecutionCommand();
        String adaptorSessionKey = "1234";
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("error"));

        // act
        CommandExecutionFailedException exception = assertThrows(CommandExecutionFailedException.class,
                () -> adaptorClient.executeCommand(adaptorConfig, command, adaptorSessionKey));

        // assert
        assertEquals(500, exception.getCode());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testGetAttributeValue_success() throws SessionNotValidException, JsonProcessingException {
        // arrange
        String sessionId = "1234";
        AttributeKey attributeKey = AttributeKey.JOINT_POSITIONS;
        ServiceRegistrationConfigDTO config = buildServiceRegistrationConfigDTO();
        String responseBody = "{ \"jointPositions\": \"[]\" }";
        List<Double> positions = List.of(1.0, 2.0, 3.0, 1.0, 2.0, 3.0);
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));
        when(objectMapper.readValue(responseBody, AttributeValue.class))
                .thenReturn(new AttributeValue.JointPositions(positions));

        // act
        Optional<AttributeValue> result = adaptorClient.getAttributeValue(sessionId, attributeKey, config);

        // assert
        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof AttributeValue.JointPositions);
        assertEquals(positions, ((AttributeValue.JointPositions) result.get()).jointPositions());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
        verify(objectMapper, times(1)).readValue(responseBody, AttributeValue.class);
    }

    @Test
    void testGetAttributeValue_when_401Unauthorized_thenSessionNotValidException() {
        // arrange
        String sessionId = "1234";
        AttributeKey attributeKey = AttributeKey.JOINT_POSITIONS;
        ServiceRegistrationConfigDTO config = buildServiceRegistrationConfigDTO();
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("error", HttpStatus.UNAUTHORIZED));

        // act
        assertThrows(SessionNotValidException.class,
                () -> adaptorClient.getAttributeValue(sessionId, attributeKey, config));

        // assert
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void testGetAttributeValue_when_exceptionDuringHttpCall_thenEmptyOptional() throws SessionNotValidException {
        // arrange
        String sessionId = "1234";
        AttributeKey attributeKey = AttributeKey.JOINT_POSITIONS;
        ServiceRegistrationConfigDTO config = buildServiceRegistrationConfigDTO();
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("error"));

        // act
        Optional<AttributeValue> result = adaptorClient.getAttributeValue(sessionId, attributeKey, config);

        // assert
        assertTrue(result.isEmpty());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void testRegisterSimulationInstance_success(){
       ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = buildServiceRegistrationConfigDTO();
       SimulationInstanceConfig config = new SimulationInstanceConfig();
       when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
         assertDoesNotThrow(() -> adaptorClient.registerSimulationInstanceForAdaptor(serviceRegistrationConfigDTO, config));
         verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void testRegisterSimulationInstance_when_not2xx_thenException(){
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = buildServiceRegistrationConfigDTO();
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class))).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThrows(Exception.class, () -> adaptorClient.registerSimulationInstanceForAdaptor(serviceRegistrationConfigDTO, config));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void testGetSimulationInstances_success() {
        // arrange
        ServiceRegistrationConfigDTO config = buildServiceRegistrationConfigDTO();
        List<SimulationInstanceConfig> expectedInstances = buildSimulationInstanceConfigs(2);
        ResponseEntity<List<SimulationInstanceConfig>> responseEntity =
                new ResponseEntity<>(expectedInstances, null, 200);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        // act
        List<SimulationInstanceConfig> instances = adaptorClient.getSimulationInstances(config);

        // assert
        assertNotNull(instances);
        assertEquals(expectedInstances.size(), instances.size());
        assertEquals(expectedInstances.get(0).getInstanceId(), instances.get(0).getInstanceId());
        assertEquals(expectedInstances.get(1).getInstanceId(), instances.get(1).getInstanceId());
        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void testDeleteSimulationInstance(){
        // arrange
        ServiceRegistrationConfigDTO config = buildServiceRegistrationConfigDTO();

        // act
        adaptorClient.deleteSimulationInstance(config, "instanceId");

        // assert
        verify(restTemplate, times(1)).delete(anyString());
    }

    // utility methods to construct mock data
    private ServiceRegistrationConfigDTO buildServiceRegistrationConfigDTO() {
        return ServiceRegistrationConfigDTO.builder()
                .name("test")
                .host("localhost")
                .port(8080)
                .supportedActions(Set.of(ActionType.GRAB))
                .build();
    }

    private List<SimulationInstanceConfig> buildSimulationInstanceConfigs(int count){
        List<SimulationInstanceConfig> configs = new ArrayList<>();
        for(int i = 0; i < count; i++){
            configs.add(SimulationInstanceConfig.builder()
                    .instanceId("instance" + i)
                    .instanceHost("localhost")
                    .instancePort(8080)
                    .simulationType("type" + i)
                    .build());
        }
        return configs;
    }

    private ExecutionCommand buildExecutionCommand() {
        return new ExecutionCommand.GrabCommand();
    }
}
