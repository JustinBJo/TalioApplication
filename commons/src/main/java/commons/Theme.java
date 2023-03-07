package commons;

import javax.persistence.*;
import java.awt.Color;
import java.util.Objects;

@Entity
@Table(name = "themes")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_color")
    private Color boardColor;

    @Column(name = "list_color")
    private Color listColor;

    @Column(name = "task_color")
    private Color taskColor;

    @Column(name = "subtask_color")
    private Color subtaskColor;

    // default constructor
    public Theme() {
    }

    // constructor with parameters
    public Theme(Color boardColor, Color listColor, Color taskColor, Color subtaskColor) {
        this.boardColor = boardColor;
        this.listColor = listColor;
        this.taskColor = taskColor;
        this.subtaskColor = subtaskColor;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Color getBoardColor() {
        return boardColor;
    }

    public void setBoardColor(Color boardColor) {
        this.boardColor = boardColor;
    }

    public Color getListColor() {
        return listColor;
    }

    public void setListColor(Color listColor) {
        this.listColor = listColor;
    }

    public Color getTaskColor() {
        return taskColor;
    }

    public void setTaskColor(Color taskColor) {
        this.taskColor = taskColor;
    }

    public Color getSubtaskColor() {
        return subtaskColor;
    }

    public void setSubtaskColor(Color subtaskColor) {
        this.subtaskColor = subtaskColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

