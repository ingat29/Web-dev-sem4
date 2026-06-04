<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("user_id") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Image Puzzle Game</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #2c3e50;
            color: white;
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-top: 50px;
        }
        .header-bar {
            display: flex;
            justify-content: space-between;
            width: 320px;
            margin-bottom: 20px;
        }
        #puzzle-board {
            display: grid;
            grid-template-columns: repeat(3, 100px);
            grid-template-rows: repeat(3, 100px);
            gap: 2px;
            background-color: #34495e;
            padding: 10px;
            border-radius: 8px;
        }
        .tile {
            width: 100px;
            height: 100px;
            background-color: #ecf0f1;
            /* image placeholder */
            background-image: url('https://miro.medium.com/0*uQo-YVFfoMp9Eai9');
            background-size: 300px 300px;
            cursor: pointer;
            border-radius: 4px;
            transition: all 0.2s;
        }
        .tile.empty {
            background-image: none;
            background-color: transparent;
            cursor: default;
        }
        .stats { margin-top: 20px; font-size: 18px; }
        .logout-btn {
            background-color: #e74c3c; color: white; border: none; padding: 5px 10px; border-radius: 4px; text-decoration: none;
        }
    </style>
</head>
<body>

    <div class="header-bar">
        <span>Welcome, <%= username %>!</span>
        <div>
            <button onclick="resetPuzzle()" class="logout-btn" style="background-color: #f39c12; margin-right: 10px;">Reset</button>
            <a href="LogoutServlet" class="logout-btn">Logout</a>
        </div>
    </div>

    <div id="puzzle-board"></div>

    <div class="stats">
        Moves: <span id="move-counter">0</span>
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            loadPuzzle();
        });

        function loadPuzzle() {
            // AJAX call to get the current state
            fetch('PuzzleServlet?action=load')
                .then(response => response.json())
                .then(data => renderBoard(data.grid, data.moves));
        }

        function resetPuzzle() {
            if (confirm("Are you sure you want to reset the puzzle? All moves will be lost!")) {
                fetch('PuzzleServlet?action=reset')
                    .then(response => response.json())
                    .then(data => {
                        if (data.error) return console.error(data.error);
                        renderBoard(data.grid, data.moves);
                    });
            }
        }

        function moveTile(clickedIndex) {
            fetch('PuzzleServlet?action=move&index=' + clickedIndex)
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        console.error("Server Error:", data.error);
                        return;
                    }
                    renderBoard(data.grid, data.moves);
                });
        }

        function renderBoard(gridArray, moves) {
            const board = document.getElementById("puzzle-board");
            document.getElementById("move-counter").innerText = moves;
            board.innerHTML = "";

            gridArray.forEach((tileValue, index) => {
                const div = document.createElement("div");
                div.className = "tile";

                if (tileValue === 0) {
                    div.classList.add("empty");
                } else {
                    let val = parseInt(tileValue);
                    let originalRow = Math.floor((val - 1) / 3);
                    let originalCol = (val - 1) % 3;

                    div.style.backgroundPosition = "-" + (originalCol * 100) + "px -" + (originalRow * 100) + "px";
                    // Optional: number inside the tile so it's easier to solve
                    // div.innerText = val;

                    div.onclick = () => moveTile(index);
                }
                board.appendChild(div);
            });
        }
    </script>
</body>
</html>