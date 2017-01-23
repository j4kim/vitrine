<?php
    require_once('includes/db_login.php');
    
	$token = $_GET['token'];
	$vitrine_id = $_GET['vitrine_id'];
    
    $sql = "INSERT INTO subscribe (fk_user_id, fk_vitrine_id) VALUES ((select id from user where token = '$token'), $vitrine_id)";
    
    try{
        if ($conn->exec($sql) == 1) {
            echo "Successfully subscribed";
        }else{
            echo "Error subscribing";
        }
    } catch (Exception $e) {
        echo "User has already subscribed to this Vitrine";
    }
?>