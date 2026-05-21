<?php
require_once 'db.php';
header('Content-Type: application/json');

try {
    $stmt = $pdo->query("SELECT COUNT(*) as total_docs, SUM(number_of_pages) as total_pages FROM documents");
    $stats = $stmt->fetch();
    echo json_encode($stats);
} catch (\PDOException $e) {
    echo json_encode(['error' => $e->getMessage()]);
}
?>