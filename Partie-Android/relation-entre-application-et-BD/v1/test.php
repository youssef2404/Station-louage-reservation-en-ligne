<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"beb_alioua");


$latitude = (isset($_POST['latitude']) ? $_POST['latitude'] : '');
$longitude = (isset($_POST['longitude']) ? $_POST['longitude'] : '');


$qry="INSERT INTO `realtimedb`(`uniqueId`, `latitude`, `longitude`) VALUES (NULL,'$latitude', '$longitude')";
mysqli_query($conn,$qry);
 if ($conn->query($qry) === TRUE) 
	   {
           echo "New record created successfully";
       } 
     else 
        {
           echo "Error: " . $qry . "<br>" . $conn->error;
        }




?>