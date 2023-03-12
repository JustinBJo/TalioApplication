package server.api;

import java.util.List;

import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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


    /**
     * Deletes from the repository a tasklist with the provided id
     * @param id the id of the tasklist to be removed
     */
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return;
        }
        TaskList param = repo.getById(id);
        repo.delete(param);

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
