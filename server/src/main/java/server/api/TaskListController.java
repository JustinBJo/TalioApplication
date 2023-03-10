package server.api;

import java.util.List;

import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.TaskListRepository;

@RestController
@RequestMapping("/tasklist")
public class TaskListController {

    private final TaskListRepository repo;

    /**
     * constructor
     * @param repo the task list repository
     */
    public TaskListController(TaskListRepository repo) {
        this.repo = repo;
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
}
