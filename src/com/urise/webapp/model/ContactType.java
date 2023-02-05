package com.urise.webapp.model;

public enum ContactType {
    PHONE("Тел: "),
    SKYPE("Skype: "),
    MAIL("Почта: "),
    LINKEDIN("Профиль "),
    GITHUB("Профиль "),
    STACKOVERFLOW("Профиль "),
    HOMEPAGE("Домашняя страница ");

    private final String type;

    ContactType(String type) {
        this.type = type;
    }

    public String getType() {return type;}
}
