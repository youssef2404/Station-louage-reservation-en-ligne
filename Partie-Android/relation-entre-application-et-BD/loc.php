<?php
require_once "conn.php";
$sql="select * from realtimedb";
if(!$conn->query($sql)){
	echo"Error in connecting to database";    
}
else {
	$result = $conn->query($sql);
	if($result->num_rows > 0){
		$return_arr['realtimedb'] = array();
		while ($row = $result->fetch_array()){
			array_push($return_arr['realtimedb'], array(
			'uniqueId'=>$row['uniqueId'],
			'latitude '=>$row['latitude'],
			'longitude'=>$row['longitude']
			));
		}
		
		echo "$latitude &$longitude" ;
		echo json_encode($return_arr);
	}
	
}
				
?>