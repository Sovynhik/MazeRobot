package ru.rsreu.savushkin.mazerobot.core.ui;

import ru.rsreu.savushkin.mazerobot.algorithm.pathfinding.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
import ru.rsreu.savushkin.mazerobot.core.entity.Maze;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.game.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class MazePanel extends JPanel {
    private Maze maze;
    private ru.rsreu.savushkin.mazerobot.core.game.Robot robot;
    private PathFindingManager pathManager;
    private boolean gameStarted;
    private boolean gameFinished;
    private List<ru.rsreu.savushkin.mazerobot.core.entity.Point> currentPath;

    private static final int CELL_SIZE = 40;
    private static final Color WALL_COLOR = Color.DARK_GRAY;
    private static final Color EMPTY_COLOR = Color.WHITE;
    private static final Color ROBOT_COLOR = Color.BLUE;
    private static final Color TREASURE_COLOR = Color.RED;
    private static final Color BORDER_COLOR = Color.ORANGE;
    private static final Color PATH_COLOR = new Color(0, 255, 0, 100);

    public MazePanel(Maze maze, Robot robot, PathFindingManager pathManager) {
        this.maze = maze;
        this.robot = robot;
        this.pathManager = pathManager;
        this.gameStarted = false;
        this.gameFinished = false;
        this.currentPath = null;

        setPreferredSize(new Dimension(maze.getWidth() * CELL_SIZE,
                maze.getHeight() * CELL_SIZE));
        setBackground(Color.WHITE);
        setupKeyListener();
    }

    private void setupKeyListener() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameStarted || gameFinished) return;

                boolean moved = false;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        moved = robot.move(0, -1);
                        break;
                    case KeyEvent.VK_DOWN:
                        moved = robot.move(0, 1);
                        break;
                    case KeyEvent.VK_LEFT:
                        moved = robot.move(-1, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        moved = robot.move(1, 0);
                        break;
                    case KeyEvent.VK_W: // Двойной ход вверх
                        if (e.isShiftDown()) moved = robot.moveDouble(0, -1);
                        break;
                    case KeyEvent.VK_S: // Двойной ход вниз
                        if (e.isShiftDown()) moved = robot.moveDouble(0, 1);
                        break;
                    case KeyEvent.VK_A: // Двойной ход влево
                        if (e.isShiftDown()) moved = robot.moveDouble(-1, 0);
                        break;
                    case KeyEvent.VK_D: // Двойной ход вправо
                        if (e.isShiftDown()) moved = robot.moveDouble(1, 0);
                        break;
                }

                if (moved) {
                    checkGameEnd();
                    repaint();
                }
            }
        });
    }

    private void checkGameEnd() {
        if (robot.hasReachedTreasure()) {
            gameFinished = true;
            JOptionPane.showMessageDialog(this,
                    "Поздравляем! Робот нашел клад!",
                    "Игра завершена",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void startGame() {
        gameStarted = true;
        requestFocusInWindow();
    }

    public void findAndShowPath() {
        List<ru.rsreu.savushkin.mazerobot.core.entity.Point> path = pathManager.findPath(
                maze,
                robot.getX(),
                robot.getY(),
                maze.getTreasureX(),
                maze.getTreasureY()
        );

        this.currentPath = path;

        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Путь не найден!",
                    "Результат поиска",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Найден путь длиной: " + path.size() + " шагов\n" +
                            "Алгоритм: " + pathManager.getCurrentAlgorithm(),
                    "Путь найден",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMaze(g);
        drawPath(g);
        drawStatus(g);
    }

    private void drawMaze(Graphics g) {
        CellType[][] grid = maze.getGrid();

        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                int cellX = x * CELL_SIZE;
                int cellY = y * CELL_SIZE;

                // Определяем цвет клетки
                Color color;
                if (maze.isBorder(x, y)) {
                    // Граница - оранжевый цвет
                    color = BORDER_COLOR;
                } else {
                    switch (grid[y][x]) {
                        case WALL:
                            color = WALL_COLOR;
                            break;
                        case TREASURE:
                            color = TREASURE_COLOR;
                            break;
                        case ROBOT:
                            color = ROBOT_COLOR;
                            break;
                        default:
                            color = EMPTY_COLOR;
                    }
                }

                // Рисуем клетку
                g.setColor(color);
                g.fillRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
            }
        }

        // Рисуем робота поверх клетки
        int robotX = robot.getX() * CELL_SIZE;
        int robotY = robot.getY() * CELL_SIZE;
        g.setColor(ROBOT_COLOR);
        g.fillOval(robotX + 5, robotY + 5, CELL_SIZE - 10, CELL_SIZE - 10);
    }

    private void drawPath(Graphics g) {
        if (currentPath != null && !currentPath.isEmpty()) {
            g.setColor(PATH_COLOR);
            for (Point point : currentPath) {
                int x = point.getX() * CELL_SIZE;
                int y = point.getY() * CELL_SIZE;
                g.fillRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            }
        }
    }

    private void drawStatus(Graphics g) {
        g.setColor(Color.BLACK);
        if (!gameStarted) {
            g.drawString("Нажмите 'Начать игру' для старта", 10, 20);
        } else if (gameFinished) {
            g.drawString("Игра завершена! Робот нашел клад!", 10, 20);
        } else {
            g.drawString("Управление: стрелки - движение, Shift+WASD - двойной ход", 10, 20);
            g.drawString("Алгоритм: " + pathManager.getCurrentAlgorithm(), 10, 40);
        }
    }
}