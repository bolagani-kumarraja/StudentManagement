
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

@WebServlet("/delete-student")
public class DeleteStudentServlet extends HttpServlet {

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_db";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String idText = request.getParameter("id");

        String title;
        String message;
        String details = "";
        String status = "error";

        int id;

        if (idText == null || idText.isBlank()) {

            title = "Student ID Required";
            message = "Please enter a valid Student ID.";

        } else {

            try {
                id = Integer.parseInt(idText);

                if (id <= 0) {

                    title = "Invalid Student ID";
                    message = "Student ID must be greater than zero.";

                } else {

                    String sql =
                            "DELETE FROM students WHERE id = ?";

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        try (Connection con =
                                     DriverManager.getConnection(
                                             URL, USER, PASSWORD);
                             PreparedStatement ps =
                                     con.prepareStatement(sql)) {

                            ps.setInt(1, id);

                            int rows = ps.executeUpdate();

                            if (rows > 0) {

                                status = "success";
                                title = "Student Deleted Successfully!";
                                message = "The student record has been "
                                        + "removed from the database.";
                                details = "Deleted Student ID: " + id;

                            } else {

                                title = "Student Not Found";
                                message = "No student record exists "
                                        + "with the provided Student ID.";
                                details = "Student ID: " + id;
                            }
                        }

                    } catch (Exception e) {

                        getServletContext().log(
                                "Student deletion failed", e);

                        response.setStatus(
                                HttpServletResponse
                                        .SC_INTERNAL_SERVER_ERROR);

                        title = "Delete Operation Failed";
                        message = "Unable to delete the student. "
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

                    <title>Delete Student Result | StudentHub</title>

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
                            width: 100%;
                            max-width: 520px;
                            background: white;
                            padding: 45px 35px;
                            border-radius: 16px;
                            border: 1px solid #e2e8f0;
                            box-shadow: 0 10px 35px
                                        rgba(0,0,0,0.06);
                            text-align: center;
                        }

                        .result-icon {
                            width: 75px;
                            height: 75px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            border-radius: 50%;
                            margin: 0 auto 25px;
                            font-size: 35px;
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
                            margin-bottom: 14px;
                        }

                        .message {
                            color: #64748b;
                            line-height: 1.7;
                            font-size: 15px;
                        }

                        .details {
                            margin-top: 25px;
                            padding: 15px;
                            border-radius: 8px;
                            background: #f8fafc;
                            color: #334155;
                            font-size: 14px;
                            font-weight: 600;
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
                                padding: 35px 20px;
                            }

                            h1 {
                                font-size: 23px;
                            }
                        }
                    </style>
                </head>
                <body>
                """);

            // Header
            out.println("<header><div class='navbar'>");

            out.println("<a href='index.html' "
                    + "class='logo'>🎓 StudentHub</a>");

            out.println("<nav aria-label='Main navigation'>");
            out.println("<a href='index.html'>Home</a>");
            out.println("<a href='registration.html'>Register</a>");
            out.println("<a href='students'>Students</a>");
            out.println("<a href='search-student.html'>Search</a>");
            out.println("<a href='update-student.html'>Update</a>");
            out.println("<a href='delete-student.html' "
                    + "class='active'>Delete</a>");
            out.println("</nav></div></header>");

            // Result card
            out.println("<main>");

            out.println("<section class='result-card "
                    + status + "'>");

            if ("success".equals(status)) {
                out.println("<div class='result-icon'>✓</div>");
            } else {
                out.println("<div class='result-icon'>!</div>");
            }

            out.println("<h1>" + title + "</h1>");

            out.println("<p class='message'>"
                    + message + "</p>");

            if (!details.isEmpty()) {
                out.println("<div class='details'>"
                        + details + "</div>");
            }

            out.println("<div class='actions'>");

            out.println("<a href='students' "
                    + "class='btn btn-primary'>"
                    + "View All Students</a>");

            out.println("<a href='delete-student.html' "
                    + "class='btn btn-outline'>"
                    + "Delete Another</a>");

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
}
