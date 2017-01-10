<?php
	require_once('includes/db_login.php');

    $image = $_POST['image'];
    $name = $_POST['name'];
    $vitrine_id = $_POST['vitrineId'];

    $path = "uploads/$vitrine_id/$name.jpg";

    $sql = "INSERT INTO picture (path) VALUES ('$path')";

    $response = $conn->query($sql);
    if($response){
        if (!is_dir("uploads/$vitrine_id/")) {
            // dir doesn't exist, make it
            mkdir("uploads/$vitrine_id/");
        }

    file_put_contents($path,base64_decode($image, true));
    echo "Successfully Uploaded";
    }
    else{
        echo "Error inserting picture in database";
    }
    //$reponse->closeCursor();

?>
