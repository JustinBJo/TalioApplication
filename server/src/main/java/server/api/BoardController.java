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
     * searches for a board based on its code
     * @param code the code of the board
     * @return the board
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Board> getByCode(@PathVariable("code") String code) {
        List<Board> boards = repo.findAll();
        for (Board k :  boards) {
            if (k.getCode().equals(code))
                return ResponseEntity.ok(k);
        }
        return ResponseEntity.badRequest().build();
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

    /**
     * Rename the Board in the repository
     * @param id the id of the board that is being renamed
     * @param newName the new title of the board
     */
    @PutMapping("update/{id}/{newName}")
    public ResponseEntity<Board> updateName(@PathVariable("id") long id,
                       @PathVariable("newName") String newName) {
        if (id < 0 || !repo.existsById(id) || newName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Board board = repo.findById(id).get();
        board.setTitle(newName);
        repo.save(board);
        return ResponseEntity.ok(board);
    }

    /**
     * Delete the board from the repository
     * @param id the id of the board that is being removed
     * @return the deleted board
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body("Invalid id!");
        }
        if  (!repo.existsById(id)) {
            return ResponseEntity.badRequest().body("Board does not exist.");
        }
        Board board = repo.findById(id).get();
        repo.delete(board);
        return ResponseEntity.ok("Board " + id + " is removed.");
    }

    /**
     * checks whether string s is null or empty
     * @param s the string to be checked
     * @return true if condition is met,
     * false otherwise
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}