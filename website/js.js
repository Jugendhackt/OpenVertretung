window.onload = function(){

	Z = [
	{"Lehrer":"Herr osca","Fach":"NT", "Art": "String", "Vertretungsplan": 1,  "StundeVon": 1,"StundeBis": 2, "Kommentar": "lol", "Raum": "33"},
	{"Lehrer":"Marco","Fach":"Informatik", "Art": "String", "Vertretungsplan": 2,  "StundeVon": 4,"StundeBis": 1, "Kommentar": "Krank", "Raum": "007"}]
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
		besonderheiten = Z[i].Kommentar
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
	};


function openschool(){
	//openscho1	= document.getElementById("openschoo1").value
	document.getElementById('tabelle').classList.toggle('collapsed');
}