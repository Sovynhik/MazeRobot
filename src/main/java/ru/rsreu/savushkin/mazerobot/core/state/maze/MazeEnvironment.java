package ru.rsreu.savushkin.mazerobot.core.state.maze;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Окружение для задачи "робот в лабиринте"
 *
 * Определяет правила переходов между состояниями (MazeState) с помощью действий (MoveAction).
 * Поддерживает одиночные шаги и двойные прыжки с проверкой проходимости промежуточной клетки.
 */
public class MazeEnvironment implements Environment<MazeState, MoveAction> {

    private final MazeModel maze;
    private final MazeState start;
    private final MazeState goal;

    /**
     * Создаёт окружение на основе модели лабиринта
     *
     * @param maze модель лабиринта с сеткой и стенами
     */
    public MazeEnvironment(MazeModel maze) {
        this.maze = maze;
        this.start = new MazeState(1, 1);
        this.goal = new MazeState(maze.getWidth() - 2, maze.getHeight() - 2);
    }

    @Override
    public MazeState getInitialState() {
        return start;
    }

    @Override
    public MazeState getGoalState() {
        return goal;
    }

    /**
     * Проверяет, является ли состояние допустимым (в границах и не стена)
     *
     * @param state проверяемое состояние
     * @return true, если клетка проходима
     */
    @Override
    public boolean isValid(MazeState state) {
        int x = state.x(), y = state.y();
        return x >= 0 && x < maze.getWidth() && y >= 0 && y < maze.getHeight()
                && maze.getCell(x, y) != CellType.WALL;
    }

    /**
     * Возвращает все возможные действия из текущего состояния
     *
     * Включает 4 одиночных шага и 4 двойных прыжка.
     *
     * @param state текущее состояние
     * @return список из 8 возможных действий
     */
    @Override
    public List<MoveAction> getPossibleActions(MazeState state) {
        List<MoveAction> actions = new ArrayList<>();
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}}; // вверх, вправо, вниз, влево

        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];
            actions.add(new MoveAction(dx, dy, false));           // одиночный шаг
            actions.add(new MoveAction(dx * 2, dy * 2, true));    // двойной прыжок
        }
        return actions;
    }

    /**
     * Применяет действие к состоянию и возвращает новое состояние
     *
     * Для двойного шага проверяется проходимость промежуточной клетки.
     *
     * @param state текущее состояние
     * @param action действие (одиночный или двойной шаг)
     * @return новое состояние после применения действия
     */
    @Override
    public MazeState applyAction(MazeState state, MoveAction action) {
        int stepDx = action.stepDx();  // смещение до следующей клетки
        int stepDy = action.stepDy();

        int nextX = state.x() + stepDx;
        int nextY = state.y() + stepDy;

        // Для двойного шага: проверяем промежуточную клетку
        if (action.isDouble()) {
            int midX = state.x() + stepDx;
            int midY = state.y() + stepDy;
            if (!isValid(new MazeState(midX, midY))) {
                return state; // действие невозможно — возвращаем текущее состояние
            }
        }

        return new MazeState(nextX, nextY);
    }

    @Override
    public boolean isGoal(MazeState state) {
        return state.equals(goal);
    }
}