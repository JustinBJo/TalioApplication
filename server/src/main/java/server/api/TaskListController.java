package server.api;

import java.util.List;
import java.util.Optional;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;

@RestController
@RequestMapping("/tasklist")
public class TaskListController {

    private final TaskListRepository repo;
    private final BoardRepository boardRepo;
    private final TaskRepository taskRepo;

    /**
     * constructor
     * @param repo the task list repository
     */
    public TaskListController(TaskListRepository repo,
                              BoardRepository boardRepo,
                              TaskRepository taskRepo) {
        this.repo = repo;
        this.boardRepo = boardRepo;
        this.taskRepo = taskRepo;
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
     * Adds a new entity using websocket messages
     * @param taskList new entity
     * @param boardId id of entity's parent
     * @return added entity
     */
    @MessageMapping("/taskList/add/{boardId}")
    @SendTo("/topic/taskList/add/{boardId}")
    public TaskList messageAdd(@Payload TaskList taskList,
                               @DestinationVariable String boardId) {
        long lBoardId;
        try {
            lBoardId = Long.parseLong(boardId);
        } catch (Exception e) {
            return null;
        }
        var res = add(taskList, lBoardId);
        if (res.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return res.getBody();
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

        Optional<Board> optionalParent = boardRepo.findById(boardId);

        if (optionalParent.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        TaskList saved = repo.save(taskList);
        Board parent = optionalParent.get();
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
     * Removes an entity using websocket messages
     * @param id entity id
     * @return removed entity
     */
    @MessageMapping("/taskList/delete/{id}")
    @SendTo("/topic/taskList/delete")
    public TaskList messageDelete(@DestinationVariable String id) {
        long lID;
        try {
            lID = Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }

        var res = delete(lID);
        if (res.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return res.getBody();
    }

    /**
     * Deletes from the repository a tasklist with the provided id
     * @param id the id of the tasklist to be removed
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TaskList> delete(@PathVariable("id") Long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        TaskList taskList = null;
        Board parent = null;

        for (Board board : boardRepo.findAll()) {
            for (TaskList tl : board.getTaskLists()) {
                if (tl.getId().equals(id)) {
                    taskList = tl;
                    parent = board;
                    break;
                }
            }
            if (parent != null) {
                break;
            }
        }

        if (parent != null) {
            boolean unlinkSuccess = parent.removeTaskList(taskList);
            if (!unlinkSuccess) {
                return ResponseEntity.badRequest().build();
            }
            boardRepo.save(parent);
        }

        repo.deleteById(id);

        if (repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(taskList);
    }


    /**
     * Updates the entity name using websocket messages
     * @param id id of updated entity
     * @param newName entity's new name
     * @return updated entity
     */
    @MessageMapping("/taskList/update/{id}/{newName}")
    @SendTo("/topic/taskList/update/{id}")
    public TaskList messageUpdate(@DestinationVariable String id,
                                  @DestinationVariable String newName) {
        long lID;
        try {
            lID = Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }

        var res = update(lID, newName);
        if (res.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return res.getBody();
    }

    /**
     * Updates in the database the name of the given TaskList
     * @param id the id of the TaskList to be renamed
     * @param newName the new name
     */
    @PutMapping("update/{id}/{newName}")
    public ResponseEntity<TaskList> update(@PathVariable("id") long id,
                       @PathVariable("newName") String newName) {
        if (id < 0 || !repo.existsById(id) || newName.length() == 0)
            return ResponseEntity.badRequest().build();
        TaskList param = repo.getById(id);
        param.setTitle(newName);
        TaskList saved = repo.save(param);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
