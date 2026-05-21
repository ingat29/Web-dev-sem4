<?php
require_once 'db.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $title    = $_POST['title'] ?? '';
    $author   = $_POST['author'] ?? '';
    $pages    = intval($_POST['number_of_pages'] ?? 0);
    $type     = $_POST['document_type'] ?? '';
    $format   = $_POST['document_format'] ?? '';

    if (!empty($title) && !empty($author) && $pages > 0) {
        try {
            $sql = "INSERT INTO documents (title, author, number_of_pages, document_type, document_format) VALUES (?, ?, ?, ?, ?)";
            $stmt = $pdo->prepare($sql);
            $stmt->execute([$title, $author, $pages, $type, $format]);
        } catch (\PDOException $e) {
            die("Error inserting entry: " . $e->getMessage());
        }
    }
}

header("Location: index.html");
exit;
?>