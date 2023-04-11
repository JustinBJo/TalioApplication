package server.api;
import commons.Board;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.testRepository.TestBoardRepository;
import server.database.BoardRepository;
import server.api.testRepository.TestTaskListRepository;
import server.api.testRepository.TestTaskRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TaskListControllerTest {

    private TaskListRepository repo;
    private BoardRepository boardRepo;
    private TaskRepository taskRepo;
    private TaskListController taskListController;

    @BeforeEach
    public void setup() {
        repo = new TestTaskListRepository();
        boardRepo = new TestBoardRepository();
        taskRepo = new TestTaskRepository();
        taskListController = new TaskListController(repo, boardRepo, taskRepo);
    }

    @Test
    public void addTest() {
        TaskList tl = new TaskList("test1");
        Board b = new Board("test");
        boardRepo.save(b);
        taskListController.add(tl, b.getId());
        assertTrue(repo.findAll().contains(tl));
    }

    @Test
    public void getByIdTest() {
        TaskList tl1 = new TaskList("test1");
        TaskList tl2 = new TaskList("test2");
        repo.save(tl1);
        repo.save(tl2);
        assertEquals(tl1, taskListController.getById(tl1.getId()).getBody());
        assertEquals(tl2, taskListController.getById(tl2.getId()).getBody());
    }

    @Test
    public void getByIdNonExistentTest() {
        var result = taskListController.getById(1L);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void addWithNullTitle() {
        TaskList tl = new TaskList(null);
        Board b = new Board("test");
        var result = taskListController.add(tl, b.getId());
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void deleteTest() {
        TaskList tl = new TaskList(null);
        tl.setId( (long) 100);
        repo.save(tl);
        taskListController.delete(100l);
        assertFalse(repo.findById( (long) 100).isPresent());
    }

    @Test
    public void updateTaskList() {
        TaskList l = new TaskList("Old Title");
        repo.save(l);

        taskListController.update(l.getId(), "New Title");

        assertTrue(repo.findAll().contains(l));
        String newTitle = repo.getById(l.getId()).getTitle();
        assertEquals("New Title", newTitle);
    }

    @Test
    void getChildTasks() {
        TaskList tl = new TaskList("test");
        Task taskA = new Task("a");
        Task taskB = new Task("b");
        tl.addTask(taskA);
        tl.addTask(taskB);
        long tlID = repo.save(tl).getId();

        assertTrue(repo.findAll().contains(tl));

        var response = taskListController.getChildTasks(tlID);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Task> tasks = response.getBody();
        assert tasks != null;
        assertTrue(tasks.contains(taskA));
        assertTrue(tasks.contains(taskB));
    }

    @Test
    void messageAddTest() {
        TaskList tl = new TaskList("test1");
        Board b = new Board("test");
        boardRepo.save(b);
        taskListController.messageAdd(tl, b.getId().toString());
        assertTrue(repo.findAll().contains(tl));
    }

    @Test
    void messageUpdateTest() {
        TaskList l = new TaskList("Old Title");
        repo.save(l);

        taskListController.messageUpdate(l.getId().toString(), "New Title");

        assertTrue(repo.findAll().contains(l));
        String newTitle = repo.getById(l.getId()).getTitle();
        assertEquals("New Title", newTitle);
    }

}
