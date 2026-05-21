<?php
require_once 'db.php';

header('Content-Type: application/json');

// url example: get_documents.php?filter_type=format&value=PDF
$filterType = $_GET['filter_type'] ?? '';
$value      = $_GET['value'] ?? '';

try {
    $sql = "SELECT * FROM documents";
    $params = [];

    if (($filterType === 'document_type' || $filterType === 'document_format') && !empty($value)) {
        $sql .= " WHERE $filterType = ?";
        $params[] = $value;
    }

    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);

    $documents = $stmt->fetchAll();

    echo json_encode($documents);

} catch (\PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Database query failed: ' . $e->getMessage()]);
}
?>