const API_BASE_URL = 'http://localhost:8080';

let userNameEmailText = "";
let passwordText = "";


let submitButton = document.getElementById("submit-button");
let signInSuccessful = document.getElementById("signin-successful");
let dontHaveAccount = document.getElementById("dont-have-account");
let signUpButton = document.getElementById("signup-button");

let userNameValidation = document.getElementById("username-validation");
let passwordValidation = document.getElementById("password-validation");

let userNameEmail = document.getElementById("username-email");
let password = document.getElementById("password");

const urlParams = new URLSearchParams(window.location.search);
let page = urlParams.get("returnUrl");
document.addEventListener("DOMContentLoaded", (event) => {
    
    signUpButton.addEventListener("click", () => {
        window.location.href = `./signup.html?returnUrl=${page}`;
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

    fetch(`${API_BASE_URL}/users/login/${userNameEmailText.toLowerCase()}/${passwordText}`)
    .then(response => response.json())
    .then(data => {

        if(data.email === "Wrong email or Username" || data.username === "Wrong email or Username"){
            userNameValidation.value = "Wrong email or Username";
            userNameValidation.style.color = "red";
            dotLoader.style.display = "none";
            submit.innerText = "Submit"
            enableSubmitButton();
        }
        else if(data.password === "Wrong password"){
            passwordValidation.value = "Wrong password";
            passwordValidation.style.color = "red";
            dotLoader.style.display = "none";
            submit.innerText = "Submit"
            enableSubmitButton();
        }
        else{
            signInSuccessful.innerText = "Successfully logged in";
            setCookie("name", data.name, 3600);
            setCookie("emailId", data.emailId, 3600);
            setCookie("username", data.username, 3600);
            setCookie("token", data.jwtoken, 3600);
            dontHaveAccount.style.pointerEvents = "none";
            signUpButton.style.pointerEvents = "none";

            setTimeout(()=>{
                window.location.href = `./${page}?name=${data.name}`;
            }, 1000);
            
        }
    })
});
userNameEmail.addEventListener("input", (event) => {

    userNameEmailText = event.target.value;

    if(userNameEmailText.length == 0){
        userNameValidation.value = "";
        enableSubmitButton();
        return;
    }
    if((!isValidEmail(userNameEmailText)) && (!isOnlyAlphabetsAndNumbers(userNameEmailText) )){
        userNameValidation.style.color = "red";
        userNameValidation.value = "Not valid username or email";
        enableSubmitButton();
        return; 
    }
    else{
        userNameValidation.value = "";
    }

    passwordValidation.value = "";

    enableSubmitButton();
});

password.addEventListener("input", (event) => {

    passwordText = event.target.value;

    if(passwordText.length === 0){
        passwordValidation.style.color = "red";
        passwordValidation.value = "Password cannot be empty";
        enableSubmitButton();
        return; 
    }
    else{
        passwordValidation.value = "";
    }

    enableSubmitButton();
});



function enableSubmitButton() {
    if(userNameEmailText.length > 0 && userNameValidation.value === "" && passwordValidation.value === "" && passwordText.length > 0){
            submitButton.disabled = false;
            submitButton.style.backgroundColor = "rgb(248, 68, 100)";
    }
    else{
        submitButton.style.backgroundColor = "rgb(246, 143, 162)";
        submitButton.disabled = true;
    }
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

function setCookie(cookieName, value, seconds) {
    document.cookie = `${cookieName}=${encodeURIComponent(value)}; max-age=${seconds}; path=/`;
}
function getCookie(cookieName) {
    const cookies = document.cookie.split("; ");
    for (let c of cookies) {
        const [key, val] = c.split("=");
        if (key === cookieName) return decodeURIComponent(val);
    }
    return 0;
}

