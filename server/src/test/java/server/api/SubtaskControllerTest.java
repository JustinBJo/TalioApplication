package server.api;

import commons.Subtask;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskControllerTest {

    private TestSubtaskRepository repo;

    private TestTaskRepository taskRepo;

    private SubtaskController sut;
    private Subtask k1;
    private Subtask k2;

    private Task t1;

    @BeforeEach
    void setup() {
        repo = new TestSubtaskRepository();
        taskRepo = new TestTaskRepository();
        k1 = new Subtask("test1", true);
        k2 = new Subtask("test2", false);
        t1 = new Task("test");
        sut = new SubtaskController(repo, taskRepo);
    }

    @Test
    void getAll() {
        repo.save(k1);
        repo.save(k2);
        List<Subtask> res = sut.getAll();

        assertTrue(res.contains(k1));
        assertTrue(res.contains((k2)));


    }

    @Test
    void getById() {
        Subtask k = new Subtask("test", true);
        k.setId(100);
        repo.save(k);
        ResponseEntity<Subtask> response = sut.getById(100);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(k, response.getBody());
    }

    @Test
    void add() {
        k1.setId(100);
        taskRepo.save(t1);
        sut.add(k1, t1.getId());

        assertEquals(k1, repo.findById(k1.getId()).get());

    }

    @Test
    void updateTest() {
        repo.save(k1);
        sut.update(k1.getId(), "NewTitle");

        assertTrue(repo.findAll().contains(k1));
        String newTitle = repo.getById(k1.getId()).getTitle();
        assertEquals("NewTitle", newTitle);
    }
}