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

    /**
     * constructor
     * @param repo the tag repository
     */
    public TagController(TagRepository repo) {
        this.repo = repo;
    }

    /**
     * get all the tags
     * @return all the tags
     */
    @GetMapping(path = { "", "/" })
    public List<Tag> getAll() {
        return repo.findAll();
    }

    /**
     * get a tag by id
     * @param id the id of the tag
     * @return the tag
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * add a tag
     * @param tag the tag to add
     * @return the added tag
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
        if (isNullOrEmpty(tag.getName()) || tag.getColor() == null) {
            return ResponseEntity.badRequest().build();
        }

        Tag saved = repo.save(tag);
        return ResponseEntity.ok(saved);
    }

    /**
     * check if a string is null or empty
     * @param s the string to check
     * @return true if the string is null or empty, false otherwise
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}

