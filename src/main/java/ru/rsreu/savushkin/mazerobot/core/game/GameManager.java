package ru.rsreu.savushkin.mazerobot.core.game;

import ru.rsreu.savushkin.mazerobot.algorithm.pathfinding.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.entity.Maze;
import ru.rsreu.savushkin.mazerobot.core.entity.Robot;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;

import java.util.List;

public class GameManager {
    // Константы для размера лабиринта
    private final int MAZE_WIDTH = 15;
    private final int MAZE_HEIGHT = 15;

    private Maze maze;
    private Robot robot;
    private Point goal;

    // Менеджер алгоритмов, используемый для поиска пути
    private final PathFindingManager pathManager;

    public GameManager(PathFindingManager pathManager) {
        this.pathManager = pathManager;
        // При старте приложения создаем лабиринт один раз
        createMaze();
    }

    /**
     * Создает новый лабиринт и устанавливает робота и цель.
     */
    public void createMaze() {
        this.maze = new Maze(MAZE_WIDTH, MAZE_HEIGHT);

        // Используем координаты старта и цели, которые Maze сгенерировал внутри себя
        // Предполагаем, что геттеры Maze выглядят так:
        int startX = maze.getRobotX();
        int startY = maze.getRobotY();
        int goalX = maze.getTreasureX();
        int goalY = maze.getTreasureY();

        // Инициализация сущностей
        this.robot = new Robot(startX, startY, this.maze);
        this.goal = new Point(goalX, goalY);

        // Гарантируем проходимость старта и цели (если генератор мог закрыть)
        maze.setCellPassable(robot.getX(), robot.getY(), true);
        maze.setCellPassable(goal.getX(), goal.getY(), true);
    }

    /**
     * Сбрасывает робота на стартовую позицию, сохраняя текущий лабиринт.
     * Вызывается кнопкой "Начать игру".
     */
    public void resetGame() {
        if (robot != null) {
            robot.setX(maze.getRobotX());
            robot.setY(maze.getRobotY());
        }
        // Также сбросьте путь, если он хранится в GameManager
    }

    /**
     * Запускает выбранный алгоритм поиска пути.
     */
    public List<Point> findPath() {
        return pathManager.findPath(
                maze,
                robot.getX(), robot.getY(),
                goal.getX(), goal.getY()
        );
    }

    // --- Геттеры для UI-классов (View) ---
    public Maze getMaze() {
        return maze;
    }

    public Robot getRobot() {
        return robot;
    }

    public Point getGoal() {
        return goal;
    }

    public PathFindingManager getPathManager() {
        return pathManager;
    }
}