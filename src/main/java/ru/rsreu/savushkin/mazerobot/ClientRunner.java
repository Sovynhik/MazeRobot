package ru.rsreu.savushkin.mazerobot;

import ru.rsreu.savushkin.mazerobot.core.controller.MazeController;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.model.RobotModel;
import ru.rsreu.savushkin.mazerobot.ui.view.MazeView;

import javax.swing.*;

/**
 * Maze Robot Navigation System
 *
 * author: Savushkin D.A.
 * version: 1.0
 *
 * Приложение моделирует движение робота по лабиринту с использованием различных
 * алгоритмов поиска пути. Робот может перемещаться пошагово или выполнять двойные ходы.
 * Поддерживаются алгоритмы BFS и DFS с возможностью двойных перемещений.
 */
public class ClientRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MazeModel mazeModel = new MazeModel(15, 15);
            RobotModel robotModel = new RobotModel(mazeModel);
            PathFindingManager pathFindingManager = new PathFindingManager();
            MazeView view = new MazeView(mazeModel, robotModel, pathFindingManager);
            new MazeController(robotModel, view, pathFindingManager);
            view.setVisible(true);
        });
    }
}