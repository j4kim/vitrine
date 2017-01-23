<?php
	require_once('includes/db_login.php');

    $image = $_POST['image'];
    $name = $_POST['name'];
    $vitrine_id = $_POST['vitrineId'];

    $path = "uploads/$vitrine_id/$name.jpg";

    $sql = "INSERT INTO picture (path, fk_vitrine_id) VALUES ('$path', $vitrine_id)";

    $response = $conn->query($sql);
    if($response){
        if (!is_dir("uploads/$vitrine_id/")) {
            // dir doesn't exist, make it
            mkdir("uploads/$vitrine_id/");
        }

		$file = base64_decode(chunk_split($image), true);
        
		file_put_contents($path, $file);
        
        echo "Successfully Uploaded";
        
    }
    else{
        echo "Error inserting picture in database";
    }