package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;

import java.util.Random;

/**
 * Модель лабиринта
 *
 * Хранит структуру лабиринта, генерирует стены и гарантирует проходимость.
 */
public class MazeModel {
    private final CellType[][] grid;
    private final int width, height;
    private final Point robotStartPosition, treasurePosition;

    /**
     * Создаёт лабиринт заданного размера
     *
     * @param width ширина
     * @param height высота
     */
    public MazeModel(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new CellType[height][width];
        initializeGrid();
        robotStartPosition = new Point(1, 1);
        treasurePosition = new Point(width - 2, height - 2);
        grid[treasurePosition.y()][treasurePosition.x()] = CellType.TREASURE;
    }

    /** Генерирует лабиринт */
    private void initializeGrid() {
        Random rnd = new Random();
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) grid[y][x] = CellType.EMPTY;
        createOuterWalls();
        addRandomWalls(rnd);
        carveGuaranteedPath(rnd);
        clearStartAndEndAreas();
    }

    private void createOuterWalls() {
        for (int x = 0; x < width; x++) { grid[0][x] = CellType.WALL; grid[height - 1][x] = CellType.WALL; }
        for (int y = 0; y < height; y++) { grid[y][0] = CellType.WALL; grid[y][width - 1] = CellType.WALL; }
    }

    private void addRandomWalls(Random rnd) {
        for (int y = 1; y < height - 1; y++)
            for (int x = 1; x < width - 1; x++)
                if (rnd.nextDouble() < 0.3) grid[y][x] = CellType.WALL;
    }

    private void carveGuaranteedPath(Random rnd) {
        int cx = 1, cy = 1;
        while (cx < width - 2 || cy < height - 2) {
            grid[cy][cx] = CellType.EMPTY;
            if (cx < width - 2 && rnd.nextBoolean()) cx++; else if (cy < height - 2) cy++;
            if (cx > 1 && cx < width - 1) grid[cy][cx - 1] = CellType.EMPTY;
            if (cy > 1 && cy < height - 1) grid[cy - 1][cx] = CellType.EMPTY;
        }
        grid[height - 2][width - 2] = CellType.EMPTY;
        grid[1][1] = CellType.EMPTY;
    }

    private void clearStartAndEndAreas() {
        for (int y = 1; y <= 3 && y < height - 1; y++) for (int x = 1; x <= 3 && x < width - 1; x++) grid[y][x] = CellType.EMPTY;
        for (int y = height - 3; y <= height - 2 && y >= 1; y++) for (int x = width - 3; x <= width - 2 && x >= 1; x++) grid[y][x] = CellType.EMPTY;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public CellType getCell(int x, int y) { return grid[y][x]; }
    public boolean isPassable(int x, int y) { return x >= 0 && x < width && y >= 0 && y < height && grid[y][x] != CellType.WALL; }
    public Point getRobotStart() { return robotStartPosition; }
    public Point getTreasure() { return treasurePosition; }
}