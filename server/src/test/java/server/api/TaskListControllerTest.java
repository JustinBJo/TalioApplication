package server.api;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TaskListRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TaskListControllerTest {

    private TaskListRepository repo;
    private TaskListController taskListController;

    @BeforeEach
    public void setup() {
        repo = new TestTaskListRepository();
        taskListController = new TaskListController(repo);
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
    public void deleteTest() {
        TaskList tl = new TaskList(null);
        tl.setId( (long) 100);
        repo.save(tl);
        taskListController.delete(100);
        assertFalse(repo.findById( (long) 100).isPresent());
    }

}
