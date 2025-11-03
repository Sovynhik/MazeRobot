package ru.rsreu.savushkin.mazerobot.ui;

import ru.rsreu.savushkin.mazerobot.core.controller.MazeController;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.model.RobotModel;
import ru.rsreu.savushkin.mazerobot.ui.view.MazeView;

import javax.swing.*;

public class MainApp {
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