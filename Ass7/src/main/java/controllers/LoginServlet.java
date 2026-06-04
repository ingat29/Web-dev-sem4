package main.java.controllers;

import main.java.utils.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    // override doPost because HTML form uses method="POST"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            // Using PreparedStatement prevents SQL Injection attacks
            String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");

                // Create a Session
                HttpSession session = request.getSession();
                session.setAttribute("user_id", userId);
                session.setAttribute("username", user);

                response.sendRedirect("puzzle.jsp");
            } else {
                request.setAttribute("errorMessage", "Invalid username or password!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error occurred.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}