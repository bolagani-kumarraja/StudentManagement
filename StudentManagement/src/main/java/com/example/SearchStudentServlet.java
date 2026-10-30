
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

@WebServlet("/search-student")
public class SearchStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String idText = request.getParameter("id");

        String status = "error";
        String title = "";
        String message = "";

        int studentId = 0;
        String studentName = "";
        String email = "";
        String department = "";
        int year = 0;

        boolean found = false;

        // Validate Student ID
        if (idText == null || idText.isBlank()) {

            title = "Student ID Required";
            message = "Please enter a Student ID to search.";

        } else {

            try {
                int id = Integer.parseInt(idText);

                if (id <= 0) {

                    title = "Invalid Student ID";
                    message = "Student ID must be greater than zero.";

                } else {

                    String sql =
                            "SELECT * FROM students WHERE id = ?";

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        try (Connection con =
                                     DBConnection.getConnection();
                             PreparedStatement ps =
                                     con.prepareStatement(sql)) {

                            ps.setInt(1, id);

                            try (ResultSet rs = ps.executeQuery()) {

                                if (rs.next()) {

                                    found = true;
                                    status = "success";

                                    title = "Student Found";
                                    message = "Student details retrieved "
                                            + "successfully.";

                                    studentId = rs.getInt("id");
                                    studentName =
                                            rs.getString("student_name");
                                    email = rs.getString("email");
                                    department =
                                            rs.getString("department");
                                    year = rs.getInt("year");

                                } else {

                                    title = "Student Not Found";
                                    message = "No student record exists "
                                            + "with ID " + id + ".";
                                }
                            }
                        }

                    } catch (Exception e) {

                        getServletContext().log(
                                "Student search failed", e);

                        response.setStatus(
                                HttpServletResponse
                                        .SC_INTERNAL_SERVER_ERROR);

                        title = "Search Failed";
                        message = "Unable to retrieve student details. "
                                + "Please try again later.";
                    }
                }

            } catch (NumberFormatException e) {

                title = "Invalid Student ID";
                message = "Please enter a valid numeric Student ID.";
            }
        }

        try (PrintWriter out = response.getWriter()) {

            out.println("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>Search Result | StudentHub</title>

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
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            padding: 55px 20px;
                        }

                        .result-card {
                            background: white;
                            width: 100%;
                            max-width: 580px;
                            padding: 38px;
                            border-radius: 16px;
                            border: 1px solid #e2e8f0;
                            box-shadow: 0 10px 35px
                                        rgba(0,0,0,0.06);
                        }

                        .result-icon {
                            width: 70px;
                            height: 70px;
                            border-radius: 50%;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            margin: 0 auto 20px;
                            font-size: 30px;
                            font-weight: 700;
                        }

                        .success .result-icon {
                            background: #dcfce7;
                            color: #16a34a;
                        }

                        .error .result-icon {
                            background: #fee2e2;
                            color: #dc2626;
                        }

                        h1 {
                            text-align: center;
                            font-size: 27px;
                            margin-bottom: 12px;
                        }

                        .subtitle {
                            text-align: center;
                            color: #64748b;
                            font-size: 15px;
                            line-height: 1.7;
                            margin-bottom: 28px;
                        }

                        .details-heading {
                            font-size: 17px;
                            font-weight: 700;
                            margin-bottom: 18px;
                            padding-bottom: 12px;
                            border-bottom: 1px solid #e2e8f0;
                        }

                        .detail-row {
                            display: flex;
                            justify-content: space-between;
                            align-items: flex-start;
                            gap: 20px;
                            padding: 15px 0;
                            border-bottom: 1px solid #f1f5f9;
                        }

                        .detail-label {
                            color: #64748b;
                            font-size: 14px;
                            flex-shrink: 0;
                        }

                        .detail-value {
                            color: #0f172a;
                            font-size: 14px;
                            font-weight: 600;
                            text-align: right;
                            overflow-wrap: anywhere;
                        }

                        .id-badge {
                            display: inline-block;
                            background: #eef2ff;
                            color: #4338ca;
                            padding: 6px 12px;
                            border-radius: 7px;
                        }

                        .dept-badge {
                            display: inline-block;
                            background: #f1f5f9;
                            color: #334155;
                            padding: 6px 12px;
                            border-radius: 7px;
                        }

                        .actions {
                            display: flex;
                            flex-wrap: wrap;
                            justify-content: center;
                            gap: 12px;
                            margin-top: 30px;
                        }

                        .btn {
                            display: inline-block;
                            padding: 13px 19px;
                            border-radius: 8px;
                            font-size: 14px;
                            font-weight: 600;
                            text-decoration: none;
                            text-align: center;
                        }

                        .btn-primary {
                            background: #4f46e5;
                            color: white;
                        }

                        .btn-primary:hover {
                            background: #4338ca;
                        }

                        .btn-outline {
                            border: 1px solid #c7d2fe;
                            color: #4338ca;
                            background: white;
                        }

                        .btn-outline:hover {
                            background: #eef2ff;
                        }

                        footer {
                            background: #0f172a;
                            color: #cbd5e1;
                            padding: 22px;
                            text-align: center;
                            font-size: 13px;
                        }

                        @media (max-width: 600px) {
                            header {
                                padding: 18px 5%;
                            }

                            nav {
                                gap: 12px;
                            }

                            .result-card {
                                padding: 28px 20px;
                            }

                            h1 {
                                font-size: 23px;
                            }

                            .detail-row {
                                gap: 12px;
                            }
                        }
                    </style>
                </head>
                <body>
                """);

            // Header navigation
            out.println("<header><div class='navbar'>");

            out.println("<a href='index.html' "
                    + "class='logo'>🎓 StudentHub</a>");

            out.println("<nav aria-label='Main navigation'>");
            out.println("<a href='index.html'>Home</a>");
            out.println("<a href='registration.html'>Register</a>");
            out.println("<a href='students'>Students</a>");
            out.println("<a href='search-student.html' "
                    + "class='active'>Search</a>");
            out.println("<a href='update-student.html'>Update</a>");
            out.println("<a href='delete-student.html'>Delete</a>");
            out.println("</nav></div></header>");

            // Result card
            out.println("<main>");
            out.println("<section class='result-card "
                    + status + "'>");

            if (found) {
                out.println("<div class='result-icon'>✓</div>");
            } else {
                out.println("<div class='result-icon'>!</div>");
            }

            out.println("<h1>" + escapeHtml(title) + "</h1>");

            out.println("<p class='subtitle'>"
                    + escapeHtml(message) + "</p>");

            // Student details
            if (found) {

                out.println("<h2 class='details-heading'>"
                        + "Student Information</h2>");

                out.println("<div class='detail-row'>"
                        + "<span class='detail-label'>Student ID</span>"
                        + "<span class='detail-value'>"
                        + "<span class='id-badge'>"
                        + studentId
                        + "</span></span></div>");

                out.println("<div class='detail-row'>"
                        + "<span class='detail-label'>Student Name</span>"
                        + "<span class='detail-value'>"
                        + escapeHtml(studentName)
                        + "</span></div>");

                out.println("<div class='detail-row'>"
                        + "<span class='detail-label'>Email Address</span>"
                        + "<span class='detail-value'>"
                        + escapeHtml(email)
                        + "</span></div>");

                out.println("<div class='detail-row'>"
                        + "<span class='detail-label'>Department</span>"
                        + "<span class='detail-value'>"
                        + "<span class='dept-badge'>"
                        + escapeHtml(department)
                        + "</span></span></div>");

                out.println("<div class='detail-row'>"
                        + "<span class='detail-label'>Year of Study</span>"
                        + "<span class='detail-value'>Year "
                        + year
                        + "</span></div>");
            }

            // Action buttons
            out.println("<div class='actions'>");

            out.println("<a href='search-student.html' "
                    + "class='btn btn-primary'>"
                    + "Search Again</a>");

            out.println("<a href='students' "
                    + "class='btn btn-outline'>"
                    + "View All Students</a>");

            out.println("<a href='index.html' "
                    + "class='btn btn-outline'>"
                    + "← Back to Home</a>");

            out.println("</div>");

            out.println("</section>");
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
    }

    private String escapeHtml(String value) {
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
