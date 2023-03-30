package server.api;

import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class TaskControllerTest {

    private TestTaskRepository repo;
    private TestTaskListRepository taskListRepository;

    private TaskController sut;

    @BeforeEach
    public void setup() {
        repo = new TestTaskRepository();
        taskListRepository = new TestTaskListRepository();
        sut = new TaskController(repo, taskListRepository);
    }

    @Test
    public void addTest() {
        var a = new Task("Task Title", "Description");
        var tl = new TaskList("Test List");
        long tlID = taskListRepository.save(tl).getId();
        var comp = sut.add(a, tlID);
        assertEquals(OK, comp.getStatusCode());
    }

    @Test
    public void failAddTest() {
        var a = sut.add(getTask(null), 0);
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
    void updateTitleTest() {
        Task task = new Task("OldTitle",
                "Old Description",
                new ArrayList<>(),
                new ArrayList<>());
        repo.save(task);

        sut.updateTitle(task.getId(), "New Title");

        assertTrue(repo.findAll().contains(task));
        String newTitle = repo.getById(task.getId()).getTitle();
        assertEquals("New Title", newTitle);
    }

    @Test
    void updateDescriptionTest() {
        Task task = new Task("Old Title",
                "Old Description",
                new ArrayList<>(),
                new ArrayList<>());
        repo.save(task);

        sut.updateDescription(task.getId(), "new description");

        assertTrue(repo.findAll().contains(task));
        String newDescription = repo.getById(task.getId()).getDescription();
        assertEquals(newDescription, "new description");
    }

    @Test
    void deleteTaskTest() {
        Task task = new Task("t",
                "d",
                new ArrayList<>(),
                new ArrayList<>());
        repo.save(task);

        sut.delete(task.getId());

        assertFalse(repo.findById(task.getId()).isPresent());
    }

}
