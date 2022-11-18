<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"beb_alioua");

    $id_louage;
	$nbPlace_reserve;
	$nbPlace_possible_louage;
	
	$id=$_GET['t1'];
	$zone=$_GET['t2'];
	
	$qry="SELECT * FROM tickets WHERE id ='$id' AND status = 0";
	
	$exec=mysqli_query($conn,$qry);
	if(mysqli_num_rows($exec)>0)
	{
		$qry2="UPDATE tickets SET status = true WHERE id = '$id'";
		$exec2=mysqli_query($conn,$qry2);
		$qry3="SELECT id FROM louages WHERE zone='$zone' AND nbPlace_possible > 0 LIMIT 0, 1";
		$exec3=mysqli_query($conn,$qry3);
	
		while ($row = mysqli_fetch_array($exec3, MYSQLI_ASSOC)) {
		
		$id_louage=$row['id'];
		
		}
		$qry4="SELECT nbPlace_possible FROM louages WHERE id='$id_louage'";
		$exec4=mysqli_query($conn,$qry4);
		while ($row2 = mysqli_fetch_array($exec4, MYSQLI_ASSOC)) {
		
		$nbPlace_possible_louage=$row2['nbPlace_possible'];
		$nbPlace_possible_louage--;
		$qry5="UPDATE louages SET nbPlace_possible='$nbPlace_possible_louage' WHERE id='$id_louage'";
		$exec5=mysqli_query($conn,$qry5);
		
		}
		$qry6="SELECT nbPlace_reserve FROM louages WHERE id='$id_louage'";
		$exec6=mysqli_query($conn,$qry6);
		while ($row3 = mysqli_fetch_array($exec6, MYSQLI_ASSOC)) {
		
		$nbPlace_reserve=$row3['nbPlace_reserve'];
		$nbPlace_reserve++;
		$qry7="UPDATE louages SET nbPlace_reserve='$nbPlace_reserve' WHERE id='$id_louage'";
		$exec7=mysqli_query($conn,$qry7);
		
			
			
		}
	    $qry8="UPDATE tickets SET num_place = '$nbPlace_reserve' ,louage = '$id_louage' WHERE id = '$id'";
		$exec8=mysqli_query($conn,$qry8);
		echo "votre place est $nbPlace_reserve dans la louage $id_louage vers $zone Bon voyage";
	}
	else
	{
		echo "ticket n'existe pas";
	}
	

	
	




?>