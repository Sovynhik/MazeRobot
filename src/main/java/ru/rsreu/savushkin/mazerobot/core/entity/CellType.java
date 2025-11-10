package ru.rsreu.savushkin.mazerobot.core.entity;

/** Типы клеток лабиринта */
public enum CellType {
    EMPTY,      // пустая клетка
    WALL,       // стена
    ROBOT,      // позиция робота (внутреннее)
    TREASURE,   // сокровище
    VISITED     // посещённая клетка (для алгоритмов)
}