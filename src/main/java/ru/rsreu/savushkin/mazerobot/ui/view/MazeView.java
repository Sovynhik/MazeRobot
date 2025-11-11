package ru.rsreu.savushkin.mazerobot.ui.view;

import ru.rsreu.savushkin.mazerobot.core.algorithm.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.controller.MazeController;
import ru.rsreu.savushkin.mazerobot.core.entity.Event;
import ru.rsreu.savushkin.mazerobot.core.model.Listener;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.RobotAgent;
import ru.rsreu.savushkin.mazerobot.core.state.State;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeState;

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
 * и показывает результаты. Работает с универсальными состояниями (State).
 */
public class MazeView extends JFrame implements Listener {
    private final MazePanel mazePanel;
    private final JButton startButton = new JButton("Начать игру");
    private final JButton findPathButton = new JButton("Найти путь");
    private final JComboBox<String> algorithmComboBox;
    private MazeController controller;
    private final PathFindingManager pathMgr;
    private final RobotAgent<MazeState> agent;

    /**
     * Создаёт окно с лабиринтом и панелью управления
     *
     * @param mazeModel модель лабиринта
     * @param agent агент (робот), работающий с состояниями
     * @param pathMgr менеджер алгоритмов поиска пути
     */
    public MazeView(MazeModel mazeModel, RobotAgent<MazeState> agent, PathFindingManager pathMgr) {
        this.agent = agent;
        this.pathMgr = pathMgr;
        this.mazePanel = new MazePanel(mazeModel, agent);

        setTitle("Робот в лабиринте");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Загрузка иконок приложения
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
        algorithmComboBox = new JComboBox<>(pathMgr.getAvailable().toArray(new String[0]));
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

    /** Устанавливает контроллер для обработки действий */
    public void setController(MazeController controller) {
        this.controller = controller;
    }

    /**
     * Включает или выключает элементы управления игрой
     *
     * @param enable true — включить, false — выключить
     */
    public void enableGameControls(boolean enable) {
        startButton.setEnabled(!enable);
        findPathButton.setEnabled(enable);
        algorithmComboBox.setEnabled(enable);
        mazePanel.requestFocusInWindow();
    }

    /**
     * Запускает анимацию найденного пути
     *
     * @param path список состояний (MazeState) от начала до цели
     */
    public void showPath(List<? extends State> path) {
        animatePath(path.stream()
                .map(state -> (MazeState) state)
                .toList());
    }

    /**
     * Анимация пути с задержкой между шагами
     *
     * @param path путь в виде списка MazeState
     */
    private void animatePath(List<MazeState> path) {
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
                            "Найден путь длиной: " + path.size() + " шагов\n" +
                                    "Алгоритм: " + pathMgr.getCurrentAlgorithmName(),
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

    /** Добавляет обработчик клавиш */
    public void addKeyListener(KeyListener listener) {
        mazePanel.addKeyListener(listener);
    }

    @Override
    public void handle(Event event) {
        mazePanel.repaint();
    }
}