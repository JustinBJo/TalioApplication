package server.api;

import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TaskControllerTest {

    private TestTaskRepository repo;

    private TaskController sut;

    @BeforeEach
    public void setup() {
        repo = new TestTaskRepository();
        sut = new TaskController(repo);
    }

    @Test
    public void cannotAddNullTitle() {
        var actual = sut.add(getTask(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void databaseIsUsed() {
        sut.add(getTask("t1"));
        repo.calledMethods.contains("save");
    }

    private static Task getTask(String t) {
        return new Task(t, "", null, null);
    }

}
