<?php

require "conn_mdp.php";

$id = $_POST["id"];
$username = $_POST["username"];

$sql = "SELECT email,password FROM louagistes WHERE id='$id' AND username LIKE '%username%'";

$result = mysqli_query($conn,$sql);
$response = array();

if(mysqli_nul_rows($result)>0)
{
	$row = mysqli_fetch_assoc($result);
	
	mail($row["email"], "Your account password is ".$row["password"],"Smart Bab Alioua", "safa.touil@esprit.tn");
	echo "SUCCESS";
}
else
	echo" FAILED";
  
mysqli_close($conn);
?>