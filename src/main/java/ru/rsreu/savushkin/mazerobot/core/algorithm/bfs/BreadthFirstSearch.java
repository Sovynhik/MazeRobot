package ru.rsreu.savushkin.mazerobot.core.algorithm.bfs;

import ru.rsreu.savushkin.mazerobot.core.algorithm.PathFindingAlgorithm;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;

import java.util.*;

/**
 * Алгоритм поиска в ширину (BFS)
 *
 * Исследует лабиринт по уровням, гарантируя нахождение кратчайшего пути.
 * Использует только одиночные шаги.
 */
public class BreadthFirstSearch implements PathFindingAlgorithm {

    /**
     * Находит кратчайший путь от начальной до конечной точки
     *
     * @param maze лабиринт
     * @param startX начальная X-координата
     * @param startY начальная Y-координата
     * @param endX конечная X-координата
     * @param endY конечная Y-координата
     * @return список точек кратчайшего пути
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

        // Направления: вверх, вправо, вниз, влево
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(end)) {
                return reconstructPath(parent, current);
            }

            for (int[] dir : directions) {
                int nextX = current.x() + dir[0];
                int nextY = current.y() + dir[1];

                if (isWithinBounds(nextX, nextY, maze) &&
                        !visited.contains(new Point(nextX, nextY)) &&
                        maze.isPassable(nextX, nextY)) {

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
     * Проверяет, находится ли точка в пределах лабиринта
     *
     * @param x X-координата
     * @param y Y-координата
     * @param maze лабиринт
     * @return true, если координаты в пределах границ
     */
    private boolean isWithinBounds(int x, int y, MazeModel maze) {
        return x >= 0 && x < maze.getWidth() && y >= 0 && y < maze.getHeight();
    }

    /**
     * Восстанавливает путь от конечной точки к начальной
     *
     * @param parent карта родительских связей
     * @param end конечная точка
     * @return список точек от начала до конца
     */
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
        return "Поиск в ширину (BFS)";
    }
}