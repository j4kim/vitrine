<?php
	require_once('includes/db_login.php');

    $username = $_POST['username'];
    $password = md5($_POST['password']);
    $email = $_POST['email'];

    $token = uniqid('',true);

    $sql = "INSERT INTO user (username, password, email, token) VALUES ('$username', '$password', '$email', '$token')";

    try{
        if($conn->exec($sql) == 1){
            echo "User created";
        }
    } 
    catch (PDOException $e) {
        echo "Le nom d'utilisateur est déjà pris<br>";
        echo 'Exception reçue : ',  $e->getMessage(), "\n";
    }