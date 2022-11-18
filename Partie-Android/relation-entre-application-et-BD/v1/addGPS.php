<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"beb_alioua");


$latitude = (isset($_POST['latitude']) ? $_POST['latitude'] : '');
$longitude = (isset($_POST['longitude']) ? $_POST['longitude'] : '');



$qry="UPDATE `realtimedb` SET `latitude`='$latitude',`longitude`='$longitude' WHERE uniqueId =7";
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