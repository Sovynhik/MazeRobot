package ru.rsreu.savushkin.mazerobot.algorithm.pathfinding;

import ru.rsreu.savushkin.mazerobot.algorithm.dfs.DepthFirstSearch;
import ru.rsreu.savushkin.mazerobot.core.entity.Maze;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;

import java.util.List;

public class PathFindingManager {
    private PathFindingAlgorithm currentAlgorithm;

    public PathFindingManager() {
        // Алгоритм по умолчанию
        this.currentAlgorithm = new DepthFirstSearch();
    }

    public void setAlgorithm(PathFindingAlgorithm algorithm) {
        this.currentAlgorithm = algorithm;
    }

    public PathFindingAlgorithm getCurrentAlgorithm() {
        return currentAlgorithm;
    }

    public List<Point> findPath(Maze maze, int startX, int startY, int endX, int endY) {
        return currentAlgorithm.findPath(maze, startX, startY, endX, endY);
    }
}