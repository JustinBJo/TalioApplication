package server.api;

import commons.Theme;
import model.ThemeRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ThemeControllerTest {
    private ThemeController sut;
    private TestThemeRepository repo;

    @BeforeEach
    void setUp() {
        repo = new TestThemeRepository();
        sut = new ThemeController(repo);
    }

    @Test
    void createTheme() {
        ThemeRequestModel request = new ThemeRequestModel("BLACK", "BLACK", "BLACK", "BLACK");
        var actual = sut.createTheme(request);
        assertEquals(201, actual.getStatusCodeValue());
        //verify the repo
        repo.calledMethods.contains("save");

    }

    @Test
    void getThemeById() {
        ThemeRequestModel request = new ThemeRequestModel("BLACK", "BLACK", "BLACK", "BLACK");
        Theme t = new Theme(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        t.setId(1L);
        repo.save(t);
        var actual = sut.getThemeById(1L);
        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(t, actual.getBody());
        repo.calledMethods.contains("findById");
    }
}