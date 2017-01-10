<?php
	require_once('includes/db_login.php');

    $image = $_POST['image'];
    $name = $_POST['name'];
    $vitrine_id = $_POST['vitrineId'];

    $path = "uploads/$vitrineId/$name.png";

    $sql = "INSERT INTO picture (path) VALUES ('$path')";

    $response = $conn->query($sql);
    if($response){
    file_put_contents($path,base64_decode($image));
    echo "Successfully Uploaded";
    }
    else{
        echo "Error inserting picture in database";
    }
    $reponse->closeCursor();

?>
