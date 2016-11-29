<?php

    define('HOST','localhost');
    define('USER','root');
    define('PASS','9e50f75d2e');
    define('DB','mysql');

    $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
    $username = $_POST["username"];

    $statement = mysqli_prepare($con, "SELECT * FROM images WHERE username = ?");
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $image, $id, $username);


    //$filename_path = md5(time().uniqid()).".jpg";
     $decoded=base64_decode($image);
     //file_put_contents("uploads/".$filename_path,$decoded);


    $response = array();
    $response["success"] = false;

    /*while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["name"] = $name;
        $response["age"] = $age;
        $response["username"] = $username;
        $response["password"] = $password;
    }*/
    echo <img src=$decoded alt="Smiley face" height="150" width="150">;
    //echo json_encode($response);
?>
