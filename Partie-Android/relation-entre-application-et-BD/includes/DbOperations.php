<?php
   
    class DbOperations{
		private $con;
		
		function __construct(){
			
			require_once dirname(__FILE__).'/DbConnect.php';
			
			$db = new DbConnect();
			
			$this->con = $db->connect();                                               
			
		}
		
		/*CRUD*/
		
	
	
	/* ------------------ louagiste ------------------ */
	
	public function userLogin($username, $password){
		/*$password = $pass;*/
		
		$stmt = $this->con->prepare("SELECT id from louagistes WHERE username = ? AND password = ?");
		$stmt-> bind_param("ss",$username,$password);
		$stmt ->execute();
		$stmt ->store_result();
		return $stmt->num_rows > 0;
		
	}
	
	public function getUserByUsername($username){
		$stmt = $this->con->prepare("SELECT * from louagistes WHERE username = ?");
		$stmt->bind_param("s", $username);
		$stmt ->execute();
		return $stmt->get_result()->fetch_assoc();
		
	}
	
	/* ------------------ louagiste ------------------ */
	public function agentLogin($username, $password){
		/*$password = $pass;*/
		
		$stmt = $this->con->prepare("SELECT id from agents WHERE username = ? AND password = ?");
		$stmt-> bind_param("ss",$username,$password);
		$stmt ->execute();
		$stmt ->store_result();
		return $stmt->num_rows > 0;
		
	}
	
	}
	/*------------------Tickets-------------*/
	function createTicket($ville){
		
			$stmt = $this->con->prepare("INSERT INTO `tickets` (`ville`) VALUES ( '$ville' );");
			$stmt->bind_param("s",$ville);
			
			if($stmt->execute()){
				return true;
			}else {
				return false;
			}
			
		}
		
		function createUser($username,$pass,$email){
			$password = md5($pass);
			$stmt = $this->con->prepare("INSERT INTO `user` (`id`, `username`, 
			       `password`, `email`) VALUES (NULL, ? , ? , ?);");
			$stmt->bind_param("sss",$username,$password,$email);
			
			if($stmt->execute()){
				return true;
			}else {
				return false;
			}
			
		}
		
		function createGps($uniqueId,$latitude,$longitude){
			
			$stmt = $this->con->prepare("INSERT INTO `realtimedb` ( `uniqueId`,`latitude`, 
			       `longitude`) VALUES ( NULL,? , ? );");
			$stmt->bind_param("sss",$uniqueId,$latitude,$longitude);
			
			if($stmt->execute()){
				return true;
			}else {
				return false;
			}
			
		}
?>
