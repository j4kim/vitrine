<?php
	require_once('includes/db_login.php');

	$vitrine_id = $_GET['vitrine_id'];

	$sql = "SELECT * FROM picture WHERE fk_vitrine_id LIKE '".$vitrine_id."'";

	$reponse = $conn->query($sql);
    
    $pictures = [];
    while($pic = $reponse->fetch(PDO::FETCH_ASSOC)){
        $pictures[] = $pic;
    }
    echo json_encode(["pictures" => $pictures]);
    

	$reponse->closeCursor();
?>