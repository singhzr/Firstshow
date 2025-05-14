const API_BASE_URL = 'http://localhost:8080';


let totalPrice = sessionStorage.getItem("totalPrice");
let token = getCookie("token");
let sessionId = getCookie("sessionId");

document.addEventListener("DOMContentLoaded", function (event) {

    let showDetails = JSON.parse(sessionStorage.getItem("showDetails"));

    generateCard(showDetails.currentShowIdObj);
});
function generateCard(showId) {
    
    let cleanedSeats = JSON.parse(sessionStorage.getItem("clickedSeats"));

    const ticketCard = document.querySelector(".card");

    let pItems = cleanedSeats.filter(seat => seat.includes("(P)")).map(seat => seat.replace("(P)", ""));
    let cItems = cleanedSeats.filter(seat => seat.includes("(C)")).map(seat => seat.replace("(C)", ""));
    
    const divFirst = document.createElement("div");
    const divSecond = document.createElement("div");
    const divSecondA = document.createElement("div");
    const divSecondB = document.createElement("div");  
    const divSecondBone = document.createElement("div");
    const divSecondBtwo = document.createElement("div");
    const divThird = document.createElement("div");
    const divFour = document.createElement("div");
    const divFive = document.createElement("div");
    const divFiveOne = document.createElement("div");
    const divFiveTwo = document.createElement("div");
    const divFiveTwoA = document.createElement("div");
    const divFiveTwoB = document.createElement("div");
    const timer = document.createElement("div");
    timer.className = "timer";

    let classicSeatsText = cItems.join(" | ");
    const premiumSeatsText = pItems.join(" | ");

    divFirst.className = "div-first";
    divSecond.className = "div-second";
    divThird.className = "div-third"
    divSecondA.className = "div-second-a";
    divSecondB.className = "div-second-b";
    divSecondBone.className = "div-second-b-one";
    divSecondBtwo.className = "div-second-b-two";
    divFour.className = "div-four";
    divFive.className = "div-five";
    divFiveOne.className = "div-five-one";
    divFiveTwo.className = "div-five-two";
    divFiveTwoA.className = "div-five-two-a";
    divFiveTwoB.className = "div-five-two-b";

    divFirst.innerText = "Booking Summary";

    divFiveOne.innerText = "By proceeding, I express my consent to complete this transaction.";

    divFiveTwoA.innerText = "Total Amount: ₹"+totalPrice;
    divFiveTwoB.innerText = "Proceed";

    divFiveTwoB.addEventListener("click", ()=>{
        generateTicket(showId);
    });
    
    if(window.innerWidth < 1101){
        divFive.style.setProperty('--marginBottom', `10vh`);
    }

    if(window.innerHeight > 900){
        ticketCard.style.setProperty('--marginLeftCard', `30vw`);
        divFive.style.setProperty('--marginBottom', `65vh`);
        divFive.style.setProperty('--marginLeftDivFive', `30.4vw`);
    }

    if(pItems.length > 0) {
        const divPremiumText = document.createElement("div");
        const divPremiumTicketText = document.createElement("div");
        divPremiumText.className = "div-premium-text";
        divPremiumTicketText.className = "div-premium-ticket-text";

        divPremiumText.textContent = "Premium: ";
        divPremiumTicketText.textContent = premiumSeatsText;

        divSecondBone.appendChild(divPremiumText);
        divSecondBone.appendChild(divPremiumTicketText);
        divSecondB.appendChild(divSecondBone);
    }
    if(cItems.length > 0) {
        const divClassicText = document.createElement("div");
        const divClassicTicketText = document.createElement("div");
        divClassicText.className = "div-classic-text";
        divClassicTicketText.className = "div-classic-ticket-text";

        divClassicText.textContent = "Classic: ";
        divClassicTicketText.textContent = classicSeatsText;

        divSecondBtwo.appendChild(divClassicText);
        divSecondBtwo.appendChild(divClassicTicketText);
        divSecondB.appendChild(divSecondBtwo);
    }

    const totalAmountDiv = document.createElement("div");
    totalAmountDiv.className = "ticket-total-amount";
    totalAmountDiv.textContent = `Amount Payable : ₹${totalPrice}`;

    divSecond.appendChild(divSecondA);
    divSecond.appendChild(divSecondB);

    divFour.appendChild(totalAmountDiv);

    ticketCard.appendChild(divFirst);
    ticketCard.appendChild(divSecond);
    ticketCard.appendChild(divThird);
    ticketCard.appendChild(divFour);
    ticketCard.appendChild(timer);

    divFive.appendChild(divFiveOne);
    divFiveTwo.appendChild(divFiveTwoA);
    divFiveTwo.appendChild(divFiveTwoB);
    divFive.appendChild(divFiveTwo);
    document.body.appendChild(divFive);

    executeTime();
}
function generateTicket(showIdNow) {

    if(!token){
        window.location.href = `./login.html?returnUrl=confirmation.html`;
        return;
    }

    let cleanedSeats = JSON.parse(sessionStorage.getItem("clickedSeats"));

    if (cleanedSeats.length > 0) {

        someErrorOccurred("Next Page");

        setTimeout(()=>{

            fetch(`${API_BASE_URL}/tickets/bookTicket`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                    'Session-ID': sessionId 
                },
                body: JSON.stringify({
                    showId: showIdNow,
                    seatList: cleanedSeats,
                    foodAttached: 0, 
                    emailId: getCookie("emailId")
                })
            })
            
            .then(response => response.json())
            .then(ticketId => {

                if(ticketId < 0){
                someErrorOccurred("Some error ocuured. Select seats again.");  
                
                }
                else if(ticketId > 0){
                    window.location.href = `./ticket.html?ticketId=${ticketId}`;
                }
                else{
                    document.cookie = "name=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
                    document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
                    document.cookie = "emailId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
                    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
                    setCookie('sessionId', generateSessionId(), 3600);
                    someErrorOccurred("JWT expired");
                }
            });
        }, 1000);
    }
}

function executeTime(){
    const timerElement = document.querySelector(".timer");

    const intervalId = setInterval(() => {

        const secondsLeft = getRemainingTime();

        if (secondsLeft <= 0) {
            clearInterval(intervalId);
            sessionStorage.removeItem("countdownExpires"); 
            someErrorOccurred("Select seats again.");
            return;
        }
        const minutes = Math.floor(secondsLeft / 60);
        const seconds = secondsLeft % 60;

        timerElement.textContent = `Time remaining to book : ${minutes}:${seconds.toString().padStart(2, '0')}`;
        timerElement.style.color = "rgb(248, 68, 100)";
    }, 0);
}

function getRemainingTime() {
    const expiresAt = parseInt(sessionStorage.getItem("countdownExpires"), 10);

    const remainingMs = expiresAt - Date.now();
    return Math.max(0, Math.floor(remainingMs / 1000)); 
}

function someErrorOccurred(error){

    document.body.innerHTML = null; 

    const dotLoader = document.createElement('div');
    const dotLoaderInner1 = document.createElement('div');
    const dotLoaderInner2 = document.createElement('div');
    const dotLoaderInner3 = document.createElement('div');

    dotLoader.appendChild(dotLoaderInner1);
    dotLoader.appendChild(dotLoaderInner2);
    dotLoader.appendChild(dotLoaderInner3);

    dotLoader.className = "dot-loader";

    let locationDimOverlay = document.createElement('div');
    let blurringDiv = document.createElement('div');
    const someError = document.createElement('div');
    const someErrorInfo = document.createElement('div');
    const takingToHomePage = document.createElement('div');
    
    if(error === "Some error ocuured. Select seats again."){
        someErrorInfo.innerText = "Some error ocuured. Select seats again.";
    }
    else if(error === "Select seats again."){
        someErrorInfo.innerText = "Select seats again.";
    }
    else{
        someErrorInfo.innerText = "Login again, your credentials expired.";
    }

    blurringDiv.className = "blurring-div";
    someError.className = "some-error";
    someErrorInfo.className = "some-error-info";
    takingToHomePage.className = "taking-to-home-page"

    someError.appendChild(someErrorInfo);
    someError.appendChild(takingToHomePage);
    blurringDiv.appendChild(someError);
    document.body.appendChild(blurringDiv);

    blurringDiv.style.pointerEvents = "none";
    document.body.classList.add("no-scroll");
    locationDimOverlay.classList.add("screen-dim");
    document.body.appendChild(locationDimOverlay);
    document.querySelector('.blurring-div').classList.add('blurred');
    someError.classList.add('active');
    someError.style.display = 'flex';

    takingToHomePage.innerText = `Redirecting to home page in 5 seconds`;

    if(window.innerHeight > 900){
        someError.style.setProperty('--marginleftsomeerror', `15%`);
    }

    if(error === "Next Page"){
        someError.innerHTML = "";
        someError.appendChild(dotLoader);
        return;
    }

    let seconds = 4; 
    
    const interval = setInterval(() => {
        if (seconds > 0) {
            takingToHomePage.innerText = `Redirecting to home page in ${seconds} seconds`;
            seconds--;
        } else {
            clearInterval(interval);
            window.location.href = "index.html"; 
        }
    }, 1000);
}

function getCookie(cookieName) {
    const cookies = document.cookie.split("; ");
    for (let c of cookies) {
        const [key, val] = c.split("=");
        if (key === cookieName) return decodeURIComponent(val);
    }
    return 0;
}
function setCookie(cookieName, value, seconds) {
    document.cookie = `${cookieName}=${encodeURIComponent(value)}; max-age=${seconds}; path=/`;
}
function generateSessionId() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0,
            v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

