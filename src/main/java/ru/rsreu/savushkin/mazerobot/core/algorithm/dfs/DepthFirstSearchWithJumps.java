package ru.rsreu.savushkin.mazerobot.core.algorithm.dfs;

import ru.rsreu.savushkin.mazerobot.core.algorithm.PathFindingAlgorithm;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;

import java.util.*;

/**
 * DFS с поддержкой двойных ходов
 *
 * Расширяет DFS, позволяя выполнять как одиночные, так и двойные шаги.
 */
public class DepthFirstSearchWithJumps implements PathFindingAlgorithm {

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

        int[][] singleSteps = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        int[][] doubleSteps = {{0, -2}, {2, 0}, {0, 2}, {-2, 0}};

        int[][] allDirections = new int[singleSteps.length + doubleSteps.length][];
        System.arraycopy(singleSteps, 0, allDirections, 0, singleSteps.length);
        System.arraycopy(doubleSteps, 0, allDirections, singleSteps.length, doubleSteps.length);

        while (!stack.isEmpty()) {
            Point current = stack.pop();
            if (current.equals(end)) {
                return reconstructPath(parent, current);
            }

            for (int[] dir : allDirections) {
                int deltaX = dir[0];
                int deltaY = dir[1];
                int nextX = current.x() + deltaX;
                int nextY = current.y() + deltaY;

                if (isWithinBounds(nextX, nextY, maze)) {
                    Point neighbor = new Point(nextX, nextY);
                    if (!visited.contains(neighbor) && isPassableWithJump(maze, current.x(), current.y(), deltaX, deltaY)) {
                        stack.push(neighbor);
                        visited.add(neighbor);
                        parent.put(neighbor, current);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

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
        return "DFS с двойными ходами";
    }
}