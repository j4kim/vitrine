<?php
    function echoVitrines($sql){
        require_once('includes/db_login.php');
        
        try{
            $reponse = $conn->query($sql);
    
            $vitrines = [];
            while($vitrine = $reponse->fetch(PDO::FETCH_ASSOC)){
                $vitrines[] = $vitrine;
            }
            echo json_encode(["vitrines" => $vitrines]);
            
            $reponse->closeCursor();
        }
        catch (PDOException $e){
            print($e);
            die();
        }
    }

?>