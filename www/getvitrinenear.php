<?php
	require_once('includes/tools.php');

	$lat = $_GET['lat'];
	$long = $_GET['long'];

	$sql ="SELECT *, ( 6371 * acos ( cos ( radians(".$lat.") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(".$long.") ) + sin ( radians(".$lat.") ) * sin( radians( latitude ) ) ) ) AS distance
		FROM vitrine
		ORDER BY distance
		LIMIT 0 , 20";
	// ON PEUT AJOUTER HAVING distance < X (en km)
		
    echoVitrines($sql);
?>