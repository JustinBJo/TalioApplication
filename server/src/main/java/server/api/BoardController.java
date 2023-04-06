package server.api;

import commons.Board;
import commons.TaskList;
import commons.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.UserRepository;
import server.service.DefaultBoardService;
import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardRepository repo;
    private final DefaultBoardService defaultBoardService;
    private final UserRepository userRepository;

    private static final long DEFAULT_ID = 1030;

    /**
     * Constructor
     *
     * @param repo           BoardRepository
     */
    public BoardController(
            BoardRepository repo,
            DefaultBoardService defaultBoardService,
            UserRepository userRepository) {
        this.repo = repo;
        this.defaultBoardService = defaultBoardService;
        this.userRepository = userRepository;
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
     * Get the ID of the default board in the system
     * @return the default ID stored in DefaultBoardService
     */
    @GetMapping("defaultId")
    public long getDefaultId() {
        return this.defaultBoardService.getDefaultId();
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
     * Updates the board name using websocket messages
     * @param id id of updated board
     * @param newName board's new name
     * @return updated board
     */
    @MessageMapping("/board/update/{id}/{newName}")
    @SendTo("/topic/board/update/{id}")
    public Board messageUpdate(@DestinationVariable String id,
                               @DestinationVariable String newName) {
        long lID;
        try {
            lID = Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }

        var res = updateName(lID, newName);
        if (res.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return res.getBody();
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
     * Removes an entity using websocket messages
     * @param id entity id
     * @return removed entity
     */
    @MessageMapping("/board/delete/{id}")
    @SendTo("/topic/board/delete/{id}")
    public int messageDelete(@DestinationVariable String id) {
        long lID;
        try {
            lID = Long.parseLong(id);
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST.value();
        }

        var res = deleteBoard(lID);
        return res.getStatusCode().value();
    }

    /**
     * Delete the board from the repository
     * @param id the id of the board that is being removed
     * @return the deleted board
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body("Invalid id!");
        }
        if  (!repo.existsById(id)) {
            return ResponseEntity.badRequest().body("Board does not exist.");
        }

        Board board = null;
        User parent = null;

        for (User user : userRepository.findAll()) {
            for (Board b : user.getBoards()) {
                if (b.getId().equals(id)) {
                    board = b;
                    parent = user;
                    break;
                }
            }
            if (parent != null) {
                break;
            }
        }

        if (parent != null) {
            boolean unlinkSuccess = parent.removeBoard(board);
            if (!unlinkSuccess) {
                return ResponseEntity.badRequest().build();
            }
            userRepository.save(parent);
        }

        repo.deleteById(id);

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

    /**
     * Gets the default board of the application
     * @return null if the board does not exist, the default board otherwise
     */
    @GetMapping("default")
    public ResponseEntity<Board> getDefaultBoard() {
        return getById(DEFAULT_ID);
    }

    /**
     * Gets the tasklist of a board from the repository
     * @param id the id of the board
     * @return the tasklist of the board
     */
    @GetMapping("{id}/tasklist")
    public ResponseEntity<List<TaskList>> getBoardTaskList(
            @PathVariable("id") long id
    ) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Board board = repo.findById(id).get();
        return ResponseEntity.ok(board.getTaskLists());
    }

}