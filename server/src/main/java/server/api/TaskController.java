package server.api;

import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repo;

    /**
     * constructor of the task controller
     * @param repo the task repository
     */
    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    /**
     * get all the tasks
     * @return all the tasks
     */
    @GetMapping(path = { "", "/" })
    public List<Task> getAll() {
        return repo.findAll();
    }

    /**
     * get a task by id
     * @param id the id of the task
     * @return the task
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.getById(id));
    }

    /**
     * add a task
     * @param task the task to add
     * @return the added task
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Task> add(@RequestBody Task task) {

        if (isNullOrEmpty(task.getTitle())) {
            return ResponseEntity.badRequest().build();
        }

        Task saved = repo.save(task);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates in the database the title of the given Task
     * @param id the id of the Task to be edited
     * @param newTitle the new title
     */
    @PutMapping("/updateTitle/{id}/{newTitle}")
    public void updateTitle(@PathVariable("id") long id,
                       @PathVariable("newTitle") String newTitle) {
        if (id < 0 || !repo.existsById(id) || newTitle.length() == 0)
            return;
        Task param = repo.getById(id);
        param.setTitle(newTitle);
        repo.save(param);

    }

    /**
     * Updates in the database the description of the given Task
     * @param id the id of the Task to be edited
     * @param newDescription the new description
     */
    @PutMapping("/updateDescription/{id}/{newDescription}")
    public void updateDescription(@PathVariable("id") long id,
                       @PathVariable("newDescription") String newDescription) {
        if (id < 0 || !repo.existsById(id) || newDescription.length() == 0)
            return;
        Task param = repo.getById(id);
        param.setDescription(newDescription);
        repo.save(param);

    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
