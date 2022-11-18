<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"beb_alioua");

	$Num_Tel=$_GET['t1'];
	
	
	
	
	$qry="SELECT MAX(id) as max_id FROM `tickets` WHERE Num_Tel ='$Num_Tel' AND plateforme='Mobile' " ;
	$exec=mysqli_query($conn,$qry);
	
		while ($row = mysqli_fetch_array($exec, MYSQLI_ASSOC)) {
		
		$id_l=$row['max_id'];
		
		
		}
	    echo "$id_l" ;
	




?>