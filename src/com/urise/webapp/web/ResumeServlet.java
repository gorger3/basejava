package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() throws ServletException {
        storage = Config.get().getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        TextSection text = new TextSection(value);
                        r.addSection(type, text);
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        String delim = "\n";
                        String[] items = value.split(delim);
                        ListSection list = new ListSection(items);
                        r.addSection(type, list);
                }
            } else {
                r.getSections().remove(type);
            }
        }
        storage.update(r);
        response.sendRedirect("resume");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal.");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp").forward(request, response);
    }

    public void destroy() {
    }
}

//for (SectionType type : SectionType.values()) {
//        String[] values = request.getParameterValues(type.name());
//        if (value != null && value.trim().length() != 0) {
//        switch (type) { // <input type=\"text\" name=" + type.getTitle() + " size=\"50\" value=\"" + text + "\">
//        case PERSONAL:
//        case OBJECTIVE:
//        TextSection text = new TextSection(value);
//        r.addSection(type, text);
//        case ACHIEVEMENT:
//        case QUALIFICATIONS:
//        String delim = "\n";
//        String[] items = value.split(delim);
//        ListSection list = new ListSection(items);
//        r.addSection(type, list);
//        }
//        } else {
//        r.getSections().remove(type);
//        }
//        }

//    String[] values = request.getParameterValues("section");
//    SectionType[] sectionTypes = SectionType.values();
//        if (values != null) {
//                int i =0;
//                for (String value : values) {
//                if (value != null && value.trim().length() != 0) {
//                switch (i) { // <input type=\"text\" name=" + type.getTitle() + " size=\"50\" value=\"" + text + "\">
//                case 0:
//                case 1:
//                TextSection textSection = new TextSection(values[i]);
//                r.addSection(sectionTypes[i], textSection);
//                case 2:
//                case 3:
//                String delim = "\n";
//                String[] items = value.split(delim);
//                ListSection list = new ListSection(items);
//                r.addSection(sectionTypes[i], list);
//                }
//                } else {
//                r.getSections().remove(sectionTypes[i]);
//                }
//                i++;
//                }
//                }