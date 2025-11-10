package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.entity.Event;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель робота
 *
 * Управляет текущим положением робота, проверяет возможность перемещений
 * (одиночный и двойной шаг) и уведомляет подписчиков об изменении позиции.
 */
public class RobotModel {
    private Point currentPosition;
    private final MazeModel maze;
    private final List<Listener> listeners = new ArrayList<>();

    /**
     * Создаёт робота в начальной позиции лабиринта
     *
     * @param maze лабиринт, по которому перемещается робот
     */
    public RobotModel(MazeModel maze) {
        this.maze = maze;
        this.currentPosition = maze.getRobotStart();
    }

    /**
     * Перемещает робота на один шаг в указанном направлении
     *
     * @param deltaX смещение по X (+1 — вправо, -1 — влево)
     * @param deltaY смещение по Y (+1 — вниз, -1 — вверх)
     * @return true, если перемещение выполнено успешно
     */
    public boolean move(int deltaX, int deltaY) {
        int nextX = currentPosition.x() + deltaX;
        int nextY = currentPosition.y() + deltaY;

        if (maze.isPassable(nextX, nextY)) {
            currentPosition = new Point(nextX, nextY);
            notifyListeners();
            return true;
        }
        return false;
    }

    /**
     * Выполняет двойной шаг (прыжок) через одну клетку
     *
     * Требует, чтобы промежуточная и конечная клетки были проходимыми.
     *
     * @param deltaX смещение по X (направление)
     * @param deltaY смещение по Y (направление)
     * @return true, если двойной шаг выполнен
     */
    public boolean moveDouble(int deltaX, int deltaY) {
        int midX = currentPosition.x() + deltaX;
        int midY = currentPosition.y() + deltaY;
        int finalX = currentPosition.x() + deltaX * 2;
        int finalY = currentPosition.y() + deltaY * 2;

        if (maze.isPassable(midX, midY) && maze.isPassable(finalX, finalY)) {
            currentPosition = new Point(finalX, finalY);
            notifyListeners();
            return true;
        }
        return false;
    }

    /** @return текущая позиция робота */
    public Point getPosition() { return currentPosition; }

    /** @return true, если робот находится на клетке с сокровищем */
    public boolean isAtTreasure() { return currentPosition.equals(maze.getTreasure()); }

    /** Добавляет слушателя изменений состояния робота */
    public void addListener(Listener listener) { listeners.add(listener); }

    /** @return лабиринт, в котором находится робот */
    public MazeModel getMaze() { return maze; }

    /** Уведомляет всех слушателей о перемещении робота */
    private void notifyListeners() {
        Event event = new Event();
        for (Listener l : listeners) l.handle(event);
    }
}