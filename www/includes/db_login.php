<?php

$DB_host = "localhost";
$DB_user = "root";
$DB_pass = "";
$DB_name = "vitrine";

try {
    $conn = new PDO('mysql:host='.$DB_host.';dbname='.$DB_name, $DB_user, $DB_pass);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    print "Erreur !: " . $e->getMessage() . "<br/>";
    die();
}
?>