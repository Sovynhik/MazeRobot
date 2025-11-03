package ru.rsreu.savushkin.mazerobot.core.algorithm.bfs;

import ru.rsreu.savushkin.mazerobot.core.algorithm.PathFindingAlgorithm;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;

import java.util.*;

public class BreadthFirstSearch implements PathFindingAlgorithm {
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

        int[][] dirs = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        while (!queue.isEmpty()) {
            Point cur = queue.poll();
            if (cur.equals(end)) return reconstructPath(parent, cur);

            for (int[] dir : dirs) {
                int nx = cur.x() + dir[0];
                int ny = cur.y() + dir[1];
                if (nx >= 0 && nx < maze.getWidth() && ny >= 0 && ny < maze.getHeight()) {
                    Point neighbor = new Point(nx, ny);
                    if (maze.isPassable(nx, ny) && !visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                        parent.put(neighbor, cur);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private List<Point> reconstructPath(Map<Point, Point> parent, Point end) {
        List<Point> path = new ArrayList<>();
        Point cur = end;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);
        return path;
    }

    @Override
    public String getName() { return "Поиск в ширину (BFS)"; }
}