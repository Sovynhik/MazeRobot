package ru.rsreu.savushkin.mazerobot.core.controller;

import ru.rsreu.savushkin.mazerobot.algorithm.dfs.DepthFirstSearch;
import ru.rsreu.savushkin.mazerobot.algorithm.pathfinding.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.entity.Point;
import ru.rsreu.savushkin.mazerobot.core.game.GameManager;
import ru.rsreu.savushkin.mazerobot.core.ui.MazePanel;
import ru.rsreu.savushkin.mazerobot.algorithm.bfs.BreadthFirstSearch;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MazeGame extends JFrame {

    private GameManager gameManager;
    private MazePanel mazePanel;
    private PathFindingManager pathManager;

    private JButton startButton;
    private JButton findPathButton;
    private JComboBox<String> algorithmComboBox;

    public MazeGame() {
        super("Робот в Лабиринте - Интеллектуальные системы");

        // --- 1. Инициализация логики ---
        this.pathManager = new PathFindingManager();
        this.pathManager.setAlgorithm(new DepthFirstSearch()); // Устанавливаем алгоритм по умолчанию
        this.gameManager = new GameManager(pathManager); // GameManager теперь управляет Maze и Robot

        // --- 2. Настройка UI ---
        setupUI();

        setVisible(true);
    }

    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // MazePanel теперь принимает GameManager
        this.mazePanel = new MazePanel(gameManager);
        add(mazePanel, BorderLayout.CENTER);

        // Панель управления
        JPanel controlPanel = new JPanel(new FlowLayout());

        startButton = new JButton("Начать игру");
        // Логика кнопки "Начать игру" (Controller)
        startButton.addActionListener(e -> {
            // Вызываем сброс состояния в Model
            gameManager.resetGame();

            // Сброс пути и перерисовка View
            mazePanel.setCurrentPath(null);
            mazePanel.repaint();

            // Обновление состояния кнопок
            startButton.setEnabled(false);
            findPathButton.setEnabled(true);
            algorithmComboBox.setEnabled(true);
        });

        findPathButton = new JButton("Найти путь");
        findPathButton.setEnabled(false);
        // Логика кнопки "Найти путь" (Controller)
        findPathButton.addActionListener(e -> {
            // Вызываем поиск пути в Model
            List<Point> path = gameManager.findPath();

            // Обновляем View
            mazePanel.setCurrentPath(path);
            mazePanel.repaint();
        });

        // Выбор алгоритма
        String[] algorithms = {"Поиск в глубину (DFS)", "Поиск в ширину (BFS)"}; // <-- ДОБАВЛЕНО
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.setEnabled(false);
        algorithmComboBox.addActionListener(e -> {
            switchAlgorithm((String) algorithmComboBox.getSelectedItem());
        });

        controlPanel.add(startButton);
        controlPanel.add(findPathButton);
        controlPanel.add(new JLabel("Алгоритм:"));
        controlPanel.add(algorithmComboBox);

        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        // По умолчанию игра готова к старту
        startButton.setEnabled(true);
    }

    private void switchAlgorithm(String algorithmName) {
        // Получаем PathFindingManager через GameManager
        PathFindingManager pathManager = gameManager.getPathManager();

        switch (algorithmName) {
            case "Поиск в глубину (DFS)":
                pathManager.setAlgorithm(new DepthFirstSearch());
                break;
            case "Поиск в ширину (BFS)": // <-- ДОБАВЛЕНО
                pathManager.setAlgorithm(new BreadthFirstSearch());
                break;
            default:
                // Если выбран неизвестный алгоритм, ничего не делаем или логируем ошибку
                System.err.println("Неизвестный алгоритм: " + algorithmName);
        }
        System.out.println("Алгоритм изменен на: " + algorithmName);

        // Очищаем путь, чтобы избежать путаницы при переключении
        mazePanel.setCurrentPath(null);
        mazePanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MazeGame());
    }
}