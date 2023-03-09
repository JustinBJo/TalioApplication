package server.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Tag;
import server.database.TagRepository;

@RestController
@RequestMapping("/tag")
public class TagController {
    private final TagRepository repo;

    public TagController(TagRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Tag> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
        if (isNullOrEmpty(tag.getName()) || tag.getColor() == null) {
            return ResponseEntity.badRequest().build();
        }

        Tag saved = repo.save(tag);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}

