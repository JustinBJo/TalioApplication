package server.api;


import commons.Subtask;
import commons.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.SubtaskRepository;
import server.database.TaskRepository;



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
     * Adds a new entity using websocket messages
     * @param entity new entity
     * @param parentID id of entity's parent
     * @return added entity
     */
    @MessageMapping("/subtask/add/{parentID}")
    @SendTo("/topic/subtask/add/{parentID}")
    public Subtask messageAdd(@Payload Subtask entity,
                              @DestinationVariable String parentID) {
        long lParentID;
        try {
            lParentID = Long.parseLong(parentID);
        } catch (Exception e) {
            return null;
        }
        var res = add(entity, lParentID);
        if (res.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return res.getBody();
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
     * Removes an entity using websocket messages
     * @param id entity id
     * @return removed entity
     */
    @MessageMapping("/subtask/delete/{id}")
    @SendTo("/topic/subtask/delete")
    public Subtask messageDelete(@DestinationVariable String id) {
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
     * Deletes a subtask with a given id from the repository
     * @param id the id of the subtask to be deleted
     * @return the deleted subtask
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Subtask> delete(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Subtask subtask = null;
        Task parent = null;

        for (Task task : taskRepo.findAll()) {
            for (Subtask s : task.getSubtasks()) {
                if (s.getId().equals(id)) {
                    subtask = s;
                    parent = task;
                    break;
                }
            }
            if (parent != null) {
                break;
            }
        }

        if (parent != null) {
            boolean unlinkSuccess = parent.removeSubtask(subtask);
            if (!unlinkSuccess) {
                return ResponseEntity.badRequest().build();
            }
            taskRepo.save(parent);
        }

        repo.deleteById(id);
        if (repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(subtask);
    }

    /**
     * Updates the entity name using websocket messages
     * @param id id of updated entity
     * @param newName entity's new name
     * @return updated entity
     */
    @MessageMapping("/subtask/update/{id}/{newName}")
    @SendTo("/topic/subtask/update/{id}")
    public Subtask messageUpdateTitle(@DestinationVariable String id,
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
     * Updates in the database the title of the given subtask
     * @param id the id of the subtask to be edited
     * @param newTitle the new title
     */
    @PutMapping("/update/{id}/{newTitle}")
    public ResponseEntity<Subtask> update(@PathVariable("id") long id,
                       @PathVariable("newTitle") String newTitle) {
        if (id < 0 || !repo.existsById(id) || newTitle.length() == 0)
            return ResponseEntity.badRequest().build();
        Subtask param = repo.findById(id).get();
        param.setTitle(newTitle);
        Subtask saved = repo.save(param);
        return ResponseEntity.ok(saved);
    }
}
