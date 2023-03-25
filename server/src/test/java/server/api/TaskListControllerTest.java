package server.api;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TaskListControllerTest {

    private TaskListRepository repo;
    private TaskRepository taskRepo;
    private TaskListController taskListController;

    @BeforeEach
    public void setup() {
        repo = new TestTaskListRepository();
        taskRepo = new TestTaskRepository();
        taskListController = new TaskListController(repo, taskRepo);
    }

    @Test
    public void addTest() {
        TaskList tl = new TaskList("test1");
        taskListController.add(tl);
        assertTrue(repo.findAll().contains(tl));
    }
    
    @Test
    public void getAllTest() {
        TaskList tl1 = new TaskList("test1");
        TaskList tl2 = new TaskList("test2");
        repo.save(tl1);
        repo.save(tl2);
        List<TaskList> all = taskListController.getAll();
        assertTrue(all.contains(tl1));
        assertTrue(all.contains(tl2));
        assertEquals(all.size(), 2);
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
        var result = taskListController.add(tl);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void addTaskTest() {
        TaskList tasklist = new TaskList("test list");
        long listId = repo.save(tasklist).getId();

        Task task = new Task("test task", "description",
                new ArrayList<>(), new ArrayList<>());
        long taskId = taskRepo.save(task).getId();

        var result = taskListController.addChildTask(listId, taskId);

        String targetString = "Added Task " + taskId
                + " to List " + listId;

        assertEquals(OK, result.getStatusCode());
        assertEquals(targetString, result.getBody());
    }

    @Test
    public void addNonExistentTaskTest() {
        TaskList tasklist = new TaskList("test list");
        long listId = repo.save(tasklist).getId();

        var result = taskListController.addChildTask(listId, 1);

        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void deleteTest() {
        TaskList tl = new TaskList(null);
        tl.setId( (long) 100);
        repo.save(tl);
        taskListController.delete(100);
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

}
