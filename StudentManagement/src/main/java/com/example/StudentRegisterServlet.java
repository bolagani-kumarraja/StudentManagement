
package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register-student")
public class StudentRegisterServlet extends HttpServlet {

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_db";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("studentName");
        String email = request.getParameter("email");
        String department = request.getParameter("department");
        String yearText = request.getParameter("year");

        String status = "error";
        String title;
        String message;

        boolean registered = false;
        int year = 0;

        // Validate form data
        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || department == null || department.isBlank()
                || yearText == null || yearText.isBlank()) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST);

            title = "Missing Information";
            message = "Please fill in all required fields.";

        } else {

            try {
                year = Integer.parseInt(yearText);

                if (year < 1 || year > 4) {

                    response.setStatus(
                            HttpServletResponse.SC_BAD_REQUEST);

                    title = "Invalid Year";
                    message = "Please select a valid year of study.";

                } else {

                    String sql = "INSERT INTO students "
                            + "(student_name, email, department, year) "
                            + "VALUES (?, ?, ?, ?)";

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        try (Connection con =
                                     DriverManager.getConnection(
                                             URL, USER, PASSWORD);
                             PreparedStatement ps =
                                     con.prepareStatement(sql)) {

                            ps.setString(1, name);
                            ps.setString(2, email);
                            ps.setString(3, department);
                            ps.setInt(4, year);

                            int rows = ps.executeUpdate();

                            if (rows > 0) {

                                registered = true;
                                status = "success";

                                title = "Registration Successful!";
                                message = "Student information has been "
                                        + "saved successfully.";

                            } else {

                                title = "Registration Failed";
                                message = "Student record could not "
                                        + "be saved.";
                            }
                        }

                    } catch (Exception e) {

                        getServletContext().log(
                                "Student registration failed", e);

                        response.setStatus(
                                HttpServletResponse
                                        .SC_INTERNAL_SERVER_ERROR);

                        title = "Registration Failed";
                        message = "Unable to register the student. "
                                + "Please try again later.";
                    }
                }

            } catch (NumberFormatException e) {

                response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST);

                title = "Invalid Year";
                message = "Please select a valid year of study.";
            }
        }

        // Render professional result page
        try (PrintWriter out = response.getWriter()) {

            out.println("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>Registration Result | StudentHub</title>

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
                            padding: 40px;
                            border-radius: 16px;
                            border: 1px solid #e2e8f0;
                            box-shadow: 0 10px 35px
                                        rgba(0,0,0,0.06);
                        }

                        .result-icon {
                            width: 75px;
                            height: 75px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            border-radius: 50%;
                            margin: 0 auto 22px;
                            font-size: 34px;
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
                            font-size: 27px;
                            text-align: center;
                            margin-bottom: 12px;
                        }

                        .subtitle {
                            color: #64748b;
                            font-size: 15px;
                            line-height: 1.7;
                            text-align: center;
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

                        .dept-badge {
                            display: inline-block;
                            background: #eef2ff;
                            color: #4338ca;
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
                            padding: 13px 20px;
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

                            .result-card {
                                padding: 28px 20px;
                            }

                            h1 {
                                font-size: 23px;
                            }
                        }
                    </style>
                </head>

                <body>
                """);

            // Navigation bar
            out.println("<header><div class='navbar'>");

            out.println("<a href='index.html' "
                    + "class='logo'>🎓 StudentHub</a>");

            out.println("<nav aria-label='Main navigation'>");
            out.println("<a href='index.html'>Home</a>");
            out.println("<a href='registration.html' "
                    + "class='active'>Register</a>");
            out.println("<a href='students'>Students</a>");
            out.println("<a href='search-student.html'>Search</a>");
            out.println("<a href='update-student.html'>Update</a>");
            out.println("<a href='delete-student.html'>Delete</a>");
            out.println("</nav></div></header>");

            // Result card
            out.println("<main>");

            out.println("<section class='result-card "
                    + status + "'>");

            if (registered) {
                out.println("<div class='result-icon'>✓</div>");
            } else {
                out.println("<div class='result-icon'>!</div>");
            }

            out.println("<h1>" + escapeHtml(title) + "</h1>");

            out.println("<p class='subtitle'>"
                    + escapeHtml(message) + "</p>");

            // Display registered student information
            if (registered) {

                out.println("<h2 class='details-heading'>"
                        + "Registered Student Details</h2>");

                out.println("<div class='detail-row'>"
                        + "<span class='detail-label'>Student Name</span>"
                        + "<span class='detail-value'>"
                        + escapeHtml(name)
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

            out.println("<a href='registration.html' "
                    + "class='btn btn-primary'>"
                    + "Register Another</a>");

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
