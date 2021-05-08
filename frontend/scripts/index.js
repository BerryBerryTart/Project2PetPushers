var user = "employee";
function clickSubmit{
    

}



var x = document.getElementsByClassName("login-html");
var y = document.getElementsByClassName("employee-html");
var z = document.getElementsByClassName("financeManager-html");
function onclickEvent(){
    if(user == "employee"){
     alert('STAUS: LOGIN SUCCESSFUL');
    x[0].style.visibility = "hidden";
    y[0].style.visibility = "visible";
    z[0].style.visibility = "hidden";   
    } else if (user == "financeManager"){
        alert('STAUS: REDIRECTING TO MANAGER PORTAL...');
    x[0].style.visibility = "hidden";
    y[0].style.visibility = "hidden";
    z[0].style.visibility = "visible";   
    }else{
        alert('STAUS: LOGIN FAILED!');
    x[0].style.visibility = "visible";
    y[0].style.visibility = "hidden";
    z[0].style.visibility = "hidden";   
    }  
}

/*function signInClick() {
    var ele = document.getElementsByName('gender');
      
    for(i = 0; i < ele.length; i++) {
        if(ele[i].checked)
        document.getElementById("result").innerHTML
                = "Gender: "+ele[i].value;
    }
}
function signUpClick() {
    var ele = document.getElementsByName('gender');
      
    for(i = 0; i < ele.length; i++) {
        if(ele[i].checked)
        document.getElementById("result").innerHTML
                = "Gender: "+ele[i].value;
    }
}*/