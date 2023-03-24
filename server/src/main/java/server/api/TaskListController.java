package server.api;

import java.util.List;

import commons.Task;
import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TaskListRepository;
import server.database.TaskRepository;

@RestController
@RequestMapping("/tasklist")
public class TaskListController {

    private final TaskListRepository repo;
    private final TaskRepository taskRepo;

    /**
     * constructor
     * @param repo the task list repository
     */
    public TaskListController(TaskListRepository repo,
                              TaskRepository taskRepo) {
        this.repo = repo;
        this.taskRepo = taskRepo;
    }

    /**
     * get all the task lists
     * @return all the task lists
     */
    @GetMapping(path = { "", "/" })
    public List<TaskList> getAll() {
        return repo.findAll();
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
     * @return the added task list
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<TaskList> add(@RequestBody TaskList taskList) {

        if (taskList.getTitle() == null || taskList.getTasks() == null) {
            return ResponseEntity.badRequest().build();
        }

        TaskList saved = repo.save(taskList);
        return ResponseEntity.ok(saved);
    }

    /**
     * Add a task to a specific list
     * @param taskListId the id of the list that the task is being added to
     * @param taskId the id of the task that is being added
     * @return OK if the task has been added to the tasklist,
     *         BAD_REQUEST otherwise
     */
    @PutMapping("addTask/{taskListId}/{taskId}")
    public ResponseEntity<String> addChildTask(
            @PathVariable("taskListId") long taskListId,
            @PathVariable("taskId") long taskId
    ) {
        if (taskListId < 0 || !repo.existsById(taskListId)
                || taskId < 0 || !taskRepo.existsById(taskId)) {
            return ResponseEntity.badRequest().build();
        }

        TaskList taskList = repo.getById(taskListId);
        Task task = taskRepo.getById(taskId);

        boolean success = taskList.addTask(task);
        if (!success) return ResponseEntity.badRequest().build();

        repo.save(taskList);
        return ResponseEntity.ok(
                "Added Task " + taskId + " to List " + taskListId
        );
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
        repo.delete(taskList);
        return ResponseEntity.ok("TaskList " + id + " is removed.");
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

}
