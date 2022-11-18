<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"beb_alioua");

	$uniqueId=$_GET['t1'];
	
	
	
	
	$qry="SELECT * FROM `realtimedb` WHERE uniqueId ='$uniqueId'" ;
	$exec=mysqli_query($conn,$qry);
	
		while ($row = mysqli_fetch_array($exec, MYSQLI_ASSOC)) {
		
		$uniqueId=$row['uniqueId'];
		$latitude=$row['latitude'];
		$longitude=$row['longitude'];
		}
	    echo "$latitude &$longitude" ;

?>