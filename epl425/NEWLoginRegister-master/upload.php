<?php

$image=$_POST['image'];
$username=$POST['username'];
if(!file_exists($username)){
  $oldmask=umask(0);
  mkdir($username,0744);
}
else {
  if (file_exists("info.txt")) {
    $i=file_get_contents("info.txt");
    $next=$i+1;
    file_put_contents("info.txt",str_replace($i,$next,file_get_contents("info.txt")));
  }
  else {
    $file=fopen("info.txt","w");
    $next=1;
    $fwrite($file,$num);
    fclose($file);
  }
}
$decoded=base64_decode("$image");
$imagename=$username.$next."JPEG";
file_put_contents("$imagename",$decoded);
 ?>
