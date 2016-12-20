<?php
	require_once('includes/db_login.php');

	$name = $_POST["name"];
	$radius = $_POST["radius"]+0;
	$color = $_POST["hexColor"];
	$latitude = $_POST["latitude"]+0;
	$longitude = $_POST["longitude"]+0;
	$user = $_POST["user"];
	
	try{
		$req = $conn->prepare("
			INSERT INTO vitrine (name, radius, latitude, longitude, color) VALUES (:name, :radius, :latitude, :longitude, :color);
			insert into subscribe (fk_user_id, fk_vitrine_id) values ((select id from user where username = :user), LAST_INSERT_ID())");
		$result = $req->execute([
			":name" => $name, 
			":radius" => $radius,
			":latitude" => $latitude,
			":longitude" => $longitude,
			":color" => $color,
			":user" => $user
		]);
		if($result)
			echo $conn->lastInsertId();
		else
			echo $req->errorInfo();
	}catch (Exception $e){
		echo($e);
	}	
?>