package ru.rsreu.savushkin.mazerobot.core.state;

/**
 * Действие, переводящее из одного состояния в другое
 */
public interface Action {
    String getName(); // Например: "Вверх", "Прыжок вправо"
}