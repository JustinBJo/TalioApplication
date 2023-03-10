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

    /**
     * empty constructor for object mapper
     */
    public Theme() {
    }


    /**
     * Create a new theme.
     * @param boardColor the color of the board
     * @param listColor the color of the list
     * @param taskColor  the color of the task
     * @param subtaskColor the color of the subtask
     */
    public Theme(Color boardColor, Color listColor,
                 Color taskColor, Color subtaskColor) {
        this.boardColor = boardColor;
        this.listColor = listColor;
        this.taskColor = taskColor;
        this.subtaskColor = subtaskColor;
    }

    /**
     * get the id of the theme
     * @return the id of the theme
     */
    public Long getId() {
        return id;
    }

    /**
     * set the id of the theme
     * @param id the id of the theme
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get the board color of the theme
     * @return the board color of the theme
     */
    public Color getBoardColor() {
        return boardColor;
    }

    /**
     * set the board color of the theme
     * @param boardColor the board color of the theme
     */
    public void setBoardColor(Color boardColor) {
        this.boardColor = boardColor;
    }

    /**
     * get the list color of the theme
     * @return the list color of the theme
     */
    public Color getListColor() {
        return listColor;
    }

    /**
     * set the list color of the theme
     * @param listColor the list color of the theme
     */
    public void setListColor(Color listColor) {
        this.listColor = listColor;
    }

    /**
     * get the task color of the theme
     * @return the task color of the theme
     */
    public Color getTaskColor() {
        return taskColor;
    }

    /**
     * set the task color of the theme
     * @param taskColor the task color of the theme
     */
    public void setTaskColor(Color taskColor) {
        this.taskColor = taskColor;
    }

    /**
     * get the subtask color of the theme
     * @return the subtask color of the theme
     */
    public Color getSubtaskColor() {
        return subtaskColor;
    }

    /**
     * set the subtask color of the theme
     * @param subtaskColor the subtask color of the theme
     */
    public void setSubtaskColor(Color subtaskColor) {
        this.subtaskColor = subtaskColor;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id)
                && Objects.equals(boardColor, theme.boardColor)
                && Objects.equals(listColor, theme.listColor)
                && Objects.equals(taskColor, theme.taskColor)
                && Objects.equals(subtaskColor, theme.subtaskColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, boardColor, listColor, taskColor, subtaskColor);
    }
}

