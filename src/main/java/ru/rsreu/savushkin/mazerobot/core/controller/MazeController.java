package ru.rsreu.savushkin.mazerobot.core.controller;

import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.*;
import ru.rsreu.savushkin.mazerobot.ui.view.MazeView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Контроллер управления роботом и игрой
 *
 * Отвечает за обработку ввода, запуск поиска пути и управление состоянием игры.
 */
public class MazeController {
    private final RobotModel robotModel;
    private final MazeView mazeView;
    private final PathFindingManager pathFindingManager;
    private boolean isGameActive = false;

    /**
     * Создаёт контроллер
     *
     * @param robotModel модель робота
     * @param mazeView представление
     * @param pathFindingManager менеджер алгоритмов
     */
    public MazeController(RobotModel robotModel, MazeView mazeView, PathFindingManager pathFindingManager) {
        this.robotModel = robotModel;
        this.mazeView = mazeView;
        this.pathFindingManager = pathFindingManager;
        mazeView.setController(this);
        robotModel.addListener(mazeView);
        setupKeyboardControls();
    }

    /**
     * Настраивает обработку клавиш
     */
    private void setupKeyboardControls() {
        mazeView.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (!isGameActive) return;

                boolean moved = switch (event.getKeyCode()) {
                    case KeyEvent.VK_UP -> robotModel.move(0, -1);
                    case KeyEvent.VK_DOWN -> robotModel.move(0, 1);
                    case KeyEvent.VK_LEFT -> robotModel.move(-1, 0);
                    case KeyEvent.VK_RIGHT -> robotModel.move(1, 0);
                    case KeyEvent.VK_W -> event.isShiftDown() && robotModel.moveDouble(0, -1);
                    case KeyEvent.VK_S -> event.isShiftDown() && robotModel.moveDouble(0, 1);
                    case KeyEvent.VK_A -> event.isShiftDown() && robotModel.moveDouble(-1, 0);
                    case KeyEvent.VK_D -> event.isShiftDown() && robotModel.moveDouble(1, 0);
                    default -> false;
                };

                if (moved && robotModel.isAtTreasure()) {
                    isGameActive = false;
                    mazeView.showVictory();
                }
            }
        });
    }

    /** Запускает игру */
    public void startGame() {
        isGameActive = true;
        mazeView.enableGameControls(true);
    }

    /** Выполняет поиск пути */
    public void findPath() {
        Point pos = robotModel.getPosition();
        Point treasure = robotModel.getMaze().getTreasure();
        var path = pathFindingManager.findPath(robotModel.getMaze(), pos.x(), pos.y(), treasure.x(), treasure.y());
        mazeView.showPath(path);
    }

    /**
     * Меняет текущий алгоритм
     *
     * @param name название алгоритма
     */
    public void changeAlgorithm(String name) {
        pathFindingManager.setAlgorithm(name);
    }
}