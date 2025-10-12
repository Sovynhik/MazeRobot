package ru.rsreu.savushkin.mazerobot.core.ui;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
import ru.rsreu.savushkin.mazerobot.core.entity.Maze;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.entity.Robot;
import ru.rsreu.savushkin.mazerobot.core.game.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class MazePanel extends JPanel {

    private final GameManager gameManager;
    private List<Point> currentPath;

    private static final int CELL_SIZE = 40;
    private static final Color WALL_COLOR = Color.DARK_GRAY;
    private static final Color EMPTY_COLOR = Color.WHITE;
    private static final Color ROBOT_COLOR = Color.BLUE;
    private static final Color TREASURE_COLOR = Color.RED;
    private static final Color BORDER_COLOR = Color.ORANGE;
    private static final Color PATH_COLOR = new Color(0, 255, 0, 100);

    public MazePanel(GameManager gameManager) {
        this.gameManager = gameManager;

        // Устанавливаем предпочтительный размер, используя данные от GameManager
        Maze initialMaze = gameManager.getMaze();
        setPreferredSize(new Dimension(initialMaze.getWidth() * CELL_SIZE, initialMaze.getHeight() * CELL_SIZE));
        setBackground(Color.WHITE);

        setupKeyListener();
    }

    /**
     * Методы для работы с путем (вызывается из MazeGame)
     */
    public void setCurrentPath(List<Point> path) {
        this.currentPath = path;
    }

    private void setupKeyListener() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Получаем робота из GameManager
                Robot robot = gameManager.getRobot();

                boolean moved = false;

                // Перемещаем робота (логика движения остается в Robot.java)
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
                    case KeyEvent.VK_W:
                        if (e.isShiftDown()) moved = robot.moveDouble(0, -1);
                        break;
                    case KeyEvent.VK_S:
                        if (e.isShiftDown()) moved = robot.moveDouble(0, 1);
                        break;
                    case KeyEvent.VK_A:
                        if (e.isShiftDown()) moved = robot.moveDouble(-1, 0);
                        break;
                    case KeyEvent.VK_D:
                        if (e.isShiftDown()) moved = robot.moveDouble(1, 0);
                        break;
                }

                if (moved) {
                    // Проверяем окончание игры и перерисовываем
                    if (robot.hasReachedTreasure()) {
                        // Показываем сообщение о победе
                        JOptionPane.showMessageDialog(MazePanel.this,
                                "Поздравляем! Робот нашел клад!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
                    }
                    repaint();
                }
            }
        });
        requestFocusInWindow(); // Для немедленного захвата фокуса
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Получаем все данные из GameManager
        Maze maze = gameManager.getMaze();
        Robot robot = gameManager.getRobot();

        drawMaze(g, maze, robot);
        drawPath(g);
        drawStatus(g, robot.hasReachedTreasure()); // Передаем статус окончания
    }

    // Обновленный метод отрисовки лабиринта
    private void drawMaze(Graphics g, Maze maze, Robot robot) {
        CellType[][] grid = maze.getGrid();
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                int cellX = x * CELL_SIZE;
                int cellY = y * CELL_SIZE;

                Color color;
                if (maze.isBorder(x, y)) {
                    color = BORDER_COLOR;
                } else {
                    switch (grid[y][x]) {
                        case WALL:
                            color = WALL_COLOR;
                            break;
                        case TREASURE:
                            color = TREASURE_COLOR;
                            break;
                        default:
                            color = EMPTY_COLOR;
                            break;
                    }
                }

                g.setColor(color);
                g.fillRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
            }
        }

        // Рисуем робота поверх всего
        int robotX = robot.getX() * CELL_SIZE;
        int robotY = robot.getY() * CELL_SIZE;
        g.setColor(ROBOT_COLOR);
        g.fillOval(robotX + 5, robotY + 5, CELL_SIZE - 10, CELL_SIZE - 10);
    }

    // drawPath - остается без изменений (использует currentPath)
    private void drawPath(Graphics g) {
        if (currentPath != null && !currentPath.isEmpty()) {
            Graphics2D g2d = (Graphics2D) g.create();
            // ... (Ваш код drawPath с линиями и точками)
            g2d.setColor(PATH_COLOR);
            g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            final int HALF_CELL = CELL_SIZE / 2;
            final int DOT_SIZE = CELL_SIZE / 3;

            for (int i = 0; i < currentPath.size() - 1; i++) {
                Point p1 = currentPath.get(i);
                Point p2 = currentPath.get(i + 1);
                int x1 = p1.getX() * CELL_SIZE + HALF_CELL;
                int y1 = p1.getY() * CELL_SIZE + HALF_CELL;
                int x2 = p2.getX() * CELL_SIZE + HALF_CELL;
                int y2 = p2.getY() * CELL_SIZE + HALF_CELL;
                g2d.drawLine(x1, y1, x2, y2);
            }

            g2d.setColor(new Color(255, 100, 0));
            for (Point point : currentPath) {
                int x = point.getX() * CELL_SIZE;
                int y = point.getY() * CELL_SIZE;
                g2d.fillOval(x + HALF_CELL - DOT_SIZE / 2,
                        y + HALF_CELL - DOT_SIZE / 2,
                        DOT_SIZE, DOT_SIZE);
            }
            g2d.dispose();
        }
    }

    private void drawStatus(Graphics g, boolean gameFinished) {
        g.setColor(Color.BLACK);
        // Мы предполагаем, что игра всегда "начата" после запуска MazeGame
        if (gameFinished) {
            g.drawString("Игра завершена! Робот нашел клад!", 10, 20);
        } else {
            g.drawString("Управление: стрелки - движение, Shift+WASD - двойной ход", 10, 20);
            // Получаем текущий алгоритм из PathFindingManager через GameManager
            String currentAlgo = gameManager.getPathManager().getCurrentAlgorithm().toString();
            g.drawString("Алгоритм: " + currentAlgo, 10, 40);
        }
    }
}