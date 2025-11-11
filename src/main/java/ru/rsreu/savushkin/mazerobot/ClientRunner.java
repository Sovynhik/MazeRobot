package ru.rsreu.savushkin.mazerobot;

import ru.rsreu.savushkin.mazerobot.core.controller.MazeController;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.RobotAgent;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeEnvironment;
import ru.rsreu.savushkin.mazerobot.ui.view.MazeView;

import javax.swing.*;

/**
 * Система навигации агента по лабиринту
 *
 * Автор: Савушкин
 * Версия: 1.0
 *
 * Универсальная архитектура поиска пути в пространстве состояний.
 */
public class ClientRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MazeModel mazeModel = new MazeModel(15, 15);
            var environment = new MazeEnvironment(mazeModel);
            var agent = new RobotAgent<>(environment);
            var pathMgr = new ru.rsreu.savushkin.mazerobot.core.algorithm.PathFindingManager();

            var view = new MazeView(mazeModel, agent, pathMgr);
            new MazeController(agent, view, pathMgr);
            view.setVisible(true);
        });
    }
}