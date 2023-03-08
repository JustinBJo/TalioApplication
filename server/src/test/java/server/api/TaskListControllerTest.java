package server.api;

import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TaskListRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
