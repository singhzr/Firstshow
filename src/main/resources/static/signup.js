const API_BASE_URL = 'http://localhost:8080';


let userNameText = "";
let nameofuserText = "";
let emailText = "";
let passwordText = "";

let submitButton = document.getElementById("submit-button");
let signUpSuccessful = document.getElementById("signup-successful");
let alreadySignedUp = document.getElementById("already-signed-up");
let loginButton = document.getElementById("login-button");

let userNameValidation = document.getElementById("username-validation");
let nameOfUserValidation = document.getElementById("name-validation");
let emailValidation = document.getElementById("email-validation");
let passwordStrength = document.getElementById("password-strength");

let userName = document.getElementById("username");
let nameofuser = document.getElementById("name");
let email = document.getElementById("email");
let password = document.getElementById("password");

const urlParams = new URLSearchParams(window.location.search);
let page = urlParams.get("returnUrl");
document.addEventListener("DOMContentLoaded", (event) => {

    fetch(`${API_BASE_URL}/users/loadUser`);

    loginButton.addEventListener("click", () => {
        window.location.href = `./login.html?returnUrl=${page}`;
    });
    event.preventDefault();
});
submitButton.addEventListener("click", (event) => {

    const submit = document.getElementById('submit-button');
    submit.innerText = "";
    const dotLoader = document.createElement('div');
    const dotLoaderInner1 = document.createElement('div');
    const dotLoaderInner2 = document.createElement('div');
    const dotLoaderInner3 = document.createElement('div');

    dotLoader.appendChild(dotLoaderInner1);
    dotLoader.appendChild(dotLoaderInner2);
    dotLoader.appendChild(dotLoaderInner3);

    dotLoader.className = "dot-loader";

    submit.appendChild(dotLoader);

    event.preventDefault();

    fetch(`${API_BASE_URL}/users/addUser`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            userName: userNameText.toLowerCase(),
            name: nameofuserText,
            emailId: emailText,
            password: passwordText
        })
    })
    .then(response => response.text())
    .then(data => {
        if(data === "Email already exist"){
            emailValidation.value = data;
            emailValidation.style.color = "red";
            dotLoader.style.display = "none";
            submit.innerText = "Submit"
        }
        else{
            const urlParams = new URLSearchParams(window.location.search);
            let page = urlParams.get("returnUrl");
            alreadySignedUp.style.pointerEvents = "none";
            loginButton.style.pointerEvents = "none";
            signUpSuccessful.innerText = "Signup successful continue bookings";
            emailValidation.value = "";
            userNameValidation.value = "";
            nameOfUserValidation.value = "";
            passwordStrength.value = "";
            
            setTimeout(()=>{
                window.location.href = `./login.html?returnUrl=${page}`;
            }, 1000)
            
            
        }
    })
});
userName.addEventListener("input", (event) => {

    userNameText = event.target.value;

    if(userNameText.length == 0){
        userNameValidation.value = "";
        enableSubmitButton();
        return;
    }
    if(!isOnlyAlphabetsAndNumbers(userNameText)){
        userNameValidation.style.color = "red";
        userNameValidation.value = "Not valid";
        enableSubmitButton();
        return; 
    }
    fetch(`${API_BASE_URL}/users/validateUser/${userNameText.toLowerCase()}`)
    .then(response => response.text()) 
    .then(data => {
        userNameValidation.value = data;
        if(data.length >= 10){
            userNameValidation.style.color = "red";
        }
        else{
            userNameValidation.style.color = "rgb(47, 206, 95)";
        }
        enableSubmitButton();
    });
});

email.addEventListener("input", (event) => {

    emailText = event.target.value;

    if(emailText.length == 0){
        emailValidation.value = "";
        enableSubmitButton();
        return;
    }

    if(!isValidEmail(emailText)){
        emailValidation.style.color = "red";
        emailValidation.value = "Not a valid email address";
    }
    else{
        emailValidation.value = "";
    }
    enableSubmitButton();
});

nameofuser.addEventListener("input", (event) => {

    nameofuserText = event.target.value;

    if(nameofuserText.length == 0){
        nameOfUserValidation.value = "";
        enableSubmitButton();
        return;
    }

    if(!isOnlyAlphabets(nameofuserText)){
        nameOfUserValidation.style.color = "red";
        nameOfUserValidation.value = "Name should only contain letters";
    }
    else{
        nameOfUserValidation.value = "";
    }
    enableSubmitButton();
});

password.addEventListener("input", (event) => {

    passwordText = event.target.value;

    if(passwordText.length == 0){
        passwordStrength.value = "";
        enableSubmitButton();
        return;
    }

    if(!validPassword(passwordText)){
        passwordStrength.style.color = "red";
        passwordStrength.value = "Weak password";
    }
    else{
        passwordStrength.style.color = "rgb(47, 206, 95)";
        passwordStrength.value = "Strong password";
    }
    enableSubmitButton();
});


function enableSubmitButton() {
    if(userNameValidation.value === "available" && nameOfUserValidation.value.length === 0 && 
        emailValidation.value.length === 0 && passwordStrength.value === "Strong password" && 
        passwordText.length >= 6 && userNameText.length >= 1 && emailText.length >= 1 && 
        nameofuserText.length >= 1){

            submitButton.disabled = false;
            submitButton.style.backgroundColor = "rgb(248, 68, 100)";
    }
    else{
        submitButton.style.backgroundColor = "rgb(246, 143, 162)";
        submitButton.disabled = true;
    }
}
function isOnlyAlphabets(str) {
    
    for (let i = 0; i < str.length; i++) {

        if(str[i] == " "){
            continue;
        }
        const code = str.charCodeAt(i);

        if (!(code >= 65 && code <= 90) && !(code >= 97 && code <= 122)) {
            return false;
        }
    }
    return true;
}

function isOnlyAlphabetsAndNumbers(input) {
    if (typeof input !== 'string' || input.length === 0) return false;
  
    for (let i = 0; i < input.length; i++) {
      const code = input.charCodeAt(i);
      const isUpper = code >= 65 && code <= 90;
      const isLower = code >= 97 && code <= 122;
      const isDigit = code >= 48 && code <= 57;
      if (!isUpper && !isLower && !isDigit) return false;
    }
  
    return true;
  }
  
function validPassword(password) {
    let hasLower = false;
    let hasUpper = false;
    let hasNumber = false;
    let hasSpecial = false;

    for (let i = 0; i < password.length; i++) {
        const char = password[i];
        const code = char.charCodeAt(0);

        if (code >= 97 && code <= 122) hasLower = true;
        else if (code >= 65 && code <= 90) hasUpper = true;
        else if (code >= 48 && code <= 57) hasNumber = true;
        else hasSpecial = true;
    }

    return password.length >= 6 && hasLower && hasUpper && hasNumber && hasSpecial;
}

function isValidEmail(email) {
    if (!email.includes("@")) return false;

    let parts = email.split("@");
    if (parts.length !== 2) return false;

    let local = parts[0];
    let domain = parts[1];

    if (local.length === 0 || domain.length < 3) return false;
    if (!domain.includes(".")) return false;

    let domainParts = domain.split(".");
    if (domainParts.some(part => part.length === 0)) return false;

    return true;
}
