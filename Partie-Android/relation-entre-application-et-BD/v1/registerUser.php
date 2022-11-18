<?php
    
require_once '../includes/DbOperations.php'	;
$response = array();
	
if($_SERVER['REQUEST_METHOD']=='POST'){
	
	if(
	   isset($_POST['username']) and
	    isset($_POST['password']) and
		 isset($_POST['email']))
	   {
		   $db = new DbOperations();
		   if($db->createUser(
		        $_POST['username'],
				$_POST['password'],
				$_POST['email']
				))
				{
					$response['error'] = false;
					$response['message']="User registred successfully";
				}else{
					$response['error'] = true;
					$response['message']="Try again";
				}
			 
	}else{
		$response['error']=true;
		$response['message']="Required fields are missing";
	}
}else{
	$response['error'] =true;
	$response['message'] ="Invalid Request";
}

echo json_encode($response);
?>