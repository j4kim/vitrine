<?php
    require_once('includes/db_login.php');
    
	$token = $_GET['token'];
	$vitrine_id = $_GET['vitrine_id'];
    
    $sql = "SELECT count(*) FROM subscribe where fk_user_id = (select id from user where token = '$token') and fk_vitrine_id = $vitrine_id ORDER BY `id` ASC";
    
    if ($res = $conn->query($sql)) {
        // 1 or 0
        echo $res->fetchColumn();
    }
?>