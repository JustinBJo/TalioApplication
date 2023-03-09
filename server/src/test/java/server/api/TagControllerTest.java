package server.api;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class TagControllerTest {
    private TestTagRepository repo;

    private TagController sut;

    @BeforeEach
    public void setup() {
        repo = new TestTagRepository();
        sut = new TagController(repo);
    }

    @Test
    public void cannotAddNullPerson() {
        var actual = sut.add(getTag(null, "BLUE"));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        actual = sut.add(getTag("Name", null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        actual = sut.add(getTag(null, null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void databaseIsUsed() {
        sut.add(getTag("name", "BLUE"));
        repo.calledMethods.contains("save");
    }

    private static Tag getTag(String name, String color) {
        return new Tag(name,color);
    }

}