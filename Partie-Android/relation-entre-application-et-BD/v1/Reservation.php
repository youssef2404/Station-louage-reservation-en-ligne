<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"beb_alioua");



$station_depart = (isset($_POST['station_depart']) ? $_POST['station_depart'] : '');
$station_arrive = (isset($_POST['station_arrive']) ? $_POST['station_arrive'] : '');
$ville = (isset($_POST['ville']) ? $_POST['ville'] : '');
$date = (isset($_POST['date']) ? $_POST['date'] : '');
$status = (isset($_POST['status']) ? $_POST['status'] : '');
$plateforme = (isset($_POST['plateforme']) ? $_POST['plateforme'] : '');
$Num_Tel = (isset($_POST['Num_Tel']) ? $_POST['Num_Tel'] : '');
$Sms = (isset($_POST['Sms']) ? $_POST['Sms'] : '');
$num_place = (isset($_POST['num_place']) ? $_POST['num_place'] : '');
$louage = (isset($_POST['louage']) ? $_POST['louage'] : '');
$dateReservation = (isset($_POST['dateReservation']) ? $_POST['dateReservation'] : '');


$qry="INSERT INTO `tickets` (`id`, `station_depart`, `station_arrive`, `ville`, `date`, `status`, `dateReservation`, `plateforme`,`Num_Tel`,`Sms`, `num_place`, `louage`)
     VALUES (NULL,'$station_depart','$station_arrive','$ville',NOW(), '$status', '$dateReservation','$plateforme','$Num_Tel','$Sms','$num_place','$louage')";
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