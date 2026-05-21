<?php
require_once 'db.php';
header('Content-Type: application/json');

$id = intval($_GET['id'] ?? 0);

try {
    $stmt = $pdo->prepare("SELECT * FROM documents WHERE id = ?");
    $stmt->execute([$id]);
    $doc = $stmt->fetch();

    if ($doc) {
        echo json_encode($doc);
    } else {
        echo json_encode(['error' => 'Document record not found.']);
    }
} catch (\PDOException $e) {
    echo json_encode(['error' => $e->getMessage()]);
}
?>