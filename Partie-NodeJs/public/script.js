function sendtodb(){

	
	var y=40;
	var depart= document.getElementById("depart").value;

	var ville= document.getElementById("ville").value;

	var arrivee= document.getElementById("arrivee").value;


	var ladate=new Date();
var mounth=ladate.getMonth() + 1;

var chaine3 =ladate.getDate()+"-"+mounth+"-"+ladate.getFullYear();



	
 var xhttp = new XMLHttpRequest();

xhttp.open("GET", `http://192.168.112.149:3001/addAlert/${depart}/${arrivee}/${ville}/${chaine3}`, true);

	 xhttp.send();
 xhttp.open("GET",`http://192.168.112.149:3001/dernierticket/${y}`, true);
    xhttp.send();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
		 var data = JSON.parse(this.responseText);
		 for(var a = 0; a < data.length; a++) {
                 var id = data[a].id;
                 var date = data[a].date;}
		console.log(id);
console.log(date);

		 
		document.getElementById("date").value=date;
	    }}


 
	


}
 function printDiv() {
            var divContents = document.getElementById("qr").innerHTML;
            var a = window.open('', '', 'height=500, width=500');
            a.document.write('<html>');
            a.document.write('<body > <h1>باب عليوة الذكية <br>');
            a.document.write(divContents);
            a.document.write('</body></html>');
            a.document.close();
            a.print();
			location.href = "interface.html";
        }

