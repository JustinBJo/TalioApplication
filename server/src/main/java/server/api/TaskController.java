package server.api;

import commons.Subtask;
import commons.Task;
import commons.TaskList;
import commons.TaskTransfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.SubtaskRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repo;
    private final TaskListRepository taskListRepository;
    private final SubtaskRepository subtaskRepo;

    /**
     * constructor of the task controller
     *
     * @param repo               the task repository
     * @param taskListRepository task list repository
     */
    public TaskController(
            TaskRepository repo,
            TaskListRepository taskListRepository,
            SubtaskRepository subtaskRepo) {
        this.repo = repo;
        this.taskListRepository = taskListRepository;
        this.subtaskRepo = subtaskRepo;
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
     * Adds a new entity using websocket messages
     * @param entity new entity
     * @param parentID id of entity's parent
     * @return added entity
     */
    @MessageMapping("/task/add/{parentID}")
    @SendTo("/topic/task/add/{parentID}")
    public Task messageAdd(@Payload Task entity,
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
     * Gets all subtasks belonging to a given task
     * @param taskId ID of the task whose subtasks wil be retrieved
     * @return BAD_REQUEST if the task doesn't exist,
     *         OK with the list of subtasks in body if it does
     */
    @GetMapping("getSubtasks/{taskId}")
    public ResponseEntity<List<Subtask>> getChildSubtasks(
            @PathVariable("taskId") long taskId
    ) {
        if (taskId < 0 || !repo.existsById(taskId)) {
            return ResponseEntity.badRequest().build();
        }

        Task task = repo.getById(taskId);
        return ResponseEntity.ok(task.getSubtasks());
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

    private final Map<Object, Consumer<TaskTransfer>> parentChangeListeners =
            new HashMap<>();

    /**
     * Start listening for a call to updateParent
     * @return deferred result with task transfer results
     */
    @GetMapping("/listen/updateParent")
    public DeferredResult<ResponseEntity<TaskTransfer>> listenParentChange() {
        var res = new DeferredResult<ResponseEntity<TaskTransfer>>(
                5000L, ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        );
        var key = new Object();

        parentChangeListeners.put(
                key,
                taskTransfer -> res.setResult(ResponseEntity.ok(taskTransfer))
        );
        res.onCompletion(() -> parentChangeListeners.remove(key));

        return res;
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

        Long oldParentId = findParentsId(id); // Needs to happen before deleting

        List<Subtask> transfSubtasks = repo.findById(id).get().getSubtasks();

        var deleteResponse = delete(id);
        if (deleteResponse.getStatusCode() != HttpStatus.OK
                || deleteResponse.getBody() == null) {
            return ResponseEntity.badRequest().build();
        }
        Task oldTask = deleteResponse.getBody();
        oldTask.setSubtasks(new ArrayList<>());

        var res = add(oldTask, newParentId);

        if (res.getStatusCode() != HttpStatus.OK || res.getBody() == null) {
            return ResponseEntity.badRequest().build();
        }

        Task newTask = res.getBody();
        for (Subtask subtask : transfSubtasks) {
            Subtask savedSubtask = subtaskRepo.save(subtask);
            newTask.addSubtask(savedSubtask);
        }
        var savedTask = repo.save(newTask);

        parentChangeListeners.forEach((k, v) -> {
            v.accept(
                    new TaskTransfer(
                            oldParentId,
                            deleteResponse.getBody(),
                            newParentId,
                            savedTask
                    )
            );
        });

        return res;
    }

    private Long findParentsId(Long taskId) {
        for (TaskList taskList : taskListRepository.findAll()) {
            for (Task t : taskList.getTasks()) {
                if (t.getId().equals(taskId)) {
                    return taskList.getId();
                }
            }
        }
        return -1L;
    }

    /**
     * @param id the id
     * @return -
     */
    @GetMapping("/findParentId/{id}")
    public ResponseEntity<TaskList> responseParentId(@PathVariable("id")
                                                         long id) {
        Long parentId = findParentsId(id);
        if (parentId < 0) {
            return ResponseEntity.badRequest().build();
        }
        var parent = taskListRepository.findById(parentId);
        if (parent.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(parent.get());
    }

    /**
     * Updates the entity name using websocket messages
     * @param id id of updated entity
     * @param newName entity's new name
     * @return updated entity
     */
    @MessageMapping("/task/updateTitle/{id}/{newName}")
    @SendTo("/topic/task/updateTitle/{id}")
    public Task messageUpdateTitle(@DestinationVariable String id,
                                   @DestinationVariable String newName) {
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

    /**
     * Updates the entity description using websocket messages
     * @param id id of updated entity
     * @param newDescription entity's new description
     * @return updated entity
     */
    @MessageMapping("/task/updateDescription/{id}/{newDescription}")
    @SendTo("/topic/task/updateDescription/{id}")
    public Task messageUpdateDescription(
            @DestinationVariable String id,
            @DestinationVariable String newDescription
    ) {
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

    /**
     * Removes an entity using websocket messages
     * @param id entity id
     * @return removed entity
     */
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

            for (Task t : parent.getTasks()) {
                Task repoTask = repo.findById(t.getId()).get();
                repo.save(repoTask);
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

    /**
     * Updates the entity's children using websocket messages
     * @param id id of updated entity
     * @param childIds ids of new children
     * @return updated entity
     */
    @MessageMapping("/task/updateChildren/{id}/{childIds}")
    @SendTo("/topic/task/updateChildren/{id}")
    public Task messageUpdateChildren(@DestinationVariable String id,
                                      @DestinationVariable String childIds) {
        List<Long> lChildIds = new ArrayList<>();
        long lID;
        try {
            lID = Long.parseLong(id);

            var sIds = childIds.split(",");
            for (var sChildID : sIds) {
                var lChildID =
                        Long.parseLong(sChildID.replaceAll("[^0-9]", ""));
                lChildIds.add(lChildID);
            }
        } catch (Exception e) {
            return null;
        }

        var res = updateSubtasks(lID, lChildIds);
        if (res.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return res.getBody();
    }

    /**
     * Updates the subtasks in a tasks
     * @param id - the id of the task that will be updated
     * @param subtaskIds - list of the ids of subtasks
     */
    @PutMapping("/updateSubtasks/{id}/[{subtaskIds}]")
    public ResponseEntity<Task> updateSubtasks(@PathVariable("id") long id,
                            @PathVariable("subtaskIds") List<Long> subtaskIds) {
        if (id < 0 || !repo.existsById(id))
            return ResponseEntity.badRequest().build();
        Task param = repo.getById(id);
        List<Subtask> subtasks = new ArrayList<>();
        for (long subtaskId : subtaskIds) {
            subtasks.add(subtaskRepo.findById(subtaskId).get());
        }

        param.setSubtasks(subtasks);
        var saved = repo.save(param);
        return ResponseEntity.ok(saved);
    }

}
