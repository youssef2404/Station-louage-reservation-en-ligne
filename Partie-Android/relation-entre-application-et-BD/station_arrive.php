<?php
require_once "conn.php";
$sql="select * from louages";
if(!$conn->query($sql)){
	echo"Error in connecting to database";    
}
else {
	$result = $conn->query($sql);
	if($result->num_rows > 0){
		$return_arr['louages'] = array();
		while ($row = $result->fetch_array()){
			array_push($return_arr['louages'], array(
			'id'=>$row['id'],
			'matricule '=>$row['matricule'],
			'zone'=>$row['zone']
			));
		}
		echo json_encode($return_arr);
	}
	
}
				
?>