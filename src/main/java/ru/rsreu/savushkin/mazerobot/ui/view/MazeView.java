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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Главное окно приложения
 *
 * Отображает лабиринт, панель управления, анимирует найденный путь
 * и показывает сообщения о результатах.
 */
public class MazeView extends JFrame implements Listener {
    private final MazePanel mazePanel;
    private final JButton startButton = new JButton("Начать игру");
    private final JButton findPathButton = new JButton("Найти путь");
    private final JComboBox<String> algorithmComboBox;
    private MazeController controller;
    private final PathFindingManager pathMgr;
    private final RobotModel model;

    public MazeView(MazeModel maze, RobotModel robot, PathFindingManager pathMgr) {
        this.model = robot;
        this.pathMgr = pathMgr;
        this.mazePanel = new MazePanel(maze, robot);

        setTitle("Робот в лабиринте");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Установка иконок приложения
        try {
            List<Image> icons = List.of(
                    new ImageIcon(getClass().getResource("/icons/robot16.png")).getImage(),
                    new ImageIcon(getClass().getResource("/icons/robot32.png")).getImage(),
                    new ImageIcon(getClass().getResource("/icons/robot64.png")).getImage()
            );
            setIconImages(icons);
        } catch (Exception e) {
            System.err.println("Не удалось загрузить иконки: " + e.getMessage());
        }

        add(mazePanel, BorderLayout.CENTER);

        // Панель управления
        algorithmComboBox = new JComboBox<>(pathMgr.getAvailableAlgorithms().toArray(new String[0]));
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        startButton.addActionListener(e -> controller.startGame());
        findPathButton.addActionListener(e -> controller.findPath());
        algorithmComboBox.addActionListener(e ->
                controller.changeAlgorithm((String) algorithmComboBox.getSelectedItem()));

        findPathButton.setEnabled(false);
        algorithmComboBox.setEnabled(false);

        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(startButton);
        controlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        controlPanel.add(findPathButton);
        controlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        controlPanel.add(new JLabel("Алгоритм:"));
        controlPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        controlPanel.add(algorithmComboBox);
        controlPanel.add(Box.createHorizontalGlue());

        add(controlPanel, BorderLayout.SOUTH);

        // Подгонка окна под размер лабиринта
        pack();
        int panelWidth = mazePanel.getPreferredSize().width;
        controlPanel.setPreferredSize(new Dimension(panelWidth, controlPanel.getPreferredSize().height));
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void setController(MazeController controller) {
        this.controller = controller;
    }

    /** Включает/выключает элементы управления игрой */
    public void enableGameControls(boolean enable) {
        startButton.setEnabled(!enable);
        findPathButton.setEnabled(enable);
        algorithmComboBox.setEnabled(enable);
        mazePanel.requestFocusInWindow();
    }

    /** Запускает анимацию найденного пути */
    public void showPath(List<Point> path) {
        animatePath(path);
    }

    /** Анимация пути с задержкой */
    private void animatePath(List<Point> path) {
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Путь не найден!", "Результат поиска", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Timer timer = new Timer();
        final int[] index = {0};
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (index[0] < path.size()) {
                    mazePanel.updatePath(path.subList(0, index[0] + 1));
                    index[0]++;
                } else {
                    timer.cancel();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            MazeView.this,
                            "Найден путь длиной: " + path.size() + " шагов\nАлгоритм: " +
                                    pathMgr.getAlgorithm().getName(),
                            "Путь найден",
                            JOptionPane.INFORMATION_MESSAGE
                    ));
                }
            }
        }, 0, 200);
    }

    /** Показывает сообщение о победе */
    public void showVictory() {
        JOptionPane.showMessageDialog(
                this,
                "Поздравляем! Робот нашёл клад!",
                "Игра завершена",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void addKeyListener(KeyListener listener) {
        mazePanel.addKeyListener(listener);
    }

    @Override
    public void handle(Event event) {
        mazePanel.repaint();
    }
}