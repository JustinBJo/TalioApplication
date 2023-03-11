package server.api;

import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class TaskControllerTest {

    private TestTaskRepository repo;

    private TaskController sut;

    @BeforeEach
    public void setup() {
        repo = new TestTaskRepository();
        sut = new TaskController(repo);
    }

    @Test
    public void addTest() {
        var a = new Task("Task Title", "Description",
        new ArrayList<>(), new ArrayList<>());
        var comp = sut.add(a);
        assertEquals(OK, comp.getStatusCode());
    }

    @Test
    public void failAddTest() {
        var a = sut.add(getTask(null));
        assertEquals(BAD_REQUEST, a.getStatusCode());
    }

    @Test
    public void getByIdTest() {
        var a = getTask("t1");
        repo.save(a);
        assertEquals(a, sut.getById(a.getId()).getBody());
    }

    @Test
    public void getByIdFailTest() {
        assertEquals(BAD_REQUEST, sut.getById(-1).getStatusCode());
    }

    private static Task getTask(String t) {
        return new Task(t, "", null, null);
    }

    @Test
    void getAll() {
        var a = getTask("t1");
        var b = getTask("t2");
        repo.save(a);
        repo.save(b);
        List<Task> tasks = sut.getAll();
        assertTrue(tasks.contains(a));
        assertTrue(tasks.contains(b));
        assertEquals(2, tasks.size());
    }

}
