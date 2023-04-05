package com.urise.webapp.web;

import com.urise.webapp.model.Resume;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResumeServlet extends HttpServlet {
    //    private final Storage storage = Config.get().getStorage();
    private String dbUrl = "jdbc:postgresql://localhost:5432/resumes";
    private String dbUser = "postgres";
    private String dbPassword = "postgres";

    final String DRIVER = "org.postgresql.Driver";
    Connection conn = null;

    //    private final SqlHelper sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    @Override
    public void init() throws ServletException {
        try {
            Class.forName(DRIVER);
//            conn = DriverManager.getConnection(Config.get().getDbUrl(), Config.get().getDbUser(), Config.get().getDbPassword());

            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            /*
            Почему-то через Config не работает:

            Выбрасывает такое исключение:
            javax.servlet.ServletException: Servlet.init() для сервлета [resumeServlet] выбросил исключение
            ...
            java.lang.ExceptionInInitializerError
	            com.urise.webapp.web.ResumeServlet.init(ResumeServlet.java:30)
	            ...
            java.lang.IllegalStateException: Invalid resumes.propertiesC:\Users\GGorelik\java\apache-tomcat-9.0.73\bin\config\resumes.properties
	            com.urise.webapp.Config.<init>(Config.java:35)
	            com.urise.webapp.Config.<clinit>(Config.java:13)
	            com.urise.webapp.web.ResumeServlet.init(ResumeServlet.java:30)
	            ...
            **/
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

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


        try (PreparedStatement psForResumes = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            ResultSet resumesRS = psForResumes.executeQuery();
            while (resumesRS.next()) {
                String uuid = resumesRS.getString("uuid");
                String fullName = resumesRS.getString("full_name");
                out.println("<tr>" + "<td>" + uuid + "</td>" + "<td>" + fullName + "</td></tr>");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        out.println("</table></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void destroy() {
        // Close connection object.
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
