package server.api;

import commons.Task;
import commons.TaskList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repo;
    private final TaskListRepository taskListRepository;

    /**
     * constructor of the task controller
     *
     * @param repo               the task repository
     * @param taskListRepository task list repository
     */
    public TaskController(
            TaskRepository repo,
            TaskListRepository taskListRepository
    ) {
        this.repo = repo;
        this.taskListRepository = taskListRepository;
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

    @MessageMapping("/task/add/{parentID}")
    @SendTo("/topic/task/add")
    public Task messageAdd(@Payload Task entity, @DestinationVariable String parentID) {
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
     * add a task
     * @param task the task to add
     * @return the added task
     */
    @PostMapping(path = { "/{taskListId}" })
    public ResponseEntity<Task> add(
            @RequestBody Task task,
            @PathVariable("taskListId") long taskListId
            ) {

        if (isNullOrEmpty(task.getTitle())) {
            return ResponseEntity.badRequest().build();
        }

        if (taskListId < 0 || !taskListRepository.existsById(taskListId)) {
            return ResponseEntity.badRequest().build();
        }

        Task saved = repo.save(task);
        TaskList parent = taskListRepository.getById(taskListId);
        boolean linkSuccess = parent.addTask(saved);

        if (!linkSuccess) {
            repo.delete(saved);
            return ResponseEntity.badRequest().build();
        }

        taskListRepository.save(parent);
        return ResponseEntity.ok(saved);
    }

    /**
     * Sets a new task list to hold a given task
     * @param id id of the changed task
     * @param newParentId id of the list that now holds the task
     * @return updated task
     */
    @PutMapping("/updateParent/{id}/{newParentId}")
    public ResponseEntity<Task> updateParent(
            @PathVariable("id") long id,
            @PathVariable("newParentId") long newParentId
    ) {
        if (id < 0 || !repo.existsById(id) || newParentId < 0
                || !taskListRepository.existsById(newParentId)) {
            return ResponseEntity.badRequest().build();
        }

        var deleteResponse = delete(id);
        if (deleteResponse.getStatusCode() != HttpStatus.OK
                || deleteResponse.getBody() == null) {
            return ResponseEntity.badRequest().build();
        }

        return add(deleteResponse.getBody(), newParentId);
    }

    @MessageMapping("/task/updateTitle/{id}/{newName}")
    @SendTo("/topic/task/update/{id}")
    public Task messageUpdateTitle(@DestinationVariable String id, @DestinationVariable String newName) {
        long lID;
        try {
            lID = Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }

        var res = updateTitle(lID, newName);
        if (res.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return res.getBody();
    }

    /**
     * Updates in the database the title of the given Task
     * @param id the id of the Task to be edited
     * @param newTitle the new title
     */
    @PutMapping("/updateTitle/{id}/{newTitle}")
    public ResponseEntity<Task> updateTitle(@PathVariable("id") long id,
                       @PathVariable("newTitle") String newTitle) {
        if (id < 0 || !repo.existsById(id) || newTitle.length() == 0)
            return ResponseEntity.badRequest().build();
        Task param = repo.getById(id);
        param.setTitle(newTitle);
        Task saved = repo.save(param);
        return ResponseEntity.ok(saved);
    }

    @MessageMapping("/task/updateDescription/{id}/{newDescription}")
    @SendTo("/topic/task/update/{id}")
    public Task messageUpdateDescription(@DestinationVariable String id, @DestinationVariable String newDescription) {
        long lID;
        try {
            lID = Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }

        var res = updateDescription(lID, newDescription);
        if (res.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return res.getBody();
    }

    /**
     * Updates in the database the description of the given Task
     * @param id the id of the Task to be edited
     * @param newDescription the new description
     */
    @PutMapping("/updateDescription/{id}/{newDescription}")
    public ResponseEntity<Task> updateDescription(@PathVariable("id") long id,
                       @PathVariable("newDescription") String newDescription) {
        if (id < 0 || !repo.existsById(id) || newDescription.length() == 0)
            return ResponseEntity.badRequest().build();
        Task param = repo.getById(id);
        if (newDescription.equals("HARDCODED-EMPTY-" +
                "DESCRIPTION-METHOD-FOR-EDITING-TASKS"))
            param.setDescription("");
        else
            param.setDescription(newDescription);
        Task saved = repo.save(param);
        return ResponseEntity.ok(saved);
    }

    @MessageMapping("/task/delete/{id}")
    @SendTo("/topic/task/delete")
    public Task messageDelete(@DestinationVariable String id) {
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
     * Deletes a task with a given id from the repository
     * @param id the id of the task to be deleted
     * @return the deleted task
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Task> delete(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Task task = null;
        TaskList parent = null;

        for (TaskList taskList : taskListRepository.findAll()) {
            for (Task t : taskList.getTasks()) {
                if (t.getId().equals(id)) {
                    task = t;
                    parent = taskList;
                    break;
                }
            }
            if (parent != null) {
                break;
            }
        }

        if (parent != null) {
            boolean unlinkSuccess = parent.removeTask(task);
            if (!unlinkSuccess) {
                return ResponseEntity.badRequest().build();
            }
            taskListRepository.save(parent);
        }

        repo.deleteById(id);

        if (repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(task);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
