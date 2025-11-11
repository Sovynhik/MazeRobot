package ru.rsreu.savushkin.mazerobot.core.state;

import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeState;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MoveAction;

import java.util.List;

/**
 * Окружение, в котором происходят переходы между состояниями
 */
public interface Environment<S extends ru.rsreu.savushkin.mazerobot.core.state.State, A extends Action> {
    /**
     * Возвращает начальное состояние
     */
    S getInitialState();

    /**
     * Возвращает целевое состояние
     */
    S getGoalState();

    /**
     * Проверяет, является ли состояние допустимым
     */
    boolean isValid(S state);

    /**
     * Возвращает все возможные действия из текущего состояния
     */
    List<A> getPossibleActions(S state);

    /**
     * Применяет действие к состоянию и возвращает новое состояние
     */
    S applyAction(S state, Action action);

    MazeState applyAction(MazeState state, MoveAction action);

    /**
     * Проверяет, является ли состояние целевым
     */
    boolean isGoal(S state);
}