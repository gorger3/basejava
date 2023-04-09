package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() throws ServletException {
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset = UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h3>Список резюме</h3>");
        out.println("<table border=1><tr>" + "<tr><th>UUID</th>" + "<th>Full Name</th></tr>");
        for (Resume r : storage.getAllSorted()) {
            out.println("<tr>" + "<td>" + r.getUuid() + "</td>" + "<td>" + r.getFullName() + "</td></tr>");
        }
        out.println("</table></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void destroy() {
    }
}
