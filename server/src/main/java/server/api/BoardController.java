package server.api;

import commons.Board;
import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.TaskListRepository;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardRepository repo;
    private final TaskListRepository taskListRepo;

    /**
     * Constructor
     * @param repo BoardRepository
     * @param taskListRepo TaskListRepository
     */
    public BoardController(
            BoardRepository repo,
            TaskListRepository taskListRepo
    ) {
        this.repo = repo;
        this.taskListRepo = taskListRepo;
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
     * Links a task list to a board in the repository
     * @param boardId ID of the board
     * @param taskListId ID of the task list to be linked
     */
    @PutMapping("addTaskList/{boardId}/{taskListId}")
    public ResponseEntity<String> linkBoardToTaskList(
            @PathVariable("boardId") long boardId,
            @PathVariable("taskListId") long taskListId
    ) {
        if (boardId < 0 || !repo.existsById(boardId)
                || taskListId < 0 || !taskListRepo.existsById(taskListId)) {
            return ResponseEntity.badRequest().build();
        }

        Board board = repo.getById(boardId);
        TaskList taskList = taskListRepo.getById(taskListId);

        boolean success = board.addTaskList(taskList);

        if (!success) return ResponseEntity.badRequest().build();

        repo.save(board);
        return ResponseEntity.ok("Added Task List " + taskListId + " to board " + boardId);
    }

    /**
     * Unlinks a task list from a board in the repository
     * @param taskListId ID of the task list to be unlinked
     */
    @PutMapping("removeTaskList/{taskListId}")
    public ResponseEntity<Board> unlinkBoardFromTaskList(
            @PathVariable("taskListId") long taskListId
    ) {
        if (taskListId < 0 || !taskListRepo.existsById(taskListId)) {
            return ResponseEntity.badRequest().build();
        }

        TaskList taskList = taskListRepo.getById(taskListId);
        for (Board board : repo.findAll()) {
            if (!board.getTaskLists().contains(taskList)) {
                continue;
            }
            // If this is reached, board is the board containing taskList

            boolean success = board.removeTaskList(taskList);
            if (!success) return ResponseEntity.badRequest().build();

            repo.save(board);

            return ResponseEntity.ok(board);
        }
        // List doesn't belong to any board
        return ResponseEntity.badRequest().build();
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

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}