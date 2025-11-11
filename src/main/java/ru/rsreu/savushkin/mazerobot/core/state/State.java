package ru.rsreu.savushkin.mazerobot.core.state;

/**
 * Интерфейс состояния системы
 *
 * Любое состояние должно уметь:
 * - быть сравнимым (equals)
 * - предоставлять строковое представление
 * - быть хэшируемым (для HashSet/HashMap)
 */
public interface State extends Comparable<State> {
    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}