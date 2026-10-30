


STUDENT MANAGEMENT SYSTEM
=========================

1. PROJECT OVERVIEW
-------------------
Student Management System is a Java web application that allows users to
register, view, search, update, and delete student records.

2. TECHNOLOGIES USED
--------------------
- Java 17
- Jakarta Servlet API 6.0
- Apache Maven
- Apache Tomcat 10.1 (recommended)
- MySQL
- MySQL Connector/J 9.4.0
- HTML and CSS

3. PROJECT STRUCTURE
--------------------
StudentManagement/
    pom.xml
    src/
      main/
        java/com/example/
          StudentRegisterServlet.java
          StudentListServlet.java
          SearchStudentServlet.java
          UpdateStudentServlet.java
          DeleteStudentServlet.java
        webapp/
          index.html
          registration.html
          search-student.html
          update-student.html
          delete-student.html

NOTE: In this GitHub repository, the Maven project (the folder containing
pom.xml) is inside the StudentManagement/ directory.

4. FEATURES
-----------
- Register a new student
- Display student records
- Search for a student
- Update student information
- Delete a student record
- Validate required form fields and year of study

5. DATABASE SETUP
-----------------
Create a MySQL database:

    CREATE DATABASE student_db;

Select the database:

    USE student_db;

Create the students table:

    CREATE TABLE students (
        id INT AUTO_INCREMENT PRIMARY KEY,
        student_name VARCHAR(100) NOT NULL,
        email VARCHAR(150) NOT NULL,
        department VARCHAR(100) NOT NULL,
        year INT NOT NULL
    );

6. DATABASE CONNECTION
----------------------
The current servlet source files use this local development connection:

    URL      : jdbc:mysql://localhost:3306/student_db
    Username : root
    Password : root

Before deploying this application to a public server, update the database
connection settings in all servlet files to use your actual database host
and credentials. Do not publish real database passwords in GitHub.

For a cloud deployment, use environment variables or a shared database
connection utility rather than hard-coding credentials. Configure the
database host, port, database name, username, and password in the hosting
provider's environment settings.

7. RUN LOCALLY
--------------
Requirements:
- JDK 17
- Apache Maven
- MySQL Server
- Apache Tomcat 10.1

Steps:
1. Start MySQL.
2. Create the student_db database and students table using the SQL above.
3. Open a terminal in the folder that contains pom.xml:

       cd StudentManagement

4. Build the WAR file:

       mvn clean package

5. The generated WAR file will be:

       target/StudentManagement.war

6. Deploy StudentManagement.war to Apache Tomcat 10.1.
7. Start Tomcat and open:

       http://localhost:8080/StudentManagement/

If the WAR is deployed with a different context name, use that context name
in the URL.

8. BUILD OUTPUT
---------------
Maven creates the deployable WAR file at:

    target/StudentManagement.war

The target/ directory contains generated build files and should not normally
be committed to Git.

9. IMPORTANT DEPLOYMENT NOTES
-----------------------------
- This is a Java Servlet application; GitHub Pages cannot run Java Servlets.
- Deploy the WAR to a Java servlet container such as Apache Tomcat.
- Use a MySQL database that is reachable from the application server.
- Keep database credentials private and configure them securely.
- The application uses Jakarta Servlet 6.0, so use a compatible server such
  as Tomcat 10.1.

10. AUTHOR
----------
Project: Student Management System
