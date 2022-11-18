<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"beb_alioua");

	
	$username=$_GET['t1'];
	$password=$_GET['t2'];
	
	
	
	$qry="SELECT id FROM `louagistes` WHERE username = '$username' AND password = '$password'" ;
	$exec=mysqli_query($conn,$qry);
	
		while ($row = mysqli_fetch_array($exec, MYSQLI_ASSOC)) {
		
		$id_l=$row['id'];
		
		}
	    echo "$id_l"
	




?>