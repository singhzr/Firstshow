const API_BASE_URL = 'http://localhost:8080';
  
let nameOfTheMovie = null;
let theaterInfo = null;
let showTime = null;
let showDate = null;
let totalAmt = null;
let seatNos = null;
let foodAttached = null;
let movieImageURL = null;
let movieLanguage = null;
let emailId = null;

document.addEventListener("DOMContentLoaded", function() {

    const expiresAt = Date.now(); 
    sessionStorage.setItem("countdownExpires", expiresAt);
    
    loadGenerator();
    
    const token = getCookie("token");

    const urlParams = new URLSearchParams(window.location.search);
    let ticketId = urlParams.get("ticketId");

    if(!ticketId || !token){
        let accessDenied = document.createElement("div");
        accessDenied.className = "access-denied";
        accessDenied.textContent = "Access Denied!";
        document.body.appendChild(accessDenied);

        window.location.href = "./index.html";
        
        return;
    }

    fetch(`${API_BASE_URL}/tickets/viewTicket?ticketId=${ticketId}`, {

        method: "GET",
        headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
        }
    
    })
    .then(response => response.json())
    .then(ticket =>{
        emailId = ticket.emailId;
        nameOfTheMovie = ticket.movieName;
        theaterInfo = ticket.theaterInfo;
        showTime = ticket.showTime;
        showDate = ticket.showDate;
        totalAmt = ticket.totalAmt;
        seatNos = ticket.seatNos;
        foodAttached = ticket.foodAttached;
        movieImageURL = API_BASE_URL+ticket.movieImageURL;
        movieLanguage = ticket.movieLanguage;
        
        setTimeout(()=>{
            createTicketCard(emailId, nameOfTheMovie, theaterInfo, showTime, showDate, totalAmt, seatNos, movieImageURL, movieLanguage);
        }, 1000)

        setTimeout(() => {
            
            html2canvas(document.querySelector(".ticket-card"), {
                scale: 4,                     
                useCORS: true,                   
                backgroundColor: null       
            }).then(canvas => {
                const imageData = canvas.toDataURL("image/png", 1.0);
            
                fetch(`${API_BASE_URL}/tickets/sendTicket`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        imageBase64: imageData,
                        email: emailId
                    })
                });
            });
            
        }, 2000);
    })
});
//In this code, canvas refers to the object that is created by the html2canvas function. It represents a drawing surface that mimics a screenshot of the selected HTML element (in this case, the .ticket-card element) and provides methods for converting this visual content into a format you can work with programmatically.
// The code you've provided captures a screenshot of an HTML element (with the class ticket-card) using the html2canvas library, converts it to a base64 PNG image, and sends this image via a POST request to a specified API endpoint (/tickets/sendTicket). Here’s a breakdown of how it works:

// Capture Screenshot:

// 1. html2canvas(document.querySelector(".ticket-card")): Selects the element with the class .ticket-card and takes a screenshot of it.

// 2. scale: 4: Increases the resolution of the screenshot by scaling it by 4 times.

// 3. useCORS: true: Allows for cross-origin requests, which is necessary if the image is loaded from a different domain.

// 4. backgroundColor: null: Ensures that the background remains transparent.

// Convert to Base64: canvas.toDataURL("image/png", 1.0): Converts the captured canvas to a base64-encoded PNG image with full quality (1.0).

//Base64 is a binary-to-text encoding scheme that is used to encode binary data (like images or files) into ASCII characters. This allows binary data, such as an image file, to be represented as a string of text that can be easily transmitted over media that are designed to handle text, like JSON, XML, or email.

//In this code, canvas refers to the object that is created by the html2canvas function. It represents a drawing surface that mimics a screenshot of the selected HTML element (in this case, the .ticket-card element) and provides methods for converting this visual content into a format you can work with programmatically.

function loadGenerator(){
    const imageGenerating = document.createElement("div");
    imageGenerating.className = "image-generating";
    imageGenerating.textContent = "Generating ticket...";

    if(window.innerWidth < 1101){
        imageGenerating.style.setProperty('--marginLeftImageGenerating', `42vw`);
    }
    document.body.appendChild(imageGenerating);
}

function createTicketCard(emailId, nameOfTheMovie, theaterInfo, showTime, showDate, totalAmt, seatNos, movieImageURL, movieLanguage) {

    document.querySelector(".image-generating").style.display = "none";
    document.querySelector(".dot-loader").style.display = "none";
    document.querySelector(".ticket-card").style.display = "inline-block";

    const ticketCard = document.querySelector(".ticket-card");

    const divFirst = document.createElement("div");
    const divFirstA = document.createElement("div");
    const divFirstB = document.createElement("div");

    const divSecond = document.createElement("div");
    const divSecondA = document.createElement("div");
    const divSecondB = document.createElement("div");  
    const divSecondBone = document.createElement("div");
    const divSecondBtwo = document.createElement("div");
    
    const divThird = document.createElement("div");
    const divFour = document.createElement("div");

    divFirst.className = "div-first";
    divSecond.className = "div-second";
    divThird.className = "div-third";
    divFour.className = "div-four";
    divFirstA.className = "div-first-a";
    divFirstB.className = "div-first-b";
    divSecondA.className = "div-second-a";
    divSecondB.className = "div-second-b";
    divSecondBone.className = "div-second-b-one";
    divSecondBtwo.className = "div-second-b-two";

    const movieImageDiv = document.createElement("img");
    movieImageDiv.className = "ticket-movie-image";
    movieImageDiv.src = movieImageURL;
    movieImageDiv.alt = `${nameOfTheMovie} Poster`;
    divFirstA.appendChild(movieImageDiv);
    divFirst.appendChild(divFirstA);

    const movieTitleDiv = document.createElement("div");
    movieTitleDiv.className = "ticket-movie-title";
    movieTitleDiv.textContent = nameOfTheMovie;
    divFirstB.appendChild(movieTitleDiv);
    divFirst.appendChild(divFirstB);

    const movieLanguageDiv = document.createElement("div");
    movieLanguageDiv.className = "ticket-movie-language";
    movieLanguageDiv.textContent = movieLanguage;
    divFirstB.appendChild(movieLanguageDiv);
    divFirst.appendChild(divFirstB);

    const showDateDiv = document.createElement("div");
    showDateDiv.className = "ticket-show-date-time";
    showDateDiv.textContent = `${showDate} | ${showTime}`;
    divFirstB.appendChild(showDateDiv);
    divFirst.appendChild(divFirstB);


    const theaterInfoDiv = document.createElement("div");
    theaterInfoDiv.className = "ticket-theater-info";
    theaterInfoDiv.textContent = `${theaterInfo}`;
    divFirstB.appendChild(theaterInfoDiv);
    divFirst.appendChild(divFirstB);


    const items = seatNos.replace(/[\[\]]/g, '').split(', ');

    const pItems = items.filter(item => item.includes('(P)'));
    const cItems = items.filter(item => item.includes('(C)'));
    
    const cleanPItems = pItems.map(item => item.replace(/\(P\)/, ''));
    const cleanCItems = cItems.map(item => item.replace(/\(C\)/, ''));

    let classicSeatsText = cleanCItems.join(" | ");
    let premiumSeatsText = cleanPItems.join(" | ");

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

    const confirmationText = document.createElement("div");
    confirmationText.className = "confirmation-text";
    confirmationText.textContent = `${cleanCItems.length+cleanPItems.length} Ticket(s) booked. A confirmation is sent on e-mail ${emailId}.`;


    const totalAmountDiv = document.createElement("div");
    totalAmountDiv.className = "ticket-total-amount";
    totalAmountDiv.textContent = `Total Amount: ${totalAmt}`;

    divSecond.appendChild(divSecondA);
    divSecond.appendChild(divSecondB);

    divThird.appendChild(confirmationText);
    divFour.appendChild(totalAmountDiv);

    ticketCard.appendChild(divFirst);
    ticketCard.appendChild(divSecond);
    ticketCard.appendChild(divThird);
    ticketCard.appendChild(divFour);

    const goBackToHomePageLink = document.createElement("div");
    goBackToHomePageLink.className = "go-back-to-home-page";
    goBackToHomePageLink.textContent = "Go back";

    if(window.innerWidth < 1401){
        goBackToHomePageLink.style.setProperty('--marginLeftBackButton', `47.2vw`);
    }

    goBackToHomePageLink.addEventListener("click", () => {
        window.location.href = "./index.html";
    });
    document.body.appendChild(goBackToHomePageLink);
}

function removeExtraCharactersC(str) {
    let strWithoutBrackets = "";
    for (let i = 0; i < str.length; i++) {

        if(isNumber(str[i].charAt(2))) {
            strWithoutBrackets += str[i].substring(0, 3) + " |";
        }
        else{
            strWithoutBrackets += str[i].substring(0, 2) + " |";
        }
    }
    return strWithoutBrackets;
}
function removeExtraCharactersP(str) {
    let strWithoutBrackets = "";
    for (let i = 0; i < str.length; i++) {

        if(isNumber(str[i].charAt(2))) {
            strWithoutBrackets += str[i].substring(0, 3) + " |";
        }
        else{
            strWithoutBrackets += str[i].substring(0, 2) + " |";
        }
    }
    return strWithoutBrackets.substring(1);
}
function isNumber(value) {
    return !isNaN(value) && isFinite(value);
}

function getCookie(cookieName) {
    const cookies = document.cookie.split("; ");
    for (let c of cookies) {
        const [key, val] = c.split("=");
        if (key === cookieName) return decodeURIComponent(val);
    }
    return 0;
}