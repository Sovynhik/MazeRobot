package ru.rsreu.savushkin.mazerobot.core.ui;

import ru.rsreu.savushkin.mazerobot.algorithm.dfs.DepthFirstSearch;
import ru.rsreu.savushkin.mazerobot.algorithm.pathfinding.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.entity.Maze;
import ru.rsreu.savushkin.mazerobot.core.game.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeGame extends JFrame {
    private Maze maze;
    private ru.rsreu.savushkin.mazerobot.core.game.Robot robot;
    private MazePanel mazePanel;
    private JButton startButton;
    private JButton findPathButton;
    private JComboBox<String> algorithmComboBox;
    private PathFindingManager pathManager;

    public MazeGame() {
        initializeGame();
        setupUI();
    }

    private void initializeGame() {
        maze = new Maze(15, 15);
        robot = new Robot(maze.getRobotX(), maze.getRobotY(), maze);
        pathManager = new PathFindingManager();
    }

    private void setupUI() {
        setTitle("Робот в лабиринте - Интеллектуальные системы");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель лабиринта
        mazePanel = new MazePanel(maze, robot, pathManager);
        add(mazePanel, BorderLayout.CENTER);

        // Панель управления
        JPanel controlPanel = new JPanel(new FlowLayout());

        startButton = new JButton("Начать игру");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                findPathButton.setEnabled(true);
                algorithmComboBox.setEnabled(true);
                mazePanel.startGame();
            }
        });

        findPathButton = new JButton("Найти путь");
        findPathButton.setEnabled(false);
        findPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mazePanel.findAndShowPath();
            }
        });

        // Выбор алгоритма
        String[] algorithms = {"Поиск в глубину (DFS)"}; // Пока только DFS
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.setEnabled(false);
        algorithmComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchAlgorithm((String) algorithmComboBox.getSelectedItem());
            }
        });

        controlPanel.add(startButton);
        controlPanel.add(findPathButton);
        controlPanel.add(new JLabel("Алгоритм:"));
        controlPanel.add(algorithmComboBox);

        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void switchAlgorithm(String algorithmName) {
        switch (algorithmName) {
            case "Поиск в глубину (DFS)":
                pathManager.setAlgorithm(new DepthFirstSearch());
                break;
            // Можно добавить другие алгоритмы позже
        }
        System.out.println("Алгоритм изменен на: " + algorithmName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MazeGame().setVisible(true);
            }
        });
    }
}