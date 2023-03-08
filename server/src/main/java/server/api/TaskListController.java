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

    public TaskListController(TaskListRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<TaskList> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskList> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.getById(id));
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<TaskList> add(@RequestBody TaskList taskList) {

        if (taskList.getTitle() == null || taskList.getTasks() == null) {
            return ResponseEntity.badRequest().build();
        }

        TaskList saved = repo.save(taskList);
        return ResponseEntity.ok(saved);
    }
}
