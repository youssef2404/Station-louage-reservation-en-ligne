<?php

header('content-type: application/json');
$con = mysqli_connect("localhost", "root", "", "beb_alioua");

$query ="select * from louagistes ";
$res=mysqli_query($con,$query);

$json_data = array();
while ($row = mysqli_fetch_assoc($res)){
	$json_data[] = $row;
}
echo json_encode($json_data);

?>