package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.entity.Event;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;

import java.util.ArrayList;
import java.util.List;

public class RobotModel implements Model {
    private Point position;
    private final MazeModel maze;
    private final List<Listener> listeners = new ArrayList<>();

    public RobotModel(MazeModel maze) {
        this.maze = maze;
        this.position = maze.getRobotStart();
    }

    @Override
    public void moveBall() {}

    public boolean move(int dx, int dy) {
        int nx = position.x() + dx, ny = position.y() + dy;
        if (maze.isPassable(nx, ny)) {
            position = new Point(nx, ny);
            notifyListeners();
            return true;
        }
        return false;
    }

    public boolean moveDouble(int dx, int dy) {
        int mx = position.x() + dx, my = position.y() + dy;
        int nx = position.x() + dx * 2, ny = position.y() + dy * 2;
        if (maze.isPassable(mx, my) && maze.isPassable(nx, ny)) {
            position = new Point(nx, ny);
            notifyListeners();
            return true;
        }
        return false;
    }

    public Point getPosition() { return position; }
    public boolean isAtTreasure() { return position.equals(maze.getTreasure()); }
    public void addListener(Listener listener) { listeners.add(listener); }

    private void notifyListeners() {
        Event event = new Event();
        for (Listener l : listeners) l.handle(event);
    }

    public MazeModel getMaze() { return maze; }
}