<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"beb_alioua");

	$id=$_GET['t1'];
	
	
	
	
	$qry="SELECT prix FROM `louages` WHERE id ='$id' AND zone='$zone'" ;
	$exec=mysqli_query($conn,$qry);
	
		while ($row = mysqli_fetch_array($exec, MYSQLI_ASSOC)) {
		
		$id_l=$row['id'];
		$zone=$row['zone'];
		$prix=$row['prix'];
		
		}
	    echo "$email &$prix |$zone";
	




?>