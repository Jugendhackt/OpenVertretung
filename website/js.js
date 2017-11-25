window.onload = function(){
    alert("Onload");
    //your http post request goes here
};

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
