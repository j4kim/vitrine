<?php
    require_once('includes/db_login.php');
    
	$token = $_GET['token'];
	$vitrine_id = $_GET['vitrine_id'];
    
    $sql = "SELECT count(*) FROM subscribe, user where user.token = '$token' and subscribe.fk_vitrine_id = $vitrine_id";
    
    if ($res = $conn->query($sql)) {
        // 1 or 0
        echo $res->fetchColumn();
    }
?>