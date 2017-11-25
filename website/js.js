function searchFunc(){
	suchfeld = document.getElementById("suchfeld").value
};
document.getElementById("suchfeld")
    .addEventListener("keyup", function(event) {
    event.preventDefault();
    if (event.keyCode === 13) {
        document.getElementById("btn-search").click();
    }
});

window.onload(function(event){
	alert(2);});
