package ru.rsreu.savushkin.mazerobot.ui.view;

import ru.rsreu.savushkin.mazerobot.core.controller.MazeController;
import ru.rsreu.savushkin.mazerobot.core.entity.Event;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.model.Listener;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.model.RobotModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.List;

public class MazeView extends JFrame implements Listener {
    private final MazePanel panel;
    private final JButton startButton = new JButton("Начать игру");
    private final JButton findPathButton = new JButton("Найти путь");
    private final JComboBox<String> algorithmComboBox;
    private MazeController controller;
    private final PathFindingManager pathMgr;

    public MazeView(MazeModel maze, RobotModel robot, PathFindingManager pathMgr) {
        this.pathMgr = pathMgr;
        panel = new MazePanel(maze, robot);
        setTitle("Робот в лабиринте - Строгий MVC с расширяемыми алгоритмами");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        algorithmComboBox = new JComboBox<>(pathMgr.getAvailableAlgorithms().toArray(new String[0]));
        JPanel controlPanel = new JPanel(new FlowLayout());
        startButton.addActionListener(e -> controller.startGame());
        findPathButton.addActionListener(e -> controller.findPath());
        algorithmComboBox.addActionListener(e -> controller.changeAlgorithm((String) algorithmComboBox.getSelectedItem()));
        findPathButton.setEnabled(false);
        algorithmComboBox.setEnabled(false);

        controlPanel.add(startButton);
        controlPanel.add(findPathButton);
        controlPanel.add(new JLabel("Алгоритм:"));
        controlPanel.add(algorithmComboBox);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public void setController(MazeController controller) { this.controller = controller; }

    public void enableGameControls(boolean enable) {
        startButton.setEnabled(!enable);
        findPathButton.setEnabled(enable);
        algorithmComboBox.setEnabled(enable);
        panel.requestFocusInWindow();
    }

    public void showPath(List<Point> path) {
        panel.updatePath(path);
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Путь не найден!", "Результат поиска", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Найден путь длиной: " + path.size() + " шагов\nАлгоритм: " + pathMgr.getAlgorithm().getName(),
                    "Путь найден", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void showVictory() {
        JOptionPane.showMessageDialog(this, "Поздравляем! Робот нашел клад!", "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
    }

    public void addKeyListener(KeyListener listener) { panel.addKeyListener(listener); }

    @Override
    public void handle(Event event) {
        panel.repaint();
    }
}