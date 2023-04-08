package com.urise.webapp.web;

import com.urise.webapp.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class ResumeServlet extends HttpServlet {
    //    private final Storage storage = Config.get().getStorage();
    final String DRIVER = "org.postgresql.Driver";
    Connection conn = null;

    //    private final SqlHelper sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    @Override
    public void init() throws ServletException {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(Config.get().getDbUrl(), Config.get().getDbUser(), Config.get().getDbPassword());

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
