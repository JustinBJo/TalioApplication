package utils;

import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ServerUtilsTest {

    private ServerUtils serverUtils;
    private Client client;
    private WebTarget webTarget;
    private Invocation.Builder builder;
    private Response response;

    @BeforeEach
    void setUp() {
        serverUtils = new ServerUtils();
        client = Mockito.mock(Client.class);
        webTarget = Mockito.mock(WebTarget.class);
        builder = Mockito.mock(Invocation.Builder.class);
        response = Mockito.mock(Response.class);

        // Configure the default behavior of the mocks
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(anyString())).thenReturn(builder);
        when(builder.accept(anyString())).thenReturn(builder);

        // Replace the ClientBuilder in the ServerUtils class
        serverUtils = new ServerUtils() {
            @Override
            protected Client createClient() {
                return client;
            }
        };
    }

    @Test
    void setWebsocketsTest() throws NoSuchFieldException,
                                    IllegalAccessException {
        WebsocketUtils websockets = new WebsocketUtils();
        serverUtils.setWebsockets(websockets);

        Field field = ServerUtils.class.getDeclaredField("websockets");
        field.setAccessible(true);
        WebsocketUtils set = (WebsocketUtils) field.get(serverUtils);

        assertEquals(websockets, set);
    }

    @Test
    void resetServerTest() throws NoSuchFieldException,
                                  IllegalAccessException {
        serverUtils.resetServer();

        Field field = ServerUtils.class.getDeclaredField("server");
        field.setAccessible(true);
        String set = (String) field.get(serverUtils);

        assertEquals("", set);
    }

//    @Test
//    void setServer_ValidUrl() {
//        when(builder.get()).thenReturn(response);
//        when(response.getStatus()).thenReturn(200);
//
//        assertDoesNotThrow(() -> serverUtils.setServer("http://localhost:8080/"));
//    }

    @Test
    void setServer_ServerNotFound() {
        assertThrows(ProcessingException.class,
                () -> serverUtils.setServer("http://localhost:9999/"));
    }


}

