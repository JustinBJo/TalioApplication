package server.api;

import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.testRepository.TestSubtaskRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskControllerTest {

    private TestSubtaskRepository repo;

    private SubtaskController sut;
    private Subtask k1;
    private Subtask k2;

    @BeforeEach
    void setup() {
        repo = new TestSubtaskRepository();
        k1 = new Subtask("test1", true);
        k2 = new Subtask("test2", false);
        sut = new SubtaskController(repo);
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
        sut.add(k1);

        assertEquals(k1, repo.findById( (long) 100).get());

    }
}