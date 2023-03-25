package server.api;

import commons.Task;
import commons.TaskList;
import org.springframework.http.ResponseEntity;
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
    public TaskController(TaskRepository repo, TaskListRepository taskListRepository) {
        this.repo = repo;
        this.taskListRepository = taskListRepository;
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

    /**
     * Deletes a task with a given id from the repository
     * @param id the id of the task to be deleted
     * @return the deleted task
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (id < 0)
            return ResponseEntity.badRequest().body("Invalid id.");
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().body("The task " +
                    "you are trying to delete does not exist.");
        }

        Task task = repo.getById(id);

        TaskList parent = null;
        for (TaskList taskList: taskListRepository.findAll()){
            if (taskList.getTasks().contains(task)) {
                parent = taskList;
                break;
            }
        }

        if (parent != null) {
            boolean unlinkSuccess = parent.removeTask(task);

            if (!unlinkSuccess) {
                return ResponseEntity.badRequest().body(
                        "Failed to unlink task from task list."
                );
            }

            taskListRepository.save(parent);
        }

        repo.delete(task);
        if (repo.existsById(id)) {
            return ResponseEntity.badRequest().body("The task " +
                    "was not correctly removed.");
        }
        return ResponseEntity.ok("Task " + id + " was removed.");
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
