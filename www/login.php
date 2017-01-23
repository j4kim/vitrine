<?php
	require_once('includes/db_login.php');

	$username = $_GET['username'];
	$password = md5($_GET['password']);

	$sql = "SELECT username, email, token FROM user WHERE username LIKE '".$username."' AND password LIKE '".$password."'";

	$reponse = $conn->query($sql);

	while ($donnees = $reponse->fetch(PDO::FETCH_ASSOC))
	{
		echo (json_encode($donnees));
	}

	$reponse->closeCursor();
?>