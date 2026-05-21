<?php
require_once 'db.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id     = intval($_POST['id'] ?? 0);
    $title  = $_POST['title'] ?? '';
    $author = $_POST['author'] ?? '';
    $pages  = intval($_POST['number_of_pages'] ?? 0);
    $type   = $_POST['document_type'] ?? '';
    $format = $_POST['document_format'] ?? '';

    if ($id > 0 && !empty($title) && !empty($author)) {
        try {
            $sql = "UPDATE documents SET title = ?, author = ?, number_of_pages = ?, document_type = ?, document_format = ? WHERE id = ?";
            $pdo->prepare($sql)->execute([$title, $author, $pages, $type, $format, $id]);
        } catch (\PDOException $e) {
            die("Error processing modification request: " . $e->getMessage());
        }
    }
}
header("Location: index.html");
exit;
?>