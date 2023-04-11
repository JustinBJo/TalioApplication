package client.utils;

import client.scenes.MainCtrlTalio;
import client.scenes.SubtaskCtrl;
import commons.Subtask;
import commons.Task;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskDetailsUtilsTest {

    @Mock
    private MainCtrlTalio mainCtrl;
    @Mock
    private ServerUtils server;
    @Mock
    private WebsocketUtils websocket;
    @Mock
    private ChildrenManager<Subtask, SubtaskCtrl> childrenManager;
    @Mock
    private  ParentWebsocketManager<Subtask, SubtaskCtrl> parentWebsocket;
    @Mock
    private EntityWebsocketManager<Task> entityWebsocket;
    @Mock
    private AlertUtils alertUtils;

    private TaskDetailsUtils sut;

    private Task task;
    private List<Subtask> st;



    @BeforeEach
    public void setup() {
        mainCtrl = Mockito.mock(MainCtrlTalio.class);
        server = Mockito.mock(ServerUtils.class);
        childrenManager = Mockito.mock(ChildrenManager.class);

        websocket = Mockito.mock(WebsocketUtils.class);
        parentWebsocket = Mockito.mock(ParentWebsocketManager.class);
        entityWebsocket = Mockito.mock(EntityWebsocketManager.class);

        alertUtils = Mockito.mock(AlertUtils.class);

        st = new ArrayList<>();
        st.add(new Subtask("s", true));
        st.add(new Subtask("s1", false));

        task = new Task("t");

        when(server.getTaskData(any())).thenReturn(st);
        when(alertUtils.confirmDeletion("task")).thenReturn(true);
        task.setSubtasks(st);

        doNothing().when(childrenManager).updateChildren(any());
        doNothing().when(childrenManager).clear();




        sut = new TaskDetailsUtils(websocket, mainCtrl, alertUtils, server);
    }

    @Test
    public void testInitialize() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});


        assertEquals(sut.getSubtaskChildrenManager().getChildrenContainer(), v);
        assertEquals(sut.getSubtaskChildrenManager().getChildFxmlFileName(),
                "Subtask.fxml");
        assertEquals(sut.getSubtaskChildrenManager().getChildSceneCtrl(),
                SubtaskCtrl.class);

        assertEquals(sut.getParentWebsocket().getWebsocket(), websocket);
        assertEquals(sut.getParentWebsocket().getChildEntityName(), "subtask");
        assertEquals(sut.getParentWebsocket().getChildEntityClass(),
                Subtask.class);
        assertEquals(sut.getParentWebsocket().getChildrenManager(),
                sut.getSubtaskChildrenManager());

        assertEquals(sut.getEntityWebsocket().getWebsocket(), websocket);
        assertEquals(sut.getEntityWebsocket().getEntityName(), "task");
        assertEquals(sut.getEntityWebsocket().getEntityClass(), Task.class);

    }



//    @Test
//    public void testSetEntity() {
//        sut.setSubtaskChildrenManager(childrenManager);
//        sut.setEntityWebsocket(entityWebsocket);
//        sut.setParentWebsocket(parentWebsocket);
//        sut.setTask(task);
//
//        sut.setEntity(task);
//
//        assertEquals(sut.getTask(), task);
//        verify(childrenManager, times(0)).clear();
//        verify(childrenManager, times(0)).updateChildren(any());
//        verify(childrenManager, times(0)).getChildrenCtrls();
//
//        verify(parentWebsocket, times(1)).register(task.getId());
//        verify(entityWebsocket, times(1))
//                .register(task.getId(), "updateTitle");
//        verify(entityWebsocket, times(1))
//                .register(task.getId(), "updateDescription");
//
//        verify(websocket, times(1))
//                .registerForMessages(eq("/topic/task/updateChildren/" +
//                                task.getId()),
//                eq(Task.class),
//                any());
//
//        verify(websocket, times(1))
//                .registerForMessages( eq("/topic/task/delete"),
//                eq(Task.class),
//                any());
//    }

    @Test
    public void testExit() {
        Task task = new Task("", "", null, null);
        sut.setTask(task);
        sut.exit();
      Mockito.verify(mainCtrl, times(1)).showMain();
    }

    @Test
    public void editTaskTest() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});
        sut.setTask(new Task("t"));
        sut.editTask();
        verify(mainCtrl, times(1))
                .showEditTask(any());
    }

    @Test
    public void editTaskNullTest() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});
        sut.setTask(null);
        sut.editTask();

        verify(alertUtils, times(1))
                .alertError("No task to edit!");
    }

    @Test
    public void deleteTaskTest() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});
        sut.setTask(task);
        sut.deleteTask();

        verify(alertUtils, times(1))
                .confirmDeletion("task");

        int k = task.getSubtasks().size();
        verify(websocket, times(0)).deleteSubtask(any());


        verify(mainCtrl, times(1))
                .showMain();

    }

    @Test
    public void addSubtaskTest() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});
        sut.setTask(new Task("t"));
        sut.addSubtask();
        verify(mainCtrl, times(1))
                .showAddSubtask(any());
    }

    @Test
    public void addSubtaskNullTest() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});
        sut.setTask(null);
        sut.addSubtask();
        verify(alertUtils, times(1))
                .alertError("No task to add subtask to!");
    }

    @Test
    public void getSubtaskChildrenManagerTest() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});
        sut.setTask(new Task("t"));
        sut.setSubtaskChildrenManager(childrenManager);
        assertEquals(childrenManager, sut.getSubtaskChildrenManager());
    }

    @Test
    public void getTaskTask() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});
        Task t = new Task("t");
        sut.setTask(t);
        assertEquals(sut.getTask(), t);
    }

    @Test
    public void setSubtaskChildrenManager() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});
        Task t = new Task("t");
        sut.setTask(t);
        sut.setSubtaskChildrenManager(childrenManager);
        assertEquals(sut.getSubtaskChildrenManager(), childrenManager);
    }

    @Test
    public void getSetParentWebsocketTest() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});
        sut.setParentWebsocket(parentWebsocket);
        assertEquals(sut.getParentWebsocket(), parentWebsocket);
    }

    @Test
    public void getSetEntityWebsocketTest() {
        VBox v = new VBox();
        sut.initialize(v, (Task) -> {});
        sut.setEntityWebsocket(entityWebsocket);
        assertEquals(sut.getEntityWebsocket(), entityWebsocket);
    }

  
}