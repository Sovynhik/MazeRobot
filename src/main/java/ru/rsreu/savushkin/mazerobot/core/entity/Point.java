package ru.rsreu.savushkin.mazerobot.core.entity;

public record Point(int x, int y) {
    @Override public String toString() { return "(" + x + ", " + y + ")"; }
}