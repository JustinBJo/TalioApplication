package server.api;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

class TagControllerTest {
    private TestTagRepository repo;

    private TagController sut;

    @BeforeEach
    public void setup() {
        repo = new TestTagRepository();
        sut = new TagController(repo);
    }

    @Test
    public void cannotAddNullTag() {
        var actual = sut.add(getTag(null, "BLUE"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        actual = sut.add(getTag("Name", null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        actual = sut.add(getTag(null, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void canAddTag() {
        var actual =
                sut.add(getTag("Tag Name", "BLUE"));
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void databaseIsUsed() {
        sut.add(getTag("name", "BLUE"));
        repo.calledMethods.contains("save");
    }

    private static Tag getTag(String name, String color) {
        return new Tag(name, color);
    }

    @Test
    void getTagById() {
        Tag t = new Tag("Name", "RED");
        repo.save(t);
        assertEquals(t, repo.getById(t.getId()));
    }

    @Test
    void cannotGetTagById() {
        Tag t = new Tag("Name", "BLUE");
        t.setId(2);
        repo.save(t);

        var requested2 = sut.getById(3);
        assertEquals(BAD_REQUEST, requested2.getStatusCode());
    }

    @Test
    void getAllTest() {
        Tag t1 = new Tag("Name", "BLUE");
        Tag t2 = new Tag("Name2", "RED");
        Tag t3 = new Tag("Name3", "GREEN");

        repo.save(t1);
        repo.save(t2);
        //repo.save(t3);

        List<Tag> allTags = sut.getAll();

        assertTrue(allTags.contains(t1));
        assertTrue(allTags.contains(t2));
        assertFalse(allTags.contains(t3));
        assertEquals(2, allTags.size());
    }
}