<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"beb_alioua");


$reclamation = (isset($_POST['reclamation']) ? $_POST['reclamation'] : '');


$qry="INSERT INTO `reclamation`(`id`, `reclamation`) VALUES (NULL,'$reclamation')";
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