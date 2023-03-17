package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.BoardRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardControllerTest {

    private BoardRepository repo;
    private BoardController sut;


    @BeforeEach
    void setUp() {
        repo = new TestBoardRepository();
        sut = new BoardController(repo);
    }

    @Test
    void getAll() {
        Board a = new Board("123", "a");
        Board b = new Board("345", "b");
        repo.save(a);
        repo.save(b);
        List<Board> response = sut.getAll();

        // Check response
        assertTrue(response.contains(a));
        assertTrue(response.contains(b));
        assertEquals(2, response.size());

        // Check repository
        assertTrue(TestBoardRepository.containsCall(
                (TestBoardRepository) repo, "findAll"));
    }

    @Test
    void getById() {
        Board a = new Board("123", "a");
        a.setId(1);
        repo.save(a);
        ResponseEntity<Board> response = sut.getById(1);

        // Check response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(a, response.getBody());

        // Check repository
        assertTrue(
            TestBoardRepository.containsCall((TestBoardRepository) repo,
                    "findById")
            || TestBoardRepository.containsCall((TestBoardRepository) repo,
                    "getById")
        );
    }

    @Test
    void failGetById() {
        ResponseEntity<Board> response = sut.getById(5);

        // Check response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Check repository
        assertTrue(TestBoardRepository.containsCall((TestBoardRepository) repo,
                "existsById"));
    }

    @Test
    void add() {
        Board a = new Board("123", "a");
        ResponseEntity<Board> response = sut.add(a);

        // Check response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(a, response.getBody());

        // Check repository
        assertTrue(repo.findAll().contains(a));
        assertTrue(TestBoardRepository.containsCall((TestBoardRepository) repo,
                "save"));
    }

    @Test
    void failAdd() {
        Board nullBoard = new Board();
        ResponseEntity<Board> response = sut.add(nullBoard);

        // Check endpoint
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Check repository
        assertFalse(repo.findAll().contains(nullBoard));
        assertFalse(TestBoardRepository.containsCall((TestBoardRepository) repo,
                "save"));

    }

    @Test
    void updateTest() {
        Board board = new Board("1030", "oldName");
        board.setId(2001);
        repo.save(board);

        // Update the board with a new name
        String newName = "newName";
        ResponseEntity<Board> response = sut.updateName(2001, newName);

        // Check endpoint
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(board, response.getBody());

        // Check repository
        assertTrue(repo.findAll().contains(board));
        assertEquals(repo.findById(board.getId()).get().getTitle(), newName);
    }

    @Test
    void failedUpdateTest() {
        Board newBoard = new Board("1030",
                "This board does not exist in the repository");
        newBoard.setId(2001);

        String newName = "newName";
        ResponseEntity<Board> response = sut.updateName(2001, newName);

        // Check endpoint
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteTest() {
        Board board = new Board("1030", "oldName");
        board.setId(2001);
        repo.save(board);

        ResponseEntity<String> response = sut.delete(board.getId());

        // Check endpoint
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
                "Board " + board.getId() + " is removed.",
                response.getBody()
        );

        // Check repository
        assertEquals(repo.getById(board.getId()), null);
    }

    @Test
    void failedDeleteTest() {
        ResponseEntity<String> response = sut.delete(2001);

        // Check endpoint
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "Board does not exist.",
                response.getBody()
        );
    }
}
