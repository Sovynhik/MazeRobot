package ru.rsreu.savushkin.mazerobot.core.algorithm;

import ru.rsreu.savushkin.mazerobot.core.algorithm.bfs.BreadthFirstSearch;
import ru.rsreu.savushkin.mazerobot.core.algorithm.dfs.DepthFirstSearch;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.*;

public class PathFindingManager {
    private PathFindingAlgorithm current;
    private final Map<String, PathFindingAlgorithm> algorithms = new HashMap<>();

    public PathFindingManager() {
        algorithms.put("Поиск в ширину (BFS)", new BreadthFirstSearch());
        algorithms.put("Поиск в глубину (DFS)", new DepthFirstSearch());
        current = algorithms.values().iterator().next();
    }

    public String getCurrentAlgorithmName() {
        return current.getName();
    }

    public <S extends State> List<S> findPath(Environment<S, ?> env) {
        return current.findPath(env);
    }

    public void setAlgorithm(String name) {
        current = algorithms.get(name);
    }

    public Set<String> getAvailable() {
        return algorithms.keySet();
    }
}