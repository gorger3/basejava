package com.urise.webapp.model;

public enum ContactType {
    PHONE("Тел: "),
    SKYPE("Skype: ") {
        @Override
        public String toHtml(String value) {
            return "<a href='skype:" + value + "'> " + value + "</a>";
        }
    },
    MAIL("Почта: ") {
        @Override
        public String toHtml0(String value) {
            return "<a href='mailto:" + value + "'> " + value + "</a>";
        }
    },
    LINKEDIN("LinkedIn: "),
    GITHUB("GitHub: "),
    STACKOVERFLOW("Stack Overflow: "),
    HOMEPAGE("Домашняя страница: ");

    private final String type;

    ContactType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    protected String toHtml0(String value) {
        return type + value;
    }

    public String toHtml(String value) {
        return value == null ? "" : toHtml0(value);
    }
}
