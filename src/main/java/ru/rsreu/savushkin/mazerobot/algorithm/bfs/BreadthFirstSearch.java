package ru.rsreu.savushkin.mazerobot.algorithm.bfs; // или ru.rsreu.savushkin.mazerobot.algorithm

import ru.rsreu.savushkin.mazerobot.algorithm.pathfinding.PathFindingAlgorithm;
import ru.rsreu.savushkin.mazerobot.core.entity.Maze;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;

import java.util.*;

public class BreadthFirstSearch implements PathFindingAlgorithm {

    @Override
    public List<Point> findPath(Maze maze, int startX, int startY, int endX, int endY) {
        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new ArrayDeque<>(); // Используем Queue для BFS
        Map<Point, Point> parentMap = new HashMap<>();

        Point start = new Point(startX, startY);
        Point end = new Point(endX, endY);

        queue.offer(start); // Добавляем в очередь
        visited.add(start);
        parentMap.put(start, null);

        // Возможные направления движения: (dx, dy)
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}}; // Вверх, Вправо, Вниз, Влево
        // Возможные размеры шага: 1 и 2 клетки
        int[] steps = {1, 2};

        while (!queue.isEmpty()) {
            Point current = queue.poll(); // Извлекаем из очереди

            // Если достигли цели, восстанавливаем путь
            if (current.equals(end)) {
                return reconstructPath(parentMap, current);
            }

            // Проверяем всех соседей с учетом шага 1 и 2
            for (int[] dir : directions) {
                int dx = dir[0];
                int dy = dir[1];

                for (int step : steps) {
                    int newX = current.getX() + dx * step;
                    int newY = current.getY() + dy * step;

                    Point neighbor = new Point(newX, newY);

                    // 1. Проверяем границы лабиринта
                    if (newX >= 0 && newX < maze.getWidth() && newY >= 0 && newY < maze.getHeight()) {

                        // 2. Дополнительная проверка для шага в 2 клетки: промежуточная клетка должна быть проходима
                        boolean isMoveValid = true;
                        if (step == 2) {
                            int midX = current.getX() + dx;
                            int midY = current.getY() + dy;
                            // Промежуточная клетка не должна быть стеной
                            if (!maze.isPassable(midX, midY)) {
                                isMoveValid = false;
                            }
                        }

                        // 3. Если движение допустимо и конечная клетка проходима и не посещена
                        if (isMoveValid && maze.isPassable(newX, newY) && !visited.contains(neighbor)) {
                            // Для BFS добавляем в очередь
                            queue.offer(neighbor);
                            visited.add(neighbor);
                            parentMap.put(neighbor, current);
                        }
                    }
                }
            }
        }

        // Путь не найден
        return Collections.emptyList();
    }

    /**
     * Восстанавливает путь от конечной точки до начальной (та же логика, что и в DFS)
     */
    private List<Point> reconstructPath(Map<Point, Point> parentMap, Point end) {
        List<Point> path = new ArrayList<>();
        Point current = end;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);
        return path;
    }

    @Override
    public String toString() {
        return "Поиск в ширину (BFS) с двойным шагом";
    }
}