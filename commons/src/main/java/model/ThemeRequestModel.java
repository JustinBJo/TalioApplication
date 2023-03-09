package model;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ThemeRequestModel {
    private String boardColor;
    private String listColor;
    private String taskColor;
    private String subtaskColor;

    /**
     * constructor
     */
    public ThemeRequestModel() {
    }

    /**
     * Create a new theme.
     * @param boardColor the color of the board
     * @param listColor the color of the list
     * @param taskColor  the color of the task
     * @param subtaskColor the color of the subtask
     */
    public ThemeRequestModel(String boardColor, String listColor,
                             String taskColor, String subtaskColor) {
        this.boardColor = boardColor;
        this.listColor = listColor;
        this.taskColor = taskColor;
        this.subtaskColor = subtaskColor;
    }

    private static final Map<String, Color> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put("BLACK", Color.BLACK);
        COLOR_MAP.put("BLUE", Color.BLUE);
        COLOR_MAP.put("CYAN", Color.CYAN);
        COLOR_MAP.put("DARK_GRAY", Color.DARK_GRAY);
        COLOR_MAP.put("GRAY", Color.GRAY);
        COLOR_MAP.put("GREEN", Color.GREEN);
        COLOR_MAP.put("LIGHT_GRAY", Color.LIGHT_GRAY);
        COLOR_MAP.put("MAGENTA", Color.MAGENTA);
        COLOR_MAP.put("ORANGE", Color.ORANGE);
        COLOR_MAP.put("PINK", Color.PINK);
        COLOR_MAP.put("RED", Color.RED);
        COLOR_MAP.put("WHITE", Color.WHITE);
        COLOR_MAP.put("YELLOW", Color.YELLOW);
    }

    /**
     * get the color of the board
     * @return the color of the board
     */
    public Color getBoardColor() {
        return COLOR_MAP.get(boardColor);
    }

    /**
     * get the color of the list
     * @return the color of the list
     */
    public Color getListColor() {
        return COLOR_MAP.get(listColor);
    }

    /**
     * get the color of the task
     * @return the color of the task
     */
    public Color getTaskColor() {
        return COLOR_MAP.get(taskColor);
    }

    /**
     * get the color of the subtask
     * @return the color of the subtask
     */
    public Color getSubtaskColor() {
        return COLOR_MAP.get(subtaskColor);
    }
}
