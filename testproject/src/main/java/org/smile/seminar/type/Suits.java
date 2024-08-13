package org.smile.seminar.type;

/**
 * Масть
 */
public enum Suits {

    CLUBS ("Трефы"),
    DIAMONDS ("Буби"),
    HEARTS ("Черви"),
    SPADES ("Пики");

    //Имя масти
    private String name;

    Suits(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
