package ru.rsreu.savushkin.mazerobot.core.state.maze;

import ru.rsreu.savushkin.mazerobot.core.state.State;

/**
 * Состояние робота в лабиринте — просто координаты
 */
public record MazeState(int x, int y) implements State {
    @Override
    public int compareTo(State o) {
        if (o instanceof MazeState other) {
            int dx = Integer.compare(x, other.x);
            return dx != 0 ? dx : Integer.compare(y, other.y);
        }
        return 0;
    }
}