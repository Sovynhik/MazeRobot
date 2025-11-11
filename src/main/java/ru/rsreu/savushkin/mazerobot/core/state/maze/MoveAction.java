package ru.rsreu.savushkin.mazerobot.core.state.maze;

import ru.rsreu.savushkin.mazerobot.core.state.Action;

/**
 * Действие перемещения агента в лабиринте.
 *
 * Поддерживает одиночные шаги и двойные прыжки (через одну клетку).
 */
public record MoveAction(int dx, int dy, boolean isDouble) implements Action {

    /**
     * Возвращает человекочитаемое название действия.
     *
     * @return название действия, например: "Вверх", "Прыжок вправо"
     */
    @Override
    public String getName() {
        String direction = switch (dx) {
            case 0 -> switch (dy) {
                case -1 -> "Вверх";
                case 1  -> "Вниз";
                default -> "Неизвестно";
            };
            case 1  -> "Вправо";
            case -1 -> "Влево";
            default -> "Неизвестно";
        };

        return isDouble ? "Прыжок " + direction : direction;
    }

    /**
     * Проверяет, является ли действие одиночным шагом.
     *
     * @return true, если это обычный шаг
     */
    public boolean isSingle() {
        return !isDouble;
    }

    /**
     * Возвращает смещение по X для одиночного шага.
     * При двойном шаге возвращает половину смещения.
     *
     * @return смещение по X для следующей клетки
     */
    public int stepDx() {
        return isDouble ? dx / 2 : dx;
    }

    /**
     * Возвращает смещение по Y для одиночного шага.
     * При двойном шаге возвращает половину смещения.
     *
     * @return смещение по Y для следующей клетки
     */
    public int stepDy() {
        return isDouble ? dy / 2 : dy;
    }
}