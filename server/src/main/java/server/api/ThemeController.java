package server.api;

import commons.Theme;
import model.ThemeRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ThemeRepository;

import java.awt.*;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private ThemeRepository themeRepository;

    /**
     * Constructor for ThemeController
     * @param themeRepository the repository to use
     */
    public ThemeController(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    /**
     * Creates a new theme
     * @param themeRequestModel the theme to create
     * @return the created theme
     */
    @PostMapping("/")
    public ResponseEntity<Theme> createTheme(@RequestBody ThemeRequestModel themeRequestModel) {
        Color board = themeRequestModel.getBoardColor();
        Color list = themeRequestModel.getListColor();
        Color task = themeRequestModel.getTaskColor();
        Color subtask = themeRequestModel.getSubtaskColor();
        Theme theme = new Theme(board, list, task, subtask);
        Theme savedTheme = themeRepository.save(theme);
        return new ResponseEntity<>(savedTheme, HttpStatus.CREATED);
    }

    /**
     * Gets a theme by id
     * @param id the id of the theme
     * @return the theme
     */
    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable Long id) {
        Theme theme = themeRepository.findById(id).orElse(null);
        return ResponseEntity.ok(theme);
    }
}
