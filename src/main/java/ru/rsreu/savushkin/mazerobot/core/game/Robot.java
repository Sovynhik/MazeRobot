package ru.rsreu.savushkin.mazerobot.core.game;

import ru.rsreu.savushkin.mazerobot.core.entity.Maze;

public class Robot {
    private int x, y;
    private Maze maze;

    public Robot(int startX, int startY, Maze maze) {
        this.x = startX;
        this.y = startY;
        this.maze = maze;
    }

    // Движение на одну клетку
    public boolean move(int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;

        if (maze.isPassable(newX, newY)) {
            x = newX;
            y = newY;
            return true;
        }
        return false;
    }

    // Движение на две клетки
    public boolean moveDouble(int dx, int dy) {
        int newX = x + dx * 2;
        int newY = y + dy * 2;

        // Проверяем, что промежуточная клетка тоже проходима
        if (maze.isPassable(x + dx, y + dy) && maze.isPassable(newX, newY)) {
            x = newX;
            y = newY;
            return true;
        }
        return false;
    }

    // Геттеры
    public int getX() { return x; }
    public int getY() { return y; }

    // Проверка, достиг ли робот клада
    public boolean hasReachedTreasure() {
        return maze.isTreasureReached(x, y);
    }
}