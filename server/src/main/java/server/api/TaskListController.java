package server.api;

import java.util.List;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;

@RestController
@RequestMapping("/tasklist")
public class TaskListController {

    private final TaskListRepository repo;
    private final BoardRepository boardRepo;

    /**
     * constructor
     * @param repo the task list repository
     */
    public TaskListController(TaskListRepository repo,
                              BoardRepository boardRepo) {
        this.repo = repo;
        this.boardRepo = boardRepo;
    }

    /**
     * get a task list by id
     * @param id the id of the task list
     * @return the task list
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskList> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.getById(id));
    }

    /**
     * add a task list
     * @param taskList the task list to add
     * @param boardId id of the board that holds the new list
     * @return the added task list
     */
    @PostMapping(path = { "/{boardId}" })
    public ResponseEntity<TaskList> add(
            @RequestBody TaskList taskList,
            @PathVariable("boardId") long boardId) {

        if (isNullOrEmpty(taskList.getTitle())) {
            return ResponseEntity.badRequest().build();
        }

        if (boardId < 0 || !boardRepo.existsById(boardId)) {
            return ResponseEntity.badRequest().build();
        }

        TaskList saved = repo.save(taskList);
        Board parent = boardRepo.getById(boardId);
        boolean linkSuccess = parent.addTaskList(saved);

        if (!linkSuccess) {
            repo.delete(saved);
            return ResponseEntity.badRequest().build();
        }

        boardRepo.save(parent);
        return ResponseEntity.ok(saved);
    }

    /**
     * Gets all tasks belonging to a given task list
     * @param taskListId ID of the task list whose tasks wil be retrieved
     * @return BAD_REQUEST if the task list doesn't exist,
     *         OK with the list of tasks in body if it does
     */
    @GetMapping("getTasks/{taskListId}")
    public ResponseEntity<List<Task>> getChildTasks(
            @PathVariable("taskListId") long taskListId
    ) {
        if (taskListId < 0 || !repo.existsById(taskListId)) {
            return ResponseEntity.badRequest().build();
        }

        TaskList taskList = repo.getById(taskListId);
        return ResponseEntity.ok(taskList.getTasks());
    }

    /**
     * Deletes from the repository a tasklist with the provided id
     * @param id the id of the tasklist to be removed
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body("Invalid id!");
        }
        if  (!repo.existsById(id)) {
            return ResponseEntity.badRequest().body("TaskList does not exist.");
        }

        TaskList taskList = repo.findById(id).get();

        Board parent = null;
        for (Board board : boardRepo.findAll()) {
            if (board.getTaskLists().contains(taskList)) {
                parent = board;
                break;
            }
        }

        if (parent != null) {
            boolean unlinkSuccess = parent.removeTaskList(taskList);

            if (!unlinkSuccess) {
                return ResponseEntity.badRequest().body(
                        "Failed to unlink task from task list."
                );
            }

            boardRepo.save(parent);
        }

        repo.delete(taskList);
        if (repo.existsById(id)) {
            return ResponseEntity.badRequest().body("The task " +
                    "was not correctly removed.");
        }
        return ResponseEntity.ok("Task " + id + " was removed.");
    }

    /**
     * Updates in the database the name of the given TaskList
     * @param id the id of the TaskList to be renamed
     * @param newName the new name
     */
    @PutMapping("update/{id}/{newName}")
    public void update(@PathVariable("id") long id,
                       @PathVariable("newName") String newName) {
        if (id < 0 || !repo.existsById(id) || newName.length() == 0)
            return;
        TaskList param = repo.getById(id);
        param.setTitle(newName);
        repo.save(param);

    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
