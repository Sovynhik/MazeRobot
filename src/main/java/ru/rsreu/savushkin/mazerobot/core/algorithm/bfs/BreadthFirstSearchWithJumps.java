package ru.rsreu.savushkin.mazerobot.core.algorithm.bfs;

import ru.rsreu.savushkin.mazerobot.core.algorithm.PathFindingAlgorithm;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;

import java.util.*;

/**
 * BFS с поддержкой двойных ходов
 *
 * Расширяет стандартный BFS, позволяя выполнять как одиночные, так и двойные шаги.
 * Для двойного хода требуется проходимость промежуточной и конечной клетки.
 */
public class BreadthFirstSearchWithJumps implements PathFindingAlgorithm {

    /**
     * Находит кратчайший путь с возможностью двойных шагов
     *
     * @param maze лабиринт
     * @param startX начальная X
     * @param startY начальная Y
     * @param endX конечная X
     * @param endY конечная Y
     * @return путь в виде списка точек
     */
    @Override
    public List<Point> findPath(MazeModel maze, int startX, int startY, int endX, int endY) {
        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> parent = new HashMap<>();

        Point start = new Point(startX, startY);
        Point end = new Point(endX, endY);

        queue.add(start);
        visited.add(start);
        parent.put(start, null);

        int[][] singleSteps = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        int[][] doubleSteps = {{0, -2}, {2, 0}, {0, 2}, {-2, 0}};

        int[][] allDirections = new int[singleSteps.length + doubleSteps.length][];
        System.arraycopy(singleSteps, 0, allDirections, 0, singleSteps.length);
        System.arraycopy(doubleSteps, 0, allDirections, singleSteps.length, doubleSteps.length);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(end)) {
                return reconstructPath(parent, current);
            }

            for (int[] dir : allDirections) {
                int deltaX = dir[0];
                int deltaY = dir[1];
                int nextX = current.x() + deltaX;
                int nextY = current.y() + deltaY;

                if (isWithinBounds(nextX, nextY, maze) &&
                        !visited.contains(new Point(nextX, nextY)) &&
                        isPassableWithJump(maze, current.x(), current.y(), deltaX, deltaY)) {

                    Point neighbor = new Point(nextX, nextY);
                    queue.add(neighbor);
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * Проверяет проходимость хода (одиночного или двойного)
     *
     * @param maze лабиринт
     * @param x текущая X
     * @param y текущая Y
     * @param deltaX смещение по X
     * @param deltaY смещение по Y
     * @return true, если ход возможен
     */
    private boolean isPassableWithJump(MazeModel maze, int x, int y, int deltaX, int deltaY) {
        if (Math.abs(deltaX) <= 1 && Math.abs(deltaY) <= 1) {
            return maze.isPassable(x + deltaX, y + deltaY);
        } else {
            int midX = x + deltaX / 2;
            int midY = y + deltaY / 2;
            return maze.isPassable(midX, midY) && maze.isPassable(x + deltaX, y + deltaY);
        }
    }

    private boolean isWithinBounds(int x, int y, MazeModel maze) {
        return x >= 0 && x < maze.getWidth() && y >= 0 && y < maze.getHeight();
    }

    private List<Point> reconstructPath(Map<Point, Point> parent, Point end) {
        List<Point> path = new ArrayList<>();
        Point current = end;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    @Override
    public String getName() {
        return "BFS с двойными ходами";
    }
}