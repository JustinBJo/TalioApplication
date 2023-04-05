package client.utils;

import client.scenes.MainCtrlTalio;
import client.scenes.SubtaskCtrl;
import commons.Subtask;
import commons.Task;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskDetailsUtilsTest {

    @Mock
    private MainCtrlTalio mainCtrl;
    @Mock
    private ServerUtils server;
    @Mock
    private ChildrenManager<Subtask, SubtaskCtrl> childrenManager;
    @Mock
    private SubtaskCtrl subtaskCtrl;
    @Mock
    private ErrorUtils errorUtils;

    private TaskDetailsUtils sut;


    @BeforeEach
    public void setup() {
        mainCtrl = Mockito.mock(MainCtrlTalio.class);
        server = Mockito.mock(ServerUtils.class);
        childrenManager = Mockito.mock(ChildrenManager.class);
        subtaskCtrl = Mockito.mock(SubtaskCtrl.class);
        errorUtils = Mockito.mock(ErrorUtils.class);



        List<Subtask> st = new ArrayList<>();
        st.add(new Subtask("s", true));

        when(server.getTaskData(any())).thenReturn(st);
        when(server.confirmDeletion("task")).thenReturn(true);
        when(server.deleteTask(any())).thenReturn(new Task("t"));

        doNothing().when(childrenManager).updateChildren(st);


        sut = new TaskDetailsUtils(server, mainCtrl);
    }

    @Test
    public void testInitialize() {
        VBox v = new VBox();
        sut.initialize(v);

        assertEquals(sut.getSubtaskChildrenManager().getChildrenContainer(), v);
        assertEquals(sut.getSubtaskChildrenManager().getChildFxmlFileName(), "Subtask.fxml");
        assertEquals(sut.getSubtaskChildrenManager().getChildSceneCtrl(), SubtaskCtrl.class);

    }

    @Test
    public void testRefreshNull() {
        VBox v = new VBox();
        sut.initialize(v);
        sut.setTask(null);
        sut.setSubtaskChildrenManager(childrenManager);
        sut.refresh();
        assertEquals(sut.getSubtaskChildrenManager().getChildrenCtrls(), new ArrayList<>());

    }

    @Test
    public void testRefresh() {
        VBox v = new VBox();
        sut.initialize(v);
        sut.setSubtaskChildrenManager(childrenManager);
        sut.setTask(new Task("t1"));

        Set<Subtask> st = new HashSet<>();
        st.add(new Subtask("s", true));

        sut.refresh();
        Mockito.verify(childrenManager, times(1)).updateChildren(server.getTaskData(any()));
        Mockito.verify(subtaskCtrl, times(childrenManager.getChildrenCtrls().size())).setParentTask(any());
    }

    @Test
    public void testSetTask() {
        sut.setTask(new Task("t"));
        assertEquals(sut.getTask().getTitle(), "t");
    }

    @Test
    public void testExit() {
        sut.exit();
      Mockito.verify(mainCtrl, times(1)).showMain();
    }

    @Test
    public void editTaskTest() {
        VBox v = new VBox();
        sut.initialize(v);
        sut.setTask(new Task("t"));
        sut.editTask();
        verify(mainCtrl, times(1)).showEditTask(any());
    }

    @Test
    public void deleteTaskTest() {
        VBox v = new VBox();
        sut.initialize(v);
        sut.setTask(new Task("t"));
        sut.deleteTask();
        verify(server, times(1)).deleteTask(any());
        verify(mainCtrl, times(1)).showMain();

    }

    @Test
    public void addSubtaskTest() {
        VBox v = new VBox();
        sut.initialize(v);
        sut.setTask(new Task("t"));
        sut.addSubtask();
        verify(mainCtrl, times(1)).showAddSubtask(any());
    }

    @Test
    public void getSubtaskChildrenManagerTest() {
        VBox v = new VBox();
        sut.initialize(v);
        sut.setTask(new Task("t"));
        sut.setSubtaskChildrenManager(childrenManager);
        assertEquals(childrenManager, sut.getSubtaskChildrenManager());
    }

    @Test
    public void getTaskTask() {
        VBox v = new VBox();
        sut.initialize(v);
        Task t = new Task("t");
        sut.setTask(t);
        assertEquals(sut.getTask(), t);
    }

    @Test
    public void setSubtaskChildrenManager() {
        VBox v = new VBox();
        sut.initialize(v);
        Task t = new Task("t");
        sut.setTask(t);
        sut.setSubtaskChildrenManager(childrenManager);
        assertEquals(sut.getSubtaskChildrenManager(), childrenManager);
    }

  
}