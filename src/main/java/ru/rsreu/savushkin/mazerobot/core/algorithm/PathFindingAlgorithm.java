package ru.rsreu.savushkin.mazerobot.core.algorithm;

import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;

import java.util.List;

public interface PathFindingAlgorithm {
    List<Point> findPath(MazeModel maze, int startX, int startY, int endX, int endY);
    String getName();
}