<?php
	require_once('includes/db_login.php');

	$token = $_GET['token'];

	$sql = "
	SELECT vitrine.* 
	FROM vitrine, subscribe, user 
	WHERE subscribe.fk_vitrine_id = vitrine.id and user.id = subscribe.fk_user_id and user.token LIKE '$token'";

	try
	{
		$reponse = $conn->query($sql);
	}
	catch (PDOException $e)
	{
		print($e);
	}

	echo ("{ \"vitrines\":[");
	while ($donnees = $reponse->fetch(PDO::FETCH_ASSOC))
	{
		//Create json object
		echo (json_encode($donnees));
		echo (",");
	}
	echo ("]}");

	$reponse->closeCursor();
?>