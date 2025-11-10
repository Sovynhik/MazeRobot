package ru.rsreu.savushkin.mazerobot.core.algorithm;

import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;

import java.util.List;

/**
 * Интерфейс для алгоритмов поиска пути
 *
 * Определяет контракт для всех алгоритмов, находящих путь в лабиринте.
 */
public interface PathFindingAlgorithm {
    /**
     * Находит путь от начальной до конечной позиции в лабиринте
     *
     * @param maze лабиринт, в котором осуществляется поиск
     * @param startX начальная X-координата
     * @param startY начальная Y-координата
     * @param endX конечная X-координата
     * @param endY конечная Y-координата
     * @return список точек, образующих путь; пустой список — если путь не найден
     */
    List<Point> findPath(MazeModel maze, int startX, int startY, int endX, int endY);

    /**
     * Возвращает название алгоритма
     *
     * @return название алгоритма в виде строки
     */
    String getName();
}