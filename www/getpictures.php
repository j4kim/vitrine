<?php
	require_once('includes/db_login.php');

	$vitrine_id = $_GET['vitrine_id'];

	$sql = "SELECT * FROM picture WHERE fk_vitrine_id LIKE '".$vitrine_id."'";

	$reponse = $conn->query($sql);

	echo ("{ \"pictures\":[");
	while ($donnees = $reponse->fetch(PDO::FETCH_ASSOC))
	{
		echo (json_encode($donnees));
		echo (",");
	}
	echo ("]}");

	$reponse->closeCursor();
?>