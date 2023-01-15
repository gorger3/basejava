package com.urise.webapp.model;

import java.util.List;

public class Organization {
    private final String name;
    private final String website;
    private final List<Period> periods;
    private Period period;

    public Organization(String name, String website, List<Period> periods) {
        this.name = name;
        this.website = website;
        this.periods = periods;
    }

    public Organization(String name, String website, List<Period> periods, int i) {
        this.name = name;
        this.website = website;
        this.periods = periods;
        this.period = periods.get(i);
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
