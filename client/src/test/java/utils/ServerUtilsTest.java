package utils;

import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import commons.Board;
import commons.Task;
import commons.TaskList;
import commons.TaskTransfer;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServerUtilsTest {

    private ServerUtils serverUtils;
    private Client client;
    private WebTarget webTarget;
    private Invocation.Builder builder;

    @BeforeEach
    void setUp() {
        serverUtils = new ServerUtils();
        client = Mockito.mock(Client.class);
        webTarget = Mockito.mock(WebTarget.class);
        builder = Mockito.mock(Invocation.Builder.class);

        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(anyString())).thenReturn(builder);
        when(builder.accept(anyString())).thenReturn(builder);


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
//    void setServerTest()

    @Test
    void listenForUpdateTaskParentTest() throws NoSuchFieldException, IllegalAccessException {
        Consumer<TaskTransfer> consumer = new Consumer<TaskTransfer>() {
            @Override
            public void accept(TaskTransfer taskTransfer) {
                return;
            }
        };
        serverUtils.listenForUpdateTaskParent(consumer);

        Field field = ServerUtils.class.getDeclaredField("pollingConsumer");
        field.setAccessible(true);
        Consumer<TaskTransfer> set = (Consumer<TaskTransfer>) field.get(serverUtils);

        assertEquals(consumer, set);
    }

//    @Test
//    void startPollingThreadTest()

//    @Test
//    void stopPollingThreadTest()

    @Test
    void getDefaultBoardTest() {
        Board def = new Board("Test default board");
        when(builder.get((GenericType<Object>) any())).thenReturn(def);

        Board result = serverUtils.getDefaultBoard();
        assertEquals(def, result);
    }

    @Test
    void getDefaultIdTest() {
        long defId = 1030L;
        when(builder.get((GenericType<Object>) any())).thenReturn(defId);

        Long result = serverUtils.getDefaultId();
        assertEquals(defId, result);
    }

    @Test
    void getBoardDataTest() {
        List<TaskList> testList = new ArrayList<>();
        when(builder.get((GenericType<Object>) any())).thenReturn(testList);

        List<TaskList> result = serverUtils.getBoardData(1);
        assertEquals(result, testList);
    }

    @Test
    void getBoardByCodeTest() {
        Board test = new Board("Test");
        when(builder.get((GenericType<Object>) any())).thenReturn(test);

        Board result = serverUtils.getBoardByCode("testcode");
        assertEquals(test, result);
    }

    @Test
    void addBoardTest() {
        Board test = new Board("Test");
        when(builder.post(Entity.entity(test, APPLICATION_JSON), Board.class))
                .thenReturn(test);

        Board result = serverUtils.addBoard(test);
        assertEquals(test, result);
    }

    @Test
    void getBoardByIdTest() {
        Board test = new Board("Test");
        when(builder.get((GenericType<Object>) any())).thenReturn(test);

        Board result = serverUtils.getBoardById(1030);
        assertEquals(test, result);
    }

}

