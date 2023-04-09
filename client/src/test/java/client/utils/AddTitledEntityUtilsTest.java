package client.utils;

import client.scenes.MainCtrlTalio;
import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddTitledEntityUtilsTest {

    @Mock
    private ServerUtils server;
    @Mock
    private MainCtrlTalio mainCtrl;
    @Mock
    private AlertUtils alertUtils;
    @Mock
    private WebsocketUtils websocket;

    private AddTitledEntityUtils.Type type;
    private TaskList taskListToEdit;

    private Subtask subtaskToEdit;
    private Task currentTask;
    private Board activeBoard;
    @Mock
    private User u;

    private AddTitledEntityUtils sut;

    @BeforeEach
    void setup() {
        server = Mockito.mock(ServerUtils.class);
        mainCtrl = Mockito.mock(MainCtrlTalio.class);
        alertUtils = Mockito.mock(AlertUtils.class);
        websocket = Mockito.mock(WebsocketUtils.class);

        sut = new AddTitledEntityUtils(server, mainCtrl, alertUtils, websocket);

        taskListToEdit = new TaskList("title");
        subtaskToEdit = new Subtask("title", false);
        currentTask = new Task("title");
        activeBoard = new Board("title");
        u = Mockito.mock(User.class);
        when(server.addBoard(any())).thenReturn(activeBoard);
        when(mainCtrl.getUser()).thenReturn(u);
    }

    @Test
    void getAndSetTaskListToEdit() {
        sut.setTaskListToEdit(taskListToEdit);
        assertEquals(taskListToEdit, sut.getTaskListToEdit());
    }

    @Test
    void getAndSetSubtaskToEdit() {
        sut.setSubtaskToEdit(subtaskToEdit);
        assertEquals(subtaskToEdit, sut.getSubtaskToEdit());
    }

    @Test
    void getAndSetCurrentTask() {
        sut.setCurrentTask(currentTask);
        assertEquals(currentTask, sut.getCurrentTask());
    }

    @Test
    void pressCancel() {
        doNothing().when(mainCtrl).showMain();
        sut.pressCancel();
        verify(mainCtrl, times(1)).showMain();
    }

    @Test
    void pressConfirmEmpty() {
        sut.pressConfirm("");
        verify(mainCtrl, times(0)).showMain();
    }

    @Test
    void confirmTaskList() {
        type = AddTitledEntityUtils.Type.TaskList;
        sut.setType(type);
        when(mainCtrl.getActiveBoard()).thenReturn(activeBoard);
        doNothing().when(mainCtrl).showMain();
        doNothing().when(websocket).addTaskList(any(), any());

        sut.pressConfirm("t");

        verify(websocket).addTaskList(any(), any());
        verify(mainCtrl, times(1)).showMain();
    }

    @Test
    void confirmTaskListNullBoard() {
        type = AddTitledEntityUtils.Type.TaskList;
        sut.setType(type);
        when(mainCtrl.getActiveBoard()).thenReturn(null);
        doNothing().when(mainCtrl).showMain();
        doNothing().when(alertUtils).alertError(any());

        sut.pressConfirm("t");

        verify(alertUtils, times(1))
                .alertError("Lists must be created within boards!");
        verify(mainCtrl, times(1)).showMain();
    }

    @Test
    void confirmBoard() {
        type = AddTitledEntityUtils.Type.Board;
        sut.setType(type);
        doNothing().when(mainCtrl).showMain();
        doNothing().when(mainCtrl).setActiveBoard(any());
        doNothing().when(u).addBoard(any());
        doNothing().when(websocket).saveUser(any());

        sut.pressConfirm("t");

        verify(mainCtrl, times(1)).setActiveBoard(any());
        verify(u, times(1)).addBoard(any());
        verify(websocket, times(1)).saveUser(u);
        verify(mainCtrl, times(1)).showMain();
    }

    @Test
    void confirmRenameTaskListNull() {
        type = AddTitledEntityUtils.Type.RenameTaskList;
        sut.setType(type);
        sut.setTaskListToEdit(null);
        doNothing().when(mainCtrl).showMain();
        doNothing().when(alertUtils).alertError(any());

        sut.pressConfirm("t");

        verify(alertUtils, times(1))
                .alertError("No task list to edit!");
        verify(mainCtrl, times(1)).showMain();
    }

    @Test
    void confirmRenameTaskList() {
        type = AddTitledEntityUtils.Type.RenameTaskList;
        sut.setType(type);
        sut.setTaskListToEdit(taskListToEdit);
        doNothing().when(websocket).updateTaskList(any(), anyString());

        sut.pressConfirm("t");

        verify(websocket, times(1)).updateTaskList(taskListToEdit, "t");
    }

    @Test
    void confirmRenameBoard() {
        type = AddTitledEntityUtils.Type.RenameBoard;
        sut.setType(type);
        doNothing().when(websocket).updateBoard(any(), anyString());
        List<Board> boards;
        boards = Mockito.mock(List.class);
        boards.add(activeBoard);
        when(u.getBoards()).thenReturn(boards);
        when(boards.indexOf(null)).thenReturn(2);
        when(boards.get(anyInt())).thenReturn(activeBoard);
        doNothing().when(websocket).saveUser(any());

        sut.pressConfirm("t");

        verify(boards, times(1)).get(2);
        verify(websocket, times(1)).saveUser(u);
    }

    @Test
    void confirmNewSubtask() {
        type = AddTitledEntityUtils.Type.Subtask;
        sut.setType(type);
        sut.setCurrentTask(currentTask);
        doNothing().when(websocket).addSubtask(any(), any());
        List<Task> tasks = new ArrayList<>();
        when(server.getTasks()).thenReturn(tasks);
        doNothing().when(mainCtrl).showTaskDetails(any());


        sut.pressConfirm("t");

        verify(server, times(1)).getTasks();
        verify(mainCtrl, times(1)).showTaskDetails(currentTask);

    }

    @Test
    void confirmRenameSubtask() {
        type = AddTitledEntityUtils.Type.RenameSubtask;
        sut.setType(type);
        sut.setSubtaskToEdit(subtaskToEdit);
        sut.setCurrentTask(currentTask);
        doNothing().when(mainCtrl).showTaskDetails(any());
        doNothing().when(websocket).updateSubtask(any(), anyString());

        sut.pressConfirm("t");

        verify(websocket, times(1)).updateSubtask(subtaskToEdit, "t");
        verify(mainCtrl, times(1)).showTaskDetails(currentTask);
    }
}