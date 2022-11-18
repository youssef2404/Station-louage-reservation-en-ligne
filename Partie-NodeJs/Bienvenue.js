const mysql = require('mysql2');
const express = require('express')
const app= express()
const port=3001

const pool = mysql.createPool({
	host: '192.168.112.232',
	user: 'youssef',
	password: '',
	database: 'beb_alioua',
	connectionLimit: 10,
	queueLimit: 0,
	waitForConnections: true,
	timezone: 'Z'

});

app.use(express.static('public'))
app.get('',(req,res)=>{
	res.sendFile(__dirname+'/public/interface.html')
	})

app.get('/addAlert/:depart/:arrivee/:ville/:dateReservation', function(req,res){

var request = "INSERT INTO `tickets` (`id`,`station_depart`,`station_arrive`,`ville`,`Num_Tel`,`Sms`,`plateforme`,`num_place`,`louage`,`date`,`status`,`dateReservation`) VALUES (NULL,?,?,?,'0',false,'web','0','0', CURRENT_TIMESTAMP,false,?)";

	pool.query(request,
[req.params.depart,req.params.arrivee,req.params.ville,req.params.dateReservation],
		function(err,results){
			if(err)
				res.json(err);
			else
				res.json(results);


		})
})

app.get('/:id', function(req,res){
var request = "SELECT * FROM tickets WHERE id=?";

	pool.query(request,
[req.params.id],
		function(err,results){
			if(err)
				res.json(err);
			else
				res.json(results);


		})
})
app.get('/dernierticket/:id', function(req,res){
var request = "SELECT * FROM tickets WHERE plateforme='web' ORDER BY id DESC LIMIT 0, 1 ";

	pool.query(request,
[req.params.id],
		function(err,results){
			if(err)
				res.json(err);
			else
				res.json(results);


		})
})
app.get('/maxid/:id', function(req,res){
var request = "SELECT MAX(id) FROM tickets WHERE plateforme='web' ";

	pool.query(request,
[req.params.id],
		function(err,results){
			if(err)
				res.json(err);
			else
				res.json(results);


		})
})




app.listen(port)
