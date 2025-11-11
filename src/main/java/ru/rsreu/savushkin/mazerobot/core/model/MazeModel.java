package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;

import java.util.Random;

/**
 * Модель лабиринта — только хранение сетки
 */
public class MazeModel {
    private final CellType[][] grid;
    private final int width, height;

    public MazeModel(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new CellType[height][width];
        initializeGrid();
        grid[height - 2][width - 2] = CellType.TREASURE;
    }

    private void initializeGrid() {
        Random rnd = new Random();
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) grid[y][x] = CellType.EMPTY;
        for (int x = 0; x < width; x++) { grid[0][x] = CellType.WALL; grid[height - 1][x] = CellType.WALL; }
        for (int y = 0; y < height; y++) { grid[y][0] = CellType.WALL; grid[y][width - 1] = CellType.WALL; }
        for (int y = 1; y < height - 1; y++) for (int x = 1; x < width - 1; x++) if (rnd.nextDouble() < 0.3) grid[y][x] = CellType.WALL;

        int cx = 1, cy = 1;
        while (cx < width - 2 || cy < height - 2) {
            grid[cy][cx] = CellType.EMPTY;
            if (cx < width - 2 && rnd.nextBoolean()) cx++; else if (cy < height - 2) cy++;
            if (cx > 1 && cx < width - 1) grid[cy][cx - 1] = CellType.EMPTY;
            if (cy > 1 && cy < height - 1) grid[cy - 1][cx] = CellType.EMPTY;
        }
        grid[height - 2][width - 2] = CellType.EMPTY;
        grid[1][1] = CellType.EMPTY;
        for (int y = 1; y <= 3 && y < height - 1; y++) for (int x = 1; x <= 3 && x < width - 1; x++) grid[y][x] = CellType.EMPTY;
        for (int y = height - 3; y <= height - 2 && y >= 1; y++) for (int x = width - 3; x <= width - 2 && x >= 1; x++) grid[y][x] = CellType.EMPTY;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public CellType getCell(int x, int y) { return grid[y][x]; }
    public boolean isPassable(int x, int y) { return x >= 0 && x < width && y >= 0 && y < height && grid[y][x] != CellType.WALL; }
}