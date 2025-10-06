package ru.rsreu.savushkin.mazerobot.algorithm.dfs;

import ru.rsreu.savushkin.mazerobot.algorithm.pathfinding.PathFindingAlgorithm;
import ru.rsreu.savushkin.mazerobot.core.entity.Maze;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;

import java.util.*;

public class DepthFirstSearch implements PathFindingAlgorithm {

    @Override
    public List<Point> findPath(Maze maze, int startX, int startY, int endX, int endY) {
        // Множество посещенных точек
        Set<Point> visited = new HashSet<>();
        // Стек для DFS
        Deque<Point> stack = new ArrayDeque<>();
        // Карта для восстановления пути (ребенок -> родитель)
        Map<Point, Point> parentMap = new HashMap<>();

        Point start = new Point(startX, startY);
        Point end = new Point(endX, endY);

        stack.push(start);
        visited.add(start);
        parentMap.put(start, null);

        // Возможные направления движения (вверх, вправо, вниз, влево)
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        while (!stack.isEmpty()) {
            Point current = stack.pop();

            // Если достигли цели, восстанавливаем путь
            if (current.equals(end)) {
                return reconstructPath(parentMap, current);
            }

            // Проверяем всех соседей
            for (int[] dir : directions) {
                int newX = current.getX() + dir[0];
                int newY = current.getY() + dir[1];

                // Проверяем, что новые координаты в пределах лабиринта
                if (newX >= 0 && newX < maze.getWidth() && newY >= 0 && newY < maze.getHeight()) {
                    Point neighbor = new Point(newX, newY);

                    // Если соседняя клетка проходима и не посещена
                    if (maze.isPassable(newX, newY) && !visited.contains(neighbor)) {
                        stack.push(neighbor);
                        visited.add(neighbor);
                        parentMap.put(neighbor, current);
                    }
                }
            }
        }

        // Путь не найден
        return Collections.emptyList();
    }

    /**
     * Восстанавливает путь от конечной точки до начальной
     */
    private List<Point> reconstructPath(Map<Point, Point> parentMap, Point end) {
        List<Point> path = new ArrayList<>();
        Point current = end;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        // Переворачиваем, чтобы путь был от начала до конца
        Collections.reverse(path);
        return path;
    }

    @Override
    public String toString() {
        return "Поиск в глубину (DFS)";
    }
}