<?php
$host = '127.0.0.1'; // localhost
$db   = 'document_manager';
$user = 'root';
$pass = '';
$charset = 'utf8mb4';

$dsn = "mysql:host=$host;dbname=$db;charset=$charset";

$options = [
    // Throw exceptions when errors occur
    PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
    // Return data as a simple associative array (key-value pairs)
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    // This setting specifically forces real prepared statements to stop SQL injection
    PDO::ATTR_EMULATE_PREPARES => false,
];

try {
    $pdo = new PDO($dsn, $user, $pass, $options);
} catch (\PDOException $e) {
    die("Database connection failed: " . $e->getMessage());
}
?>