package server.api;


import commons.Subtask;
import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.SubtaskRepository;
import server.database.TaskRepository;

import java.util.List;



@RestController
@RequestMapping("/subtask")
public class SubtaskController {

    private final SubtaskRepository repo;

    private final TaskRepository taskRepo;

    /**
     * constructor
     * @param repo the subtask repository
     */
    public SubtaskController(SubtaskRepository repo, TaskRepository taskRepo) {
        this.repo = repo;
        this.taskRepo = taskRepo;
    }
    /**
     * get all the subtasks
     * @return all the subtasks
     */
    @GetMapping(path = {"", "/"} )
    public List<Subtask> getAll() {
        return repo.findAll();
    }

    /**
     * get a subtask by id
     * @param id the id of the subtask
     * @return the subtask
     */
    @GetMapping("/{id}")
    public ResponseEntity<Subtask> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * add a subtask
     * @param subtask the subtask to add
     * @return the added subtask
     */
    @PostMapping("/{taskId}")
    public ResponseEntity<Subtask> add(
            @RequestBody Subtask subtask,
            @PathVariable("taskId") long taskId
    ) {

        if (subtask.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (taskId < 0 || !taskRepo.existsById(taskId)) {
            return ResponseEntity.badRequest().build();
        }

        Subtask saved = repo.save(subtask);
        Task parent = taskRepo.getById(taskId);
        boolean linkSuccess = parent.addSubtask(saved);

        if (!linkSuccess) {
            repo.delete(saved);
            return ResponseEntity.badRequest().build();
        }

        taskRepo.save(parent);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a subtask with a given id from the repository
     * @param id the id of the subtask to be deleted
     * @return the deleted subtask
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (id < 0)
            return ResponseEntity.badRequest().body("Invalid id.");
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().body("The subtask " +
                    "you are trying to delete does not exist.");
        }

        //Subtask subtask = repo.getById(id);
        Subtask subtask = repo.findById(id).get();


        Task parent = null;
        for (Task task : taskRepo.findAll()) {
            if (task.getSubtasks().contains(subtask)) {
                parent = task;
                break;
            }
        }

        if (parent != null) {
            boolean unlinkSuccess = parent.removeSubtask(subtask);

            if (!unlinkSuccess) {
                return ResponseEntity.badRequest().body(
                        "Failed to unlink subtask from parent task."
                );
            }

            taskRepo.save(parent);
        }

        repo.delete(subtask);
        if (repo.existsById(id)) {
            return ResponseEntity.badRequest().body("The subtask " +
                    "was not correctly removed.");
        }
        return ResponseEntity.ok("Subtask " + id + " was removed.");
    }

    /**
     * Updates in the database the title of the given subtask
     * @param id the id of the subtask to be edited
     * @param newTitle the new title
     */
    @PutMapping("/update/{id}/{newTitle}")
    public void update(@PathVariable("id") long id,
                       @PathVariable("newTitle") String newTitle) {
        if (id < 0 || !repo.existsById(id) || newTitle.length() == 0)
            return;
        Subtask param = repo.findById(id).get();
        param.setTitle(newTitle);
        repo.save(param);

    }

    /**
     * Updates in the database the status of the subtask
     * @param id the id of the subtask to be edited
     * @param newValue the new title
     */
    @PutMapping("/updateCompleteness/{id}/{newValue}")
    public void updateCompleteness(@PathVariable("id") long id,
                       @PathVariable("newValue") boolean newValue) {
        if (id < 0 || !repo.existsById(id))
            return;
        Subtask param = repo.findById(id).get();
        param.setCompleted(newValue);
        repo.save(param);

    }
}
