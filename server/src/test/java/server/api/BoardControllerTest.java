package server.api;

import commons.Board;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.testRepository.TestBoardRepository;
import server.database.BoardRepository;
import server.database.UserRepository;
import server.service.DefaultBoardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardControllerTest {

    private BoardRepository repo;
    private UserRepository userRepository;
    private DefaultBoardService service;
    private BoardController sut;

    @BeforeEach
    void setUp() {
        repo = new TestBoardRepository();
        service = new DefaultBoardService();
        userRepository = new TestUserRepository();
        sut = new BoardController(repo, service, userRepository);
    }

    @Test
    void messageUpdateTest() {
        Board board = new Board("1030", "oldName");
        board.setId(2001);
        repo.save(board);

        // Update the board with a new name
        String newName = "newName";
        Board response = sut.messageUpdate("2001", newName);

        // Check endpoint
        assertEquals(board, response);

        // Check repository
        assertTrue(repo.findAll().contains(board));
        assertEquals(repo.findById(board.getId()).get().getTitle(), newName);
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

        ResponseEntity<String> response = sut.deleteBoard(board.getId());

        // Check endpoint
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
                "Board " + board.getId() + " is removed.",
                response.getBody()
        );

        // Check repository
        assertTrue(repo.findAll().contains(board));
    }

    @Test
    void failedDeleteTest() {
        ResponseEntity<String> response = sut.deleteBoard(2001);

        // Check endpoint
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "Board does not exist.",
                response.getBody()
        );
    }

    @Test
    void getByCode() {
        Board b = new Board();
        b.setCode("aaaa");
        repo.save(b);
        assertEquals(b, sut.getByCode("aaaa").getBody());
    }

    @Test
    void updateIdTest() {
        Board board = new Board("test");
        long oldId = repo.save(board).getId();
        long newId = oldId + 1;

        repo.updateBoardId(oldId, newId);

        assertTrue(repo.getById(oldId) == null);
        assertFalse(repo.getById(newId) == null);
    }

    @Test
    void getDefaultBoardTest() {
        Board defaultBoard = new Board("default");
        long oldId = repo.save(defaultBoard).getId();

        repo.updateBoardId(oldId, 1030L);

        var res = sut.getDefaultBoard();

        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    void getDefaultBoardFailTest() {
        var res = sut.getDefaultBoard();

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void getTaskListTest() {
        Board board = new Board("test");

        List<TaskList> taskLists = new ArrayList<>();
        taskLists.add(new TaskList("list 1"));
        taskLists.add(new TaskList("list 2"));

        board.setTaskLists(taskLists);
        long id = repo.save(board).getId();

        var res = sut.getBoardTaskList(id);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(taskLists, res.getBody());
    }

    @Test
    void getTaskListInvalidIdTest() {
        var res = sut.getBoardTaskList(-1);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void getTaskListFailTest() {
        var res = sut.getBoardTaskList(1);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void getDefaultId() {
        var res = sut.getDefaultId();
        long defaultId = service.getDefaultId();
        assertEquals(res, defaultId);
    }

    @Test
    void messageDelete() {
        Board board = new Board("1030", "oldName");
        board.setId(2001);
        repo.save(board);

        int response = sut.messageDelete(board.getId().toString());

        // Check endpoint
        assertEquals(HttpStatus.OK.value(), response);

        // Check repository
        assertTrue(repo.findAll().contains(board));
    }

}
