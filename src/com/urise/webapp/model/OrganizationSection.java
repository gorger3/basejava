package com.urise.webapp.model;

import java.util.List;

public class OrganizationSection extends AbstractSection {

    private List<Organization> content;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < content.size(); i++) {
            sb.append(content.get(i) + "\n");
        }
        return sb.toString();
    }

    public List<Organization> getContent() {
        return content;
    }

    public OrganizationSection(List<Organization> content) {
        this.content = content;
    }

}
