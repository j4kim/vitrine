<?php
    require_once('includes/db_login.php');
    
	$token = $_GET['token'];
	$vitrine_id = $_GET['vitrine_id'];
    
    $sql = "delete FROM subscribe where fk_user_id = (select id from user where token = '$token') and fk_vitrine_id = $vitrine_id";
    
    if ($conn->exec($sql) == 1) {
        echo "Successfully unsubscribed";
    }else{
        echo "User is not subscribed to this Vitrine";
    }
?>