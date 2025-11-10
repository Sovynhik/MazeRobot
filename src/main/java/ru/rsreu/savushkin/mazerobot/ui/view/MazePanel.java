package ru.rsreu.savushkin.mazerobot.ui.view;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.RobotModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Панель отрисовки лабиринта
 *
 * Отвечает за визуальное представление лабиринта, робота, пути и сокровища.
 */
public class MazePanel extends JPanel {
    private final MazeModel maze;
    private final RobotModel robot;
    private List<Point> path = List.of();
    private static final int CELL_SIZE = 35;
    private static final Color WALL_COLOR = Color.DARK_GRAY;
    private static final Color EMPTY_COLOR = Color.WHITE;
    private static final Color ROBOT_COLOR = new Color(30, 144, 255);
    private static final Color TREASURE_COLOR = new Color(220, 20, 60);
    private static final Color BORDER_COLOR = new Color(255, 140, 0);
    private static final Color PATH_COLOR = new Color(0, 200, 0, 150);

    public MazePanel(MazeModel maze, RobotModel robot) {
        this.maze = maze;
        this.robot = robot;
        setPreferredSize(new Dimension(maze.getWidth() * CELL_SIZE, maze.getHeight() * CELL_SIZE));
        setFocusable(true);
        setBackground(Color.WHITE);
    }

    /**
     * Обновляет отображаемый путь
     *
     * @param path новый путь для отрисовки
     */
    public void updatePath(List<Point> path) {
        this.path = path;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Фон с лёгким градиентом
        g2d.setPaint(new GradientPaint(0, 0, Color.LIGHT_GRAY, getWidth(), getHeight(), Color.WHITE));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Отрисовка клеток
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                int px = x * CELL_SIZE;
                int py = y * CELL_SIZE;
                Color color;

                if (x == 0 || y == 0 || x == maze.getWidth() - 1 || y == maze.getHeight() - 1) {
                    color = BORDER_COLOR; // внешняя рамка
                } else {
                    CellType cell = maze.getCell(x, y);
                    color = switch (cell) {
                        case WALL -> WALL_COLOR;
                        case TREASURE -> TREASURE_COLOR;
                        default -> EMPTY_COLOR;
                    };
                }

                g2d.setColor(color);
                g2d.fillRect(px, py, CELL_SIZE, CELL_SIZE);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(px, py, CELL_SIZE, CELL_SIZE);
            }
        }

        // Отрисовка пути (зелёные круги)
        g2d.setColor(PATH_COLOR);
        for (Point p : path) {
            int cx = p.x() * CELL_SIZE + 6;
            int cy = p.y() * CELL_SIZE + 6;
            int size = CELL_SIZE - 12;
            g2d.fillOval(cx, cy, size, size);
        }

        // Отрисовка робота
        Point pos = robot.getPosition();
        g2d.setColor(ROBOT_COLOR);
        g2d.fillOval(pos.x() * CELL_SIZE + 5, pos.y() * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
    }
}