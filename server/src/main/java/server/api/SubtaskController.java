package server.api;


import commons.Subtask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.SubtaskRepository;

import java.util.List;



@RestController
@RequestMapping("/subtask")
public class SubtaskController {

    private final SubtaskRepository repo;

    /**
     * constructor
     * @param repo the subtask repository
     */
    public SubtaskController(SubtaskRepository repo) {
        this.repo = repo;
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
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Subtask> add(@RequestBody Subtask subtask) {
        if (subtask.getTitle() == null || subtask.getTitle().isEmpty())
            return ResponseEntity.badRequest().build();

        Subtask saved = repo.save(subtask);
        return ResponseEntity.ok(saved);

    }
}
