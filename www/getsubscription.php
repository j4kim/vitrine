<?php
	require_once('includes/db_login.php');

	$token = $_GET['token'];

	$sql = "
	SELECT vitrine.id, vitrine.name, vitrine.radius, vitrine.latitude, vitrine.longitude, vitrine.lastPostDate 
	FROM vitrine
		INNER JOIN subscribe 
			ON subscribe.fk_vitrine_id = vitrine.id
		INNER JOIN user 
			ON user.id = subscribe.fk_user_id 
	WHERE user.token LIKE '$token'";

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