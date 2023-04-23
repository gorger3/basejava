package com.urise.webapp.util;

import com.urise.webapp.model.*;

import java.util.Map;

public class HtmlHelper {

    public String sectionContentToHtml(Map.Entry<SectionType, Section> entry) {
        switch (entry.getKey()) {
            case PERSONAL:
            case OBJECTIVE:
                return "<input type=\"text\" name=\"" + entry.getKey() + "\" size=\"50\" value=\"" + entry.getValue() + "\">";
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                ListSection listSection = (ListSection) entry.getValue();
                StringBuilder listText = new StringBuilder();
                for (String item : listSection.getItems()) {
                    listText.append(item).append("\n");
                }
                return "<textarea name=\"" + entry.getKey() + "\" rows=\"5\" cols=\"50\">" + listText + "</textarea>";
            case EXPERIENCE:
            case EDUCATION:
                OrganizationSection orgSection = (OrganizationSection) entry.getValue();
                StringBuilder orgText = new StringBuilder();
                for (Organization org : orgSection.getOrganizations()) {
                    orgText.append(org).append("\n");
                }
                return "<textarea name=\"" + entry.getKey() + "\" rows=\"7\" cols=\"50\">" + orgText + "</textarea>";

            default:
                throw new IllegalArgumentException("There is no section of " + entry.getKey() + " type.");

        }
    }

    public String htmlToSectionContent(SectionType type, Resume r) {
//        r.getSection(type).getClass().getName();
        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
//                <dd><input type="text" name="${contactType.name()}" size="30" value="${resume.getContact(contactType)}"></dd>
                TextSection textSection = (TextSection) r.getSection(type);
//                Section textSection = r.getSection(type);
                String text = textSection != null ? textSection.toString() : "";
                return "<input type=\"text\" name=\"" + type.name() + "\" size=\"50\" value= \"" + text + "\">";
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                ListSection listSection = (ListSection) r.getSection(type);
                StringBuilder listText = new StringBuilder();
                if (listSection != null) {
                    for (String item : listSection.getItems()) {
                        listText.append(item).append("\n");
                    }
                }
                return "<textarea name=\"" + type.name() + "\" rows=\"5\" cols=\"50\">" + listText + "</textarea>";
            case EXPERIENCE:
            case EDUCATION:
                OrganizationSection orgSection = (OrganizationSection) r.getSection(type);
                StringBuilder orgText = new StringBuilder();
                if (orgSection != null) {
                    for (Organization org : orgSection.getOrganizations()) {
                        orgText.append(org).append("\n");
                    }
                }
                return "<textarea name=\"" + type.name() + "\" rows=\"7\" cols=\"50\">" + orgText + "</textarea>";

            default:
                throw new IllegalArgumentException("There is no section of " + type.getTitle() + " type.");

        }
    }

//    public String htmlToSectionContent(SectionType type, Resume r) {
//        switch (type) {
//            case PERSONAL:
//            case OBJECTIVE:
////                <dd><input type="text" name="${contactType.name()}" size="30" value="${resume.getContact(contactType)}"></dd>
//                TextSection textSection = (TextSection) r.getSection(type);
//                String text = textSection != null ? textSection.getContent() : "";
//                return "<input type=\"text\" name=" + type.name() + " size=\"50\" value=\"" + text + "\">";
//            case ACHIEVEMENT:
//            case QUALIFICATIONS:
//                ListSection listSection = (ListSection) r.getSection(type);
//                StringBuilder listText = new StringBuilder();
//                if (listSection != null) {
//                    for (String item : listSection.getItems()) {
//                        listText.append(item).append("\n");
//                    }
//                }
//                return "<textarea name=" + type.name() + " rows=\"5\" cols=\"50\">" + listText + "</textarea>";
//            case EXPERIENCE:
//            case EDUCATION:
//                OrganizationSection orgSection = (OrganizationSection) r.getSection(type);
//                StringBuilder orgText = new StringBuilder();
//                if (orgSection != null) {
//                    for (Organization org : orgSection.getOrganizations()) {
//                        orgText.append(org).append("\n");
//                    }
//                }
//                return "<textarea name=" + type.name() + " rows=\"7\" cols=\"50\">" + orgText + "</textarea>";
//
//            default:
//                throw new IllegalArgumentException("There is no section of " + type.getTitle() + " type.");
//
//        }
//    }

//    <c:forEach var="contactType" items="${ContactType.values()}">
//            <jsp:useBean id="contactType" type="com.urise.webapp.model.ContactType"/>
//            <dl>
//    <dt>${contactType.title}</dt>
//                <dd><input type="text" name="${contactType.name()}" size="30" value="${resume.getContact(contactType)}"></dd>
//            </dl>
//        </c:forEach>
}
