package ru.rsreu.savushkin.mazerobot.core.algorithm.dfs;

import ru.rsreu.savushkin.mazerobot.core.algorithm.PathFindingAlgorithm;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;

import java.util.*;

/**
 * Алгоритм поиска в глубину (DFS)
 *
 * Исследует лабиринт, углубляясь по каждому пути до конца, затем возвращается.
 * Использует только одиночные шаги.
 */
public class DepthFirstSearch implements PathFindingAlgorithm {

    /**
     * Находит путь от начала до конца с помощью DFS
     *
     * @param maze лабиринт
     * @param startX начальная X
     * @param startY начальная Y
     * @param endX конечная X
     * @param endY конечная Y
     * @return найденный путь
     */
    @Override
    public List<Point> findPath(MazeModel maze, int startX, int startY, int endX, int endY) {
        Set<Point> visited = new HashSet<>();
        Deque<Point> stack = new ArrayDeque<>();
        Map<Point, Point> parent = new HashMap<>();

        Point start = new Point(startX, startY);
        Point end = new Point(endX, endY);

        stack.push(start);
        visited.add(start);
        parent.put(start, null);

        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        while (!stack.isEmpty()) {
            Point current = stack.pop();
            if (current.equals(end)) {
                return reconstructPath(parent, current);
            }

            for (int[] dir : directions) {
                int nextX = current.x() + dir[0];
                int nextY = current.y() + dir[1];

                if (isWithinBounds(nextX, nextY, maze)) {
                    Point neighbor = new Point(nextX, nextY);
                    if (maze.isPassable(nextX, nextY) && !visited.contains(neighbor)) {
                        stack.push(neighbor);
                        visited.add(neighbor);
                        parent.put(neighbor, current);
                    }
                }
            }
        }
        return Collections.emptyList();
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
        return "Поиск в глубину (DFS)";
    }
}