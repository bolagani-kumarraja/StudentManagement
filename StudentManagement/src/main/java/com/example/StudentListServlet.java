
package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/students")
public class StudentListServlet extends HttpServlet {

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_db";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String sql = "SELECT * FROM students";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con =
                         DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery();
                 PrintWriter out = response.getWriter()) {

                String home = request.getContextPath()
                        + "/index.html";

                out.println("""
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport"
                              content="width=device-width, initial-scale=1.0">
                        <title>All Students | StudentHub</title>

                        <style>
                            * {
                                margin: 0;
                                padding: 0;
                                box-sizing: border-box;
                            }

                            body {
                                font-family: 'Segoe UI', Arial, sans-serif;
                                background: #f5f7fb;
                                color: #1e293b;
                                min-height: 100vh;
                                display: flex;
                                flex-direction: column;
                            }

                            header {
                                background: white;
                                border-bottom: 1px solid #e2e8f0;
                                padding: 18px 7%;
                            }

                            .navbar {
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                                gap: 20px;
                                flex-wrap: wrap;
                            }

                            .logo {
                                color: #4f46e5;
                                font-size: 24px;
                                font-weight: 800;
                                text-decoration: none;
                            }

                            nav {
                                display: flex;
                                flex-wrap: wrap;
                                gap: 22px;
                            }

                            nav a {
                                text-decoration: none;
                                color: #475569;
                                font-size: 14px;
                                font-weight: 500;
                            }

                            nav a:hover,
                            nav a.active {
                                color: #4f46e5;
                            }

                            main {
                                flex: 1;
                                width: 100%;
                                padding: 55px 7%;
                            }

                            .page-heading {
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                                gap: 20px;
                                flex-wrap: wrap;
                                margin-bottom: 30px;
                            }

                            .page-heading h1 {
                                font-size: 30px;
                                margin-bottom: 10px;
                            }

                            .page-heading p {
                                color: #64748b;
                                font-size: 15px;
                            }

                            .add-btn {
                                display: inline-block;
                                background: #4f46e5;
                                color: white;
                                text-decoration: none;
                                padding: 13px 22px;
                                border-radius: 8px;
                                font-size: 14px;
                                font-weight: 600;
                            }

                            .add-btn:hover {
                                background: #4338ca;
                            }

                            .table-card {
                                background: white;
                                border: 1px solid #e2e8f0;
                                border-radius: 14px;
                                overflow: hidden;
                                box-shadow: 0 8px 25px rgba(0,0,0,0.04);
                            }

                            .table-header {
                                padding: 24px;
                                border-bottom: 1px solid #e2e8f0;
                            }

                            .table-header h2 {
                                font-size: 20px;
                                margin-bottom: 6px;
                            }

                            .table-header p {
                                color: #64748b;
                                font-size: 14px;
                            }

                            .table-wrapper {
                                overflow-x: auto;
                            }

                            table {
                                width: 100%;
                                border-collapse: collapse;
                                text-align: left;
                            }

                            thead {
                                background: #f8fafc;
                            }

                            th {
                                padding: 17px 22px;
                                color: #475569;
                                font-size: 13px;
                                font-weight: 700;
                                white-space: nowrap;
                            }

                            td {
                                padding: 17px 22px;
                                font-size: 14px;
                                border-top: 1px solid #e2e8f0;
                            }

                            tbody tr:hover {
                                background: #f8fafc;
                            }

                            .id-badge {
                                display: inline-block;
                                background: #eef2ff;
                                color: #4338ca;
                                padding: 7px 11px;
                                border-radius: 7px;
                                font-weight: 600;
                            }

                            .name {
                                font-weight: 600;
                                color: #0f172a;
                            }

                            .dept-badge {
                                display: inline-block;
                                background: #f1f5f9;
                                color: #334155;
                                padding: 7px 11px;
                                border-radius: 7px;
                                font-weight: 500;
                            }

                            .empty-message {
                                text-align: center;
                                padding: 45px;
                                color: #64748b;
                            }

                            .bottom-actions {
                                display: flex;
                                justify-content: center;
                                margin-top: 30px;
                            }

                            .back-btn {
                                display: inline-block;
                                background: white;
                                color: #4f46e5;
                                border: 1px solid #c7d2fe;
                                text-decoration: none;
                                padding: 13px 24px;
                                border-radius: 8px;
                                font-size: 14px;
                                font-weight: 600;
                            }

                            .back-btn:hover {
                                background: #eef2ff;
                            }

                            footer {
                                background: #0f172a;
                                color: #cbd5e1;
                                text-align: center;
                                padding: 22px;
                                font-size: 13px;
                            }

                            @media (max-width: 600px) {
                                header {
                                    padding: 18px 5%;
                                }

                                nav {
                                    gap: 12px;
                                }

                                main {
                                    padding: 35px 5%;
                                }

                                .page-heading h1 {
                                    font-size: 25px;
                                }
                            }
                        </style>
                    </head>

                    <body>
                    """);

                // Navigation
                out.println("<header><div class='navbar'>");

                out.println("<a href='" + home
                        + "' class='logo'>🎓 StudentHub</a>");

                out.println("<nav aria-label='Main navigation'>");

                out.println("<a href='" + home + "'>Home</a>");
                out.println("<a href='registration.html'>Register</a>");
                out.println("<a href='students' class='active'>Students</a>");
                out.println("<a href='search-student.html'>Search</a>");
                out.println("<a href='update-student.html'>Update</a>");
                out.println("<a href='delete-student.html'>Delete</a>");

                out.println("</nav></div></header>");

                // Main content
                out.println("<main>");

                out.println("""
                    <div class="page-heading">
                        <div>
                            <h1>All Students</h1>
                            <p>View and manage registered student records.</p>
                        </div>

                        <a href="registration.html" class="add-btn">
                            + Add New Student
                        </a>
                    </div>

                    <section class="table-card">

                        <div class="table-header">
                            <h2>Student Records</h2>
                            <p>Complete list of registered students</p>
                        </div>

                        <div class="table-wrapper">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Student ID</th>
                                        <th>Student Name</th>
                                        <th>Email Address</th>
                                        <th>Department</th>
                                        <th>Year</th>
                                    </tr>
                                </thead>
                                <tbody>
                    """);

                boolean hasStudents = false;

                while (rs.next()) {
                    hasStudents = true;

                    out.println("<tr>");

                    out.println("<td><span class='id-badge'>"
                            + rs.getInt("id")
                            + "</span></td>");

                    out.println("<td class='name'>"
                            + escapeHtml(rs.getString("student_name"))
                            + "</td>");

                    out.println("<td>"
                            + escapeHtml(rs.getString("email"))
                            + "</td>");

                    out.println("<td><span class='dept-badge'>"
                            + escapeHtml(rs.getString("department"))
                            + "</span></td>");

                    out.println("<td>Year "
                            + rs.getInt("year")
                            + "</td>");

                    out.println("</tr>");
                }

                if (!hasStudents) {
                    out.println("""
                        <tr>
                            <td colspan="5" class="empty-message">
                                No students registered yet.
                            </td>
                        </tr>
                        """);
                }

                out.println("""
                                </tbody>
                            </table>
                        </div>
                    </section>
                    """);

                // Back to Home button
                out.println("<div class='bottom-actions'>");

                out.println("<a href='" + home
                        + "' class='back-btn'>"
                        + "← Back to Home</a>");

                out.println("</div>");
                out.println("</main>");

                // Footer
                out.println("""
                    <footer>
                        &copy; 2026 StudentHub |
                        Java-Servlet Web Application Project
                    </footer>

                    </body>
                    </html>
                    """);

            }

        } catch (Exception e) {
            getServletContext().log(
                    "Unable to display students", e);

            if (!response.isCommitted()) {
                response.sendError(
                        500,
                        "Unable to display students. "
                        + "Check the server console.");
            }
        }
    }

    // Escape database text before inserting it into HTML
    private static String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
