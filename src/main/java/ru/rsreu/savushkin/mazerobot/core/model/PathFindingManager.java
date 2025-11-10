package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.algorithm.PathFindingAlgorithm;
import ru.rsreu.savushkin.mazerobot.core.algorithm.bfs.BreadthFirstSearch;
import ru.rsreu.savushkin.mazerobot.core.algorithm.bfs.BreadthFirstSearchWithJumps;
import ru.rsreu.savushkin.mazerobot.core.algorithm.dfs.DepthFirstSearch;
import ru.rsreu.savushkin.mazerobot.core.algorithm.dfs.DepthFirstSearchWithJumps;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Менеджер алгоритмов поиска пути
 *
 * Регистрирует доступные алгоритмы, позволяет переключаться между ними
 * и выполняет поиск пути через текущий активный алгоритм.
 */
public class PathFindingManager {
    private PathFindingAlgorithm currentAlgorithm;
    private final Map<String, Class<? extends PathFindingAlgorithm>> algorithmRegistry = new HashMap<>();

    /** Инициализирует менеджер с набором стандартных алгоритмов */
    public PathFindingManager() {
        registerDefaultAlgorithms();
        initializeDefaultAlgorithm();
    }

    /** Регистрирует стандартные алгоритмы поиска */
    private void registerDefaultAlgorithms() {
        registerAlgorithm("Поиск в глубину (DFS)", DepthFirstSearch.class);
        registerAlgorithm("Поиск в ширину (BFS)", BreadthFirstSearch.class);
        registerAlgorithm("DFS с двойными ходами", DepthFirstSearchWithJumps.class);
        registerAlgorithm("BFS с двойными ходами", BreadthFirstSearchWithJumps.class);
    }

    /** Устанавливает первый зарегистрированный алгоритм как текущий */
    private void initializeDefaultAlgorithm() {
        try {
            currentAlgorithm = algorithmRegistry.values().iterator().next()
                    .getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось инициализировать алгоритм по умолчанию", e);
        }
    }

    /**
     * Регистрирует новый алгоритм
     *
     * @param name отображаемое имя алгоритма
     * @param algorithmClass класс, реализующий PathFindingAlgorithm
     */
    public void registerAlgorithm(String name, Class<? extends PathFindingAlgorithm> algorithmClass) {
        algorithmRegistry.put(name, algorithmClass);
    }

    /** @return множество имён всех зарегистрированных алгоритмов */
    public Set<String> getAvailableAlgorithms() {
        return algorithmRegistry.keySet();
    }

    /**
     * Устанавливает текущий алгоритм по имени
     *
     * @param name имя алгоритма
     */
    public void setAlgorithm(String name) {
        Class<? extends PathFindingAlgorithm> algoClass = algorithmRegistry.get(name);
        if (algoClass != null) {
            try {
                currentAlgorithm = algoClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException |
                     NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException("Не удалось создать экземпляр алгоритма: " + name, e);
            }
        }
    }

    /** @return текущий активный алгоритм */
    public PathFindingAlgorithm getAlgorithm() { return currentAlgorithm; }

    /**
     * Выполняет поиск пути через текущий алгоритм
     *
     * @param maze лабиринт
     * @param startX начальная X
     * @param startY начальная Y
     * @param endX конечная X
     * @param endY конечная Y
     * @return найденный путь (список точек)
     */
    public List<Point> findPath(MazeModel maze, int startX, int startY, int endX, int endY) {
        return currentAlgorithm.findPath(maze, startX, startY, endX, endY);
    }
}