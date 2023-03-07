package model;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ThemeRequestModel {
    private String boardColor;
    private String listColor;
    private String taskColor;
    private String subtaskColor;

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

    public Color getBoardColor() {
        return COLOR_MAP.get(boardColor);
    }

    public Color getListColor() {
        return COLOR_MAP.get(listColor);
    }

    public Color getTaskColor() {
        return COLOR_MAP.get(taskColor);
    }

    public Color getSubtaskColor() {
        return COLOR_MAP.get(subtaskColor);
    }
}
