<?php
require_once 'db.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id = intval($_POST['id'] ?? 0);
    if ($id > 0) {
        try {
            $pdo->prepare("DELETE FROM documents WHERE id = ?")->execute([$id]);
        } catch (\PDOException $e) {
            die("Error deleting record execution: " . $e->getMessage());
        }
    }
}
header("Location: index.html");
exit;
?>