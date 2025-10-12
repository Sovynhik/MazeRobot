package ru.rsreu.savushkin.mazerobot.core.entity;

import java.util.Random;

public class Maze {
    private CellType[][] grid;
    private int width;
    private int height;
    private int robotX, robotY;
    private int treasureX, treasureY;
    private Random random;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.random = new Random();
        this.grid = new CellType[height][width];
        initializeMaze();
        placeRobotAndTreasure();
    }

    /**
     * Устанавливает, является ли клетка проходимой (EMPTY) или непроходимой (WALL).
     * Это необходимо для гарантирования проходимости стартовой и целевой точек.
     * @param x Координата X.
     * @param y Координата Y.
     * @param isPassable true, если клетка должна быть EMPTY; false, если WALL.
     */
    public void setCellPassable(int x, int y, boolean isPassable) {
        // Добавьте проверку границ для безопасности
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            // Если вы используете CellType.TREASURE для цели,
            // этот код гарантирует, что она будет пустой или кладом.
            if (isPassable) {
                // Если клетка — цель, не затирайте её как EMPTY
                if (grid[y][x] != CellType.TREASURE) {
                    this.grid[y][x] = CellType.EMPTY;
                }
            } else {
                this.grid[y][x] = CellType.WALL;
            }
        }
    }

    private void initializeMaze() {
        // Сначала заполняем все клетки как пустые
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = CellType.EMPTY;
            }
        }

        // Создаем границу из стен
        createBorderWalls();

        // Генерируем случайные стены только внутри границы
        generateInnerWalls();

        // Гарантируем, что есть проход от робота до клада
        createPath();

        // Убеждаемся, что старт и финиш не являются стенами
        ensureStartAndFinishArePassable();
    }

    private void createBorderWalls() {
        // Верхняя и нижняя границы
        for (int x = 0; x < width; x++) {
            grid[0][x] = CellType.WALL;           // Верхняя граница
            grid[height - 1][x] = CellType.WALL;  // Нижняя граница
        }

        // Левая и правая границы
        for (int y = 0; y < height; y++) {
            grid[y][0] = CellType.WALL;           // Левая граница
            grid[y][width - 1] = CellType.WALL;   // Правая граница
        }
    }

    private void generateInnerWalls() {
        // Генерируем стены только внутри границы (не на самой границе)
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (random.nextDouble() < 0.3) { // 30% вероятность стены
                    grid[y][x] = CellType.WALL;
                }
            }
        }
    }

    private void createPath() {
        // Упрощенный алгоритм создания проходимого лабиринта
        // Проходим по диагонали от старта к финишу, убирая стены
        int currentX = 1;
        int currentY = 1;

        while (currentX < width - 2 || currentY < height - 2) {
            // Делаем текущую клетку проходимой
            grid[currentY][currentX] = CellType.EMPTY;

            // Двигаемся к финишу
            if (currentX < width - 2 && random.nextBoolean()) {
                currentX++;
            } else if (currentY < height - 2) {
                currentY++;
            }

            // Делаем соседние клетки тоже проходимыми с некоторой вероятностью
            if (currentX > 1 && currentX < width - 1)
                grid[currentY][currentX - 1] = CellType.EMPTY;
            if (currentY > 1 && currentY < height - 1)
                grid[currentY - 1][currentX] = CellType.EMPTY;
        }

        // Финишная клетка
        grid[height - 2][width - 2] = CellType.EMPTY;
    }

    private void ensureStartAndFinishArePassable() {
        // Гарантируем, что стартовая позиция проходима
        grid[1][1] = CellType.EMPTY;

        // Гарантируем, что финишная позиция проходима
        grid[height - 2][width - 2] = CellType.EMPTY;

        // Создаем безопасную зону вокруг старта (3x3 клетки внутри границы)
        for (int y = 1; y <= 3; y++) {
            for (int x = 1; x <= 3; x++) {
                if (y < height - 1 && x < width - 1) {
                    grid[y][x] = CellType.EMPTY;
                }
            }
        }

        // Создаем безопасную зону вокруг финиша (3x3 клетки внутри границы)
        for (int y = height - 3; y <= height - 2; y++) {
            for (int x = width - 3; x <= width - 2; x++) {
                if (y >= 1 && x >= 1) {
                    grid[y][x] = CellType.EMPTY;
                }
            }
        }
    }

    private void placeRobotAndTreasure() {
        // Размещаем робота в безопасной зоне (клетка 1,1)
        robotX = 1;
        robotY = 1;

        // Размещаем клад в правом нижнем углу (внутри границы)
        treasureX = width - 2;
        treasureY = height - 2;
        grid[treasureY][treasureX] = CellType.TREASURE;
    }

    // Геттеры
    public CellType[][] getGrid() { return grid; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getRobotX() { return robotX; }
    public int getRobotY() { return robotY; }
    public int getTreasureX() { return treasureX; }
    public int getTreasureY() { return treasureY; }

    // Проверка, является ли клетка проходимой
    public boolean isPassable(int x, int y) {
        // Сначала проверяем, что координаты в пределах массива
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        // Затем проверяем, что это не стена
        return grid[y][x] != CellType.WALL;
    }

    // Проверка достижения клада
    public boolean isTreasureReached(int x, int y) {
        return x == treasureX && y == treasureY;
    }

    // Метод для проверки, является ли клетка границей
    public boolean isBorder(int x, int y) {
        return x == 0 || y == 0 || x == width - 1 || y == height - 1;
    }
}