window.onload = function(){
	
	
 
   //alert("Onload");
		document.getElementById("suchfeld")
		.addEventListener("keyup", function(event) {
			event.preventDefault();
			if (event.keyCode === 13) {
				document.getElementById("btn-search").click();
			}
		});

};	

function searchFunc(){
	suchfeld = document.getElementById("suchfeld").value
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	document.getElementById("olo").innerHTML = ""
	
	
	 // AJAX nutzen mit IE7+, Chrome, Firefox, Safari, Opera
  xmlhttp=new XMLHttpRequest();
 
 xmlhttp.onreadystatechange=function()
 {
	
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
  {
   ude1 = xmlhttp.responseText;
   Z = JSON.parse(ude1);
   	for(i=0;i<Z.length;i++){
		stunde = "?"
		fach = "?"
		raum = "?"
		lehrer = "?"
		besonderheiten = "?"
		stundeVon = "?"
		stundeBis = "?"
		
		
		fach = Z[i].Fach
		raum = Z[i].Raum
		lehrer = Z[i].Lehrer
		besonderheiten = Z[i].Kommentar+","+Z[i].Art
		stundeVon = Z[i].StundeVon
		stundeBis = Z[i].StundeBis
	
		
		
		if (stundeVon==stundeBis){
			stunde=stundeVon
		}
		else{
			if(stundeVon<stundeBis)
				stunde=stundeVon+"-"+stundeBis
			else{
				stunde=stundeBis+"-"+stundeVon
			}
		}
		
		zeilen = document.getElementById("olo")
		zeilen.innerHTML  += "	<tr><td>"+stunde+"</td><td>"+fach+"</td><td>"+raum+"</td><td>"+lehrer+"</td><td>"+besonderheiten+"</td></tr>";	
			
		
		
	}
  }
 }
 //////////////////////////////////////////////////////
 /////////////////////////////////////////////////////
 //ServerLink = "http://10.23.41.176:8001/test"
 ServerLink = "./ausfall.json"
 //////////////////////////////////////////////////////
 /////////////////////////////////////////////////////
 xmlhttp.open("POST",ServerLink,true);
 xmlhttp.send('["readVertretungsplan", {"Vertretungsplan":1,"Benutzername":"Blumentopf","Passwort":"42424242"}]');
	};


function openschool(){
	//openscho1	= document.getElementById("openschoo1").value
	document.getElementById('tabelle').classList.toggle('collapsed');
}