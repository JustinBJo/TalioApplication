package server.api;

import commons.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ThemeRepository;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    @Autowired
    private ThemeRepository themeRepository;

    @PostMapping("/")
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        Theme savedTheme = themeRepository.save(theme);
        return new ResponseEntity<>(savedTheme, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable Long id) {
        Theme theme = themeRepository.findById(id).orElse(null);
        return ResponseEntity.ok(theme);
    }
}
