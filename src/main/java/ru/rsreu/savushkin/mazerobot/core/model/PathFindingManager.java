package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.algorithm.PathFindingAlgorithm;
import ru.rsreu.savushkin.mazerobot.core.algorithm.bfs.BreadthFirstSearch;
import ru.rsreu.savushkin.mazerobot.core.algorithm.dfs.DepthFirstSearch;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PathFindingManager {
    private PathFindingAlgorithm currentAlgorithm;
    private final Map<String, Class<? extends PathFindingAlgorithm>> algorithmRegistry = new HashMap<>();

    public PathFindingManager() {
        registerAlgorithm("Поиск в глубину (DFS)", DepthFirstSearch.class);
        registerAlgorithm("Поиск в ширину (BFS)", BreadthFirstSearch.class);
        try {
            currentAlgorithm = algorithmRegistry.values().iterator().next().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize default algorithm", e);
        }
    }

    public void registerAlgorithm(String name, Class<? extends PathFindingAlgorithm> algoClass) {
        algorithmRegistry.put(name, algoClass);
    }

    public Set<String> getAvailableAlgorithms() {
        return algorithmRegistry.keySet();
    }

    public void setAlgorithm(String name) {
        Class<? extends PathFindingAlgorithm> algoClass = algorithmRegistry.get(name);
        if (algoClass != null) {
            try {
                currentAlgorithm = algoClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException("Failed to instantiate algorithm: " + name, e);
            }
        }
    }

    public PathFindingAlgorithm getAlgorithm() { return currentAlgorithm; }

    public List<Point> findPath(MazeModel maze, int sx, int sy, int ex, int ey) {
        return currentAlgorithm.findPath(maze, sx, sy, ex, ey);
    }
}