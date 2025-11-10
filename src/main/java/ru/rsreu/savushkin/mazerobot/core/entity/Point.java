package ru.rsreu.savushkin.mazerobot.core.entity;

/** Координаты точки в лабиринте */
public record Point(int x, int y) {
    @Override public String toString() { return "(" + x + ", " + y + ")"; }
}