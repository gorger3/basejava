package com.urise.webapp.model;

import java.util.List;

public class Organization {
    private final String name;

    private final String website;

    private final List<Period> periods;

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public Organization(String name, String website, List<Period> periods) {
        this.name = name;
        this.website = website;
        this.periods = periods;
    }

    @Override
    public String toString() {
        return period.getStartDate() + "   " +
                period.getEndDate() + "   " +
                name + "   " +
                website + "   " +
                period.getTitle() + "   " +
                period.getDescription();
    }

}
