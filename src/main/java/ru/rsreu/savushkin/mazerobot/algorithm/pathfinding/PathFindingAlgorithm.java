package ru.rsreu.savushkin.mazerobot.algorithm.pathfinding;

import ru.rsreu.savushkin.mazerobot.core.entity.Maze;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;

import java.util.List;

public interface PathFindingAlgorithm {
    /**
     * Находит путь от начальной точки до конечной в лабиринте
     * @param maze лабиринт
     * @param startX начальная координата X
     * @param startY начальная координата Y
     * @param endX конечная координата X
     * @param endY конечная координата Y
     * @return список точек пути или пустой список если путь не найден
     */
    List<Point> findPath(Maze maze, int startX, int startY, int endX, int endY);
}