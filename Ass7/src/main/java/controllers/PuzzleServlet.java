package main.java.controllers;

import main.java.utils.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/PuzzleServlet")
public class PuzzleServlet extends HttpServlet {

    // Default starting state (0 is the empty space)
    private static final String DEFAULT_GRID = "1,2,3,4,5,6,0,7,8";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int userId = (int) session.getAttribute("user_id");
        String action = request.getParameter("action");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            String gridState = DEFAULT_GRID;
            int moves = 0;

            PreparedStatement selectStmt = conn.prepareStatement("SELECT grid_state, moves FROM puzzle_state WHERE user_id = ?");
            selectStmt.setInt(1, userId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                gridState = rs.getString("grid_state");
                moves = rs.getInt("moves");
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO puzzle_state (user_id, grid_state, moves) VALUES (?, ?, 0)");
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, gridState);
                insertStmt.executeUpdate();
            }

            if ("move".equals(action)) {
                int clickedIndex = Integer.parseInt(request.getParameter("index"));
                String[] tiles = gridState.split(",");

                int emptyIndex = -1;
                for (int i = 0; i < tiles.length; i++) {
                    if (tiles[i].equals("0")) {
                        emptyIndex = i;
                        break;
                    }
                }

                // Server-Side Game Logic: Check if move is valid
                // Valid moves are adjacent: diff is 1 (horizontal) or 3 (vertical)
                boolean isSameRow = (clickedIndex / 3) == (emptyIndex / 3);
                boolean isHorizontalMove = Math.abs(clickedIndex - emptyIndex) == 1 && isSameRow;
                boolean isVerticalMove = Math.abs(clickedIndex - emptyIndex) == 3;

                if (isHorizontalMove || isVerticalMove) {
                    String temp = tiles[clickedIndex];
                    tiles[clickedIndex] = tiles[emptyIndex];
                    tiles[emptyIndex] = temp;
                    moves++;

                    gridState = String.join(",", tiles);

                    // Update Database
                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE puzzle_state SET grid_state = ?, moves = ? WHERE user_id = ?");
                    updateStmt.setString(1, gridState);
                    updateStmt.setInt(2, moves);
                    updateStmt.setInt(3, userId);
                    updateStmt.executeUpdate();
                }
            }
            else if ("reset".equals(action)) {
                gridState = DEFAULT_GRID;
                moves = 0;

                PreparedStatement resetStmt = conn.prepareStatement("UPDATE puzzle_state SET grid_state = ?, moves = ? WHERE user_id = ?");
                resetStmt.setString(1, gridState);
                resetStmt.setInt(2, moves);
                resetStmt.setInt(3, userId);
                resetStmt.executeUpdate();
            }

            // Send the JSON response back to the browser
            String jsonGrid = "[" + gridState + "]";
            String jsonResponse = String.format("{\"grid\": %s, \"moves\": %d}", jsonGrid, moves);
            out.print(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Database connection failed\"}");
        }
    }
}