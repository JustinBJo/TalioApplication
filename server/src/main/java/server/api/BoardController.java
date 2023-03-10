package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardRepository repo;

    /**
     * Constructor
     * @param repo BoardRepository
     */
    public BoardController(BoardRepository repo) {
        this.repo = repo;
    }

    /**
     * Get all boards
     * @return List<Board> all boards
     */
    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return repo.findAll();
    }

    /**
     * Get board by id
     * @param id board id
     * @return ResponseEntity<Board> the board of the given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Add a board to the database
     * @param board board to add
     * @return ResponseEntity<Board> the added board
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {

        if (isNullOrEmpty(board.getCode()) || isNullOrEmpty(board.getTitle())) {
            return ResponseEntity.badRequest().build();
        }

        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}