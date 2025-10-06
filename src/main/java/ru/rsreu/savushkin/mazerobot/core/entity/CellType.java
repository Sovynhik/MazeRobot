package ru.rsreu.savushkin.mazerobot.core.entity;

public enum CellType {
    EMPTY,      // Пустая клетка
    WALL,       // Стена
    ROBOT,      // Робот
    TREASURE,   // Клад
    VISITED     // Посещенная клетка (для пути)
}
