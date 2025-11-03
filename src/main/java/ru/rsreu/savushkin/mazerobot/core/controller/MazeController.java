package ru.rsreu.savushkin.mazerobot.core.controller;

import ru.rsreu.savushkin.mazerobot.core.model.*;
import ru.rsreu.savushkin.mazerobot.ui.view.MazeView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeController {
    private final RobotModel model;
    private final MazeView view;
    private final PathFindingManager pathMgr;
    private boolean gameRunning = false;

    public MazeController(RobotModel model, MazeView view, PathFindingManager pathMgr) {
        this.model = model;
        this.view = view;
        this.pathMgr = pathMgr;
        view.setController(this);
        model.addListener(view);
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameRunning) return;
                boolean moved = switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> model.move(0, -1);
                    case KeyEvent.VK_DOWN -> model.move(0, 1);
                    case KeyEvent.VK_LEFT -> model.move(-1, 0);
                    case KeyEvent.VK_RIGHT -> model.move(1, 0);
                    case KeyEvent.VK_W -> e.isShiftDown() && model.moveDouble(0, -1);
                    case KeyEvent.VK_S -> e.isShiftDown() && model.moveDouble(0, 1);
                    case KeyEvent.VK_A -> e.isShiftDown() && model.moveDouble(-1, 0);
                    case KeyEvent.VK_D -> e.isShiftDown() && model.moveDouble(1, 0);
                    default -> false;
                };
                if (moved && model.isAtTreasure()) {
                    gameRunning = false;
                    view.showVictory();
                }
            }
        });
    }

    public void startGame() {
        gameRunning = true;
        view.enableGameControls(true);
    }

    public void findPath() {
        var pos = model.getPosition();
        var treas = model.getMaze().getTreasure();
        var path = pathMgr.findPath(model.getMaze(), pos.x(), pos.y(), treas.x(), treas.y());
        view.showPath(path);
    }

    public void changeAlgorithm(String name) {
        pathMgr.setAlgorithm(name);
    }
}