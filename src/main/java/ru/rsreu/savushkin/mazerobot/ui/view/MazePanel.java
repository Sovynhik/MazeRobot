package ru.rsreu.savushkin.mazerobot.ui.view;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.RobotModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MazePanel extends JPanel {
    private final MazeModel maze;
    private final RobotModel robot;
    private List<Point> path = List.of();
    private static final int CELL_SIZE = 40;
    private static final Color WALL_COLOR = Color.DARK_GRAY, EMPTY_COLOR = Color.WHITE, ROBOT_COLOR = Color.BLUE,
            TREASURE_COLOR = Color.RED, BORDER_COLOR = Color.ORANGE, PATH_COLOR = new Color(0, 255, 0, 100);

    public MazePanel(MazeModel maze, RobotModel robot) {
        this.maze = maze;
        this.robot = robot;
        setPreferredSize(new Dimension(maze.getWidth() * CELL_SIZE, maze.getHeight() * CELL_SIZE));
        setFocusable(true);
    }

    public void updatePath(List<Point> path) { this.path = path; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                int px = x * CELL_SIZE, py = y * CELL_SIZE;
                Color color = (x == 0 || y == 0 || x == maze.getWidth() - 1 || y == maze.getHeight() - 1) ? BORDER_COLOR :
                        switch (maze.getCell(x, y)) {
                            case WALL -> WALL_COLOR;
                            case TREASURE -> TREASURE_COLOR;
                            default -> EMPTY_COLOR;
                        };
                g.setColor(color);
                g.fillRect(px, py, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(px, py, CELL_SIZE, CELL_SIZE);
            }
        }
        g.setColor(PATH_COLOR);
        for (Point p : path) g.fillRect(p.x() * CELL_SIZE + 5, p.y() * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        Point pos = robot.getPosition();
        g.setColor(ROBOT_COLOR);
        g.fillOval(pos.x() * CELL_SIZE + 5, pos.y() * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
    }
}