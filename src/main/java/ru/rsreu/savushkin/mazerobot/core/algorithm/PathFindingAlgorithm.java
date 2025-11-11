package ru.rsreu.savushkin.mazerobot.core.algorithm;

import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.List;

/**
 * Универсальный интерфейс алгоритма поиска пути
 */
public interface PathFindingAlgorithm {
    <S extends State> List<S> findPath(Environment<S, ?> environment);
    String getName();
}