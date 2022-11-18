<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"beb_alioua");

	$id=$_GET['t1'];
	
	
	
	
	$qry="SELECT * FROM `louagistes` WHERE id ='$id'" ;
	$exec=mysqli_query($conn,$qry);
	
		while ($row = mysqli_fetch_array($exec, MYSQLI_ASSOC)) {
		
		$id_l=$row['id'];
		$email=$row['email'];
		$username=$row['username'];
		$password=$row['password'];
		$nom=$row['nom'];
		$prenom=$row['prenom'];
		$numTel=$row['numTel'];
		
		}
	    echo "$email &$username |$password !$nom $prenom #$numTel" ;
	




?>