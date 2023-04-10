package client.utils;

import client.scenes.CardCtrl;
import client.scenes.MainCtrlTalio;
import commons.Task;
import commons.TaskList;
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

class TaskListUtilsTest {
    @Mock
    private MainCtrlTalio mainCtrl;
    @Mock
    private ServerUtils server;
    @Mock
    private AlertUtils alertUtils;
    @Mock
    private WebsocketUtils websocket;
    @Mock
    private EntityWebsocketManager<TaskList> entityWebsocket;
    @Mock
    private ParentWebsocketManager<Task, CardCtrl> parentWebsocket;
    @Mock
    private ChildrenManager<Task, CardCtrl> childrenManager;

    private TaskListUtils sut;
    private TaskList taskList;

    VBox box;
    @BeforeEach
    public void setup() {
        mainCtrl = Mockito.mock(MainCtrlTalio.class);
        server = Mockito.mock(ServerUtils.class);
        alertUtils = Mockito.mock(AlertUtils.class);
        websocket = Mockito.mock(WebsocketUtils.class);
        childrenManager = Mockito.mock(ChildrenManager.class);
        entityWebsocket = Mockito.mock(EntityWebsocketManager.class);
        parentWebsocket = Mockito.mock(ParentWebsocketManager.class);

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("title"));

        when(server.getTaskListData(any())).thenReturn(tasks);
        when(alertUtils.confirmDeletion(any())).thenReturn(true);

        sut = new TaskListUtils(server, mainCtrl, alertUtils, websocket);
        box = new VBox();
        taskList = new TaskList("title");
    }

    @Test
    void initialize() {
        sut.initialize(box);

        assertEquals(CardCtrl.class,
                sut.getTaskChildrenManager().getChildSceneCtrl());
        assertEquals(box, sut.getTaskChildrenManager().getChildrenContainer());
    }

    @Test
    void getId() {
        sut.initialize(box);
        taskList.setId(100L);
        sut.setTaskList(taskList);

        assertEquals(100L, sut.getId());
    }

    @Test
    void getSetEntityWebsocket() {
        sut.setEntityWebsocket(entityWebsocket);
        assertEquals(entityWebsocket, sut.getEntityWebsocket());
    }

    @Test
    void getSetParentWebsocket() {
        sut.setParentWebsocket(parentWebsocket);
        assertEquals(parentWebsocket, sut.getParentWebsocket());
    }

    @Test
    void getAndSetTaskListTest() {
        sut.setTaskList(taskList);
        assertEquals(taskList, sut.getTaskList());
    }

    @Test
    void getTaskChildrenManager() {
        sut.initialize(box);
        sut.setChildrenManager(childrenManager);
        assertEquals(childrenManager, sut.getTaskChildrenManager());
    }

    @Test
    void setupDropTarget() {
        sut.setTaskList(taskList);
        sut.setupDropTarget(100L);
        verify(server, times(1)).
                updateTaskParent(100L, taskList);
    }

    @Test
    void setEntity() {
        sut.initialize(box);
        sut.setTaskChildrenManager(childrenManager);
        sut.setEntityWebsocket(entityWebsocket);
        sut.setParentWebsocket(parentWebsocket);
        taskList.setId(100L);

        sut.setEntity(taskList);

        assertEquals(sut.getTaskList(), taskList);
        verify(childrenManager, times(1)).clear();
        verify(childrenManager, times(1)).updateChildren(any());
        verify(childrenManager, times(1)).getChildrenCtrls();

        verify(parentWebsocket, times(1)).register(taskList.getId());
        verify(entityWebsocket, times(1))
                .register(taskList.getId(), "update");

        verify(websocket, times(1))
                .registerForMessages(eq("/topic/taskList/updateChildren/" +
                                taskList.getId()),
                        eq(TaskList.class),
                        any());
    }

    @Test
    void delete() {
        sut.setTaskList(taskList);
        doNothing().when(websocket).deleteTaskList(any());
        sut.delete();
        verify(websocket, times(1))
                .deleteTaskList(taskList);
    }

    @Test
    void rename() {
        sut.setTaskList(taskList);
        doNothing().when(mainCtrl).showRenameList(any());
        sut.rename();
        verify(mainCtrl, times(1))
                .showRenameList(taskList);
    }

    @Test
    void addTask() {
        sut.setTaskList(taskList);
        doNothing().when(mainCtrl).showAddTask(any());
        sut.addTask();
        verify(mainCtrl, times(1)).showAddTask(taskList);
    }

    @Test
    void failedAddTask() {
        sut.setTaskList(null);
        doNothing().when(alertUtils).alertError(any());
        sut.addTask();
        verify(alertUtils, times(1))
                .alertError("No list to add task to!");
    }
}