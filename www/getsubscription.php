<?php
	require_once('includes/tools.php');

	$token = $_GET['token'];

	$sql = "
	SELECT vitrine.* 
	FROM vitrine, subscribe, user 
	WHERE subscribe.fk_vitrine_id = vitrine.id and user.id = subscribe.fk_user_id and user.token LIKE '$token'";

    echoVitrines($sql);
?>