const API_BASE_URL = 'http://localhost:8080';

let currentShowId  = null;
let nameOfTheMovie = null;
let theaterName = null;
let showTime = null;
let showDate = null;
let showSeatData = null;
let theaterSeatData = null;
let clickedSeats = [];
let totalPrice = 0;

let locationDimOverlay = document.createElement("div");

const token = getCookie("token");
let sessionId = getCookie('sessionId');
document.addEventListener("DOMContentLoaded", function (event) {

    event.preventDefault();

    const savedParams = JSON.parse(sessionStorage.getItem('showDetails'));
    currentShowId = savedParams.currentShowIdObj;
    nameOfTheMovie = savedParams.nameOfTheMovieObj;
    theaterName = savedParams.theaterNameObj;
    showTime = savedParams.showTimeObj;
    showDate = savedParams.showDateObj;
    
    loadScreen();
});

window.addEventListener("pageshow", function (event) {
    const navType = performance.getEntriesByType("navigation")[0]?.type;
    if (event.persisted || navType === "back_forward") {
      window.location.reload();
    }
  });

function loadScreen(){

    const circleLoader = document.querySelector('.spinner');
    circleLoader.style.display = "flex";

    if(window.innerHeight > 900){
        circleLoader.style.setProperty('--dotLoaderSize', `100px`);
    }
    
    setTimeout(()=>{

        fetch(`${API_BASE_URL}/shows/theaterDetails/${currentShowId}`)
        .then(response => response.json())
        .then(data => {

            theaterSeatData = data;

            let maxColumns = 0;

            for(let i = 0; i < theaterSeatData.length; i++){

                let seat = theaterSeatData[i].seatNo;

                if(seat.includes("A")){
                    maxColumns++;
                }
                else{
                    break;
                }
            }

            let pixelsforpc = maxColumns*21;
            let pixelsforphone = maxColumns*14;

            fetch(`${API_BASE_URL}/shows/showSeatsData/${currentShowId}`)
            .then(response => response.json())
            .then(data => {

                showSeatData = data;
                loadTheater(data);
                circleLoader.style.display = "none";

                let widthOfWindow = window.innerWidth;
                
                const screenElement = document.querySelector('.screen');
                if (screenElement) {

                    //for pc
                    if(widthOfWindow > 1101){
                    
                        screenElement.style.setProperty('--marginLeft', `${pixelsforpc}px`);
                        
                        if(theaterSeatData.length < 40){
                            screenElement.style.setProperty('--marginLeft', `${pixelsforpc+50}px`);
                            screenElement.style.setProperty('--width', `120px`);
                        }
                    }
                    else{

                        screenElement.style.setProperty('--width', `${200}px`);
                        
                        document.querySelector('.premium-seats-div').style.setProperty('--seatsDiv', `${180}px`);
                        document.querySelector('.classic-seats-div').style.setProperty('--seatsDiv', `${180}px`);
                        document.querySelector('.premium-seats-price-div').style.setProperty('--price', `${120}px`);
                        document.querySelector('.classic-seats-price-div').style.setProperty('--price', `${120}px`);

                        if(theaterSeatData.length > 250){
                            screenElement.style.setProperty('--marginLeft', `${280}px`);
                        }
                        else{
                            screenElement.style.setProperty('--marginLeft', `${pixelsforphone-50}px`);
                        }

                        if(theaterSeatData.length < 40){
                            screenElement.style.setProperty('--marginLeft', `${pixelsforphone}px`);
                            screenElement.style.setProperty('--width', `120px`);
                        }
                        let rowNameDivs = document.querySelectorAll('.row-name-div');
                        let seats = document.querySelectorAll('.seat-number-div');
                        for(let x of rowNameDivs) x.style.setProperty('--rowName', `10px`);
                        for(let x of seats){
                            x.style.setProperty('--seatBorderRadius', `${0}px`);
                            x.style.setProperty('--seatNumberSize', `small`);
                        }
                        
                    }
                }
            });
        })
    },1000);
};

function loadTheater(theaterOfCurrentShow) {

    let movieDetails = document.getElementById("movie-details");
    let theaterSeats = document.getElementById("theater-seats");

    let movieNameDiv = document.createElement("div");
    let theaterNameAndDateTime = document.createElement("div");
    let premiumSeatsDiv = document.createElement("div");
    let classicSeatsDiv = document.createElement("div");
    let premiumSeatsPriceDiv = document.createElement("div");
    let classicSeatsPriceDiv = document.createElement("div");
    let screen = document.createElement("div");

    screen.className = "screen";
    premiumSeatsDiv.className = "premium-seats-div";
    classicSeatsDiv.className = "classic-seats-div";
    premiumSeatsPriceDiv.className = "premium-seats-price-div";
    classicSeatsPriceDiv.className = "classic-seats-price-div";
    movieNameDiv.className = "movie-name-div";
    theaterNameAndDateTime.className = "theater-name-date-time";

    let screenIcon = document.createElement("img");
    screenIcon.src = "./screen.jpg";
    screenIcon.alt = "Screen Icon";
    screen.appendChild(screenIcon);
    
    let classicPrice = 0;
    let premiumPrice = 0;
    showSeatData.forEach(seat => {
        if(seat.seatType === 'CLASSIC') {
            classicPrice = seat.price;
        }
        if(seat.seatType === 'PREMIUM') {
            premiumPrice = seat.price;
        }
    });
    premiumSeatsPriceDiv.innerText = `Premium Rs. ${premiumPrice}`;
    classicSeatsPriceDiv.innerText = `Classic Rs. ${classicPrice}`;
    movieNameDiv.innerText = nameOfTheMovie;
    theaterNameAndDateTime.innerText = theaterName + " | " + showDate + " | " + showTime;

    premiumSeatsDiv.appendChild(premiumSeatsPriceDiv);
    classicSeatsDiv.appendChild(classicSeatsPriceDiv);
    movieDetails.appendChild(movieNameDiv);
    movieDetails.appendChild(theaterNameAndDateTime);
    

    let theaterPremiumSeats = [];
    let theaterClassicSeats = [];

    theaterOfCurrentShow.forEach(element => {
        if (element.seatType === 'PREMIUM') {
            theaterPremiumSeats.push(element);
        } else {
            theaterClassicSeats.push(element);
        }
    });

    let rowLabel = "A";

    for (let i = 0; i < theaterPremiumSeats.length; ) {
        let premiumRowNumberDiv = document.createElement("div");
        let rowNameDiv = document.createElement("div");

        premiumRowNumberDiv.className = "premium-row-number-div";
        rowNameDiv.className = "row-name-div";

        rowNameDiv.innerText = rowLabel;
        premiumRowNumberDiv.appendChild(rowNameDiv);

        let j = i;
        while (j < theaterPremiumSeats.length && theaterPremiumSeats[j].seatNo.startsWith(rowLabel)) {

            let seatNoDiv = document.createElement("div");
            seatNoDiv.className = "seat-number-div";
            seatNoDiv.id = theaterPremiumSeats[j].theaterSeatId;
            seatNoDiv.value = theaterPremiumSeats[j].seatNo;

            let currentSeatsFromShowSeatData = getCurrentSeatFromShowSeatData(theaterPremiumSeats[j].seatNo);

            if (currentSeatsFromShowSeatData.status === "AVAILABLE") {
                seatNoDiv.classList.add("not-selected");
                seatNoDiv.addEventListener("click", (event)=> {
                    if (clickedSeats.includes(event.target)) {
                        removeFromCart(event, currentSeatsFromShowSeatData.price);
                    } 
                    else {
                        addToCart(event, currentSeatsFromShowSeatData.price);
                    }
                });
            }
            else {
                seatNoDiv.classList.add("booked");
            }

            if (!isNaN(parseInt(theaterPremiumSeats[j].seatNo[2], 10))) {
                seatNoDiv.innerText = theaterPremiumSeats[j].seatNo.substring(1, 3);
            } 
            else {
                seatNoDiv.innerText = theaterPremiumSeats[j].seatNo.substring(1, 2);
            }
            premiumRowNumberDiv.appendChild(seatNoDiv);
            j++;
        }

        i = j;
        premiumSeatsDiv.appendChild(premiumRowNumberDiv);
        rowLabel = String.fromCharCode(rowLabel.charCodeAt(0) + 1);
    }

    theaterSeats.appendChild(premiumSeatsDiv);

    for (let i = 0; i < theaterClassicSeats.length; ) {
        let classicRowNumberDiv = document.createElement("div");
        let rowNameDiv = document.createElement("div");

        classicRowNumberDiv.className = "classic-row-number-div";
        rowNameDiv.className = "row-name-div";

        rowNameDiv.innerText = rowLabel;
        classicRowNumberDiv.appendChild(rowNameDiv);

        let j = i;
        while (j < theaterClassicSeats.length && theaterClassicSeats[j].seatNo.startsWith(rowLabel)) {

            let seatNoDiv = document.createElement("div");
            seatNoDiv.className = "seat-number-div"
            seatNoDiv.value = theaterClassicSeats[j].seatNo;
            seatNoDiv.id = theaterClassicSeats[j].theaterSeatId;
    

            let currentSeatsFromShowSeatData = getCurrentSeatFromShowSeatData(theaterClassicSeats[j].seatNo);

            if (currentSeatsFromShowSeatData.status === "AVAILABLE") {
                seatNoDiv.classList.add("not-selected");
                seatNoDiv.addEventListener("click", (event)=> {
                    if (clickedSeats.includes(event.target)) {
                        removeFromCart(event, currentSeatsFromShowSeatData.price);
                    } else {
                        addToCart(event, currentSeatsFromShowSeatData.price);
                    }
                });
            }

            else {
                seatNoDiv.classList.add("booked");
            }

            if (!isNaN(parseInt(theaterClassicSeats[j].seatNo[2], 10))) {
                seatNoDiv.innerText = theaterClassicSeats[j].seatNo.substring(1, 3);
            } 
            else {
                seatNoDiv.innerText = theaterClassicSeats[j].seatNo.substring(1, 2);
            }
            classicRowNumberDiv.appendChild(seatNoDiv);
            j++;
        }

        i = j;
        classicSeatsDiv.appendChild(classicRowNumberDiv);
        rowLabel = String.fromCharCode(rowLabel.charCodeAt(0) + 1);
    }

    classicSeatsDiv.appendChild(screen);
    theaterSeats.appendChild(classicSeatsDiv);

    if (window.innerHeight <= 900) {
        const seats = document.querySelectorAll('.seat-number-div');

        seats.forEach(seat => {

            if(!seat.classList.contains("booked")){
                seat.addEventListener('mouseenter', () => {
                    seat.style.backgroundColor = '#1ea83c';
                    seat.style.color = 'white';
                    seat.style.cursor = "pointer";
                });

                seat.addEventListener('mouseleave', () => {
                    seat.style.backgroundColor = '';
                    seat.style.color = '';
                });
            }
        });
    }
    
}


function addToCart(event, price) {

    if(!clickedSeats.includes(event.target)){

        clickedSeats.push(event.target);
        event.target.classList.remove("not-selected");
        event.target.classList.add("selected");

        totalPrice += price;
        let stickyDiv = document.querySelector(".stickyDiv");

        if(stickyDiv.classList.contains("hidden")){
            stickyDiv.classList.remove("hidden");
        }
        document.querySelector('.price').innerText = "Pay ₹" + totalPrice;
    }
}

function removeFromCart(event, price) {

    clickedSeats.splice(clickedSeats.indexOf(event.target), 1);
    event.target.classList.add("not-selected");
    event.target.classList.remove("selected");

    totalPrice -= price;
    if (totalPrice == 0) {
        totalPrice = 0;
        let stickyDiv = document.querySelector(".stickyDiv");
        stickyDiv.classList.add("hidden");
    }
    document.querySelector('.price').innerText = "Pay Rs. " + totalPrice;
}

function getCurrentSeatFromShowSeatData(seatNo) {
    return showSeatData.find(seat => seat.seatNo === seatNo);
}

document.querySelector('.price').addEventListener("click", () => {

    let promise = lockSeat(clickedSeats);

    someErrorOccurred(true);

    promise.then(data => {

        setTimeout(()=>{

            if(data === "Seats already locked or booked."){
                someErrorOccurred();
            }
            else{

                const seatSelectionError = document.querySelector('.seat-selection-error');
                seatSelectionError.style.display = "none";
                
                const expiresAt = Date.now() + 5 * 60 * 1000; // 5 minutes from now
                sessionStorage.setItem("countdownExpires", expiresAt);

                sessionStorage.setItem("clickedSeats", JSON.stringify(clickedSeats.map(seat => seat.value)));
                sessionStorage.setItem("totalPrice", JSON.stringify(totalPrice));
                window.location.href = `./confirmation.html`;
            }
        }, 1000);
    });
})


function someErrorOccurred(check){

    const dotLoader = document.createElement('div');
    const dotLoaderInner1 = document.createElement('div');
    const dotLoaderInner2 = document.createElement('div');
    const dotLoaderInner3 = document.createElement('div');

    dotLoader.appendChild(dotLoaderInner1);
    dotLoader.appendChild(dotLoaderInner2);
    dotLoader.appendChild(dotLoaderInner3);

    dotLoader.className = "dot-loader";

    const refresh = document.querySelector('.refresh');

    let movieDetails = document.getElementById('movie-details');
    let blurringDiv = document.querySelector('.blurring-div');
    const seatSelectionError = document.querySelector('.seat-selection-error');
    const seatSelectionErrorInfo = document.querySelector('.seat-selection-error-info');

    if(check){
        refresh.style.display = "none";
        seatSelectionErrorInfo.appendChild(dotLoader);
    }
    else{
        refresh.style.display = "flex";
        seatSelectionErrorInfo.innerText = "The seats you were booking may not be available. Try booking different seats.";
    }
    
    if(window.innerHeight > 900){
        seatSelectionError.style.setProperty('--marginleftseatselectionerror', `15%`);
    }
    movieDetails.style.pointerEvents = "none";
    blurringDiv.style.pointerEvents = "none";
    document.body.classList.add("no-scroll");
    locationDimOverlay.classList.add("screen-dim");
    document.body.appendChild(locationDimOverlay);
    document.querySelector('.blurring-div').classList.add('blurred');
    seatSelectionError.classList.add('active');
    seatSelectionError.style.display = 'flex';

    refresh.addEventListener('click', () => {
        movieDetails.style.pointerEvents = "auto";
        blurringDiv.style.pointerEvents = "auto";
        document.body.classList.remove("no-scroll");
        locationDimOverlay.classList.remove("screen-dim");
        document.querySelector('.blurring-div').classList.remove('blurred');
        seatSelectionError.classList.remove('active');
        seatSelectionError.style.display = 'none';
        window.location.href = `theater.html`;
    });
}

async function lockSeat() {

    const data = {
      showId: currentShowId,
      seatList: clickedSeats.map(seat => seat.value)
    };
  
    return fetch(`${API_BASE_URL}/showSeat/lockSeat`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Session-ID': sessionId 
      },
      body: JSON.stringify(data)
    })
    .then(response => response.text()) 
    .then(data => {
        return data; 
    });
}

function getCookie(cookieName) {
    const cookies = document.cookie.split("; ");
    for (let c of cookies) {
        const [key, val] = c.split("=");
        if (key === cookieName) return decodeURIComponent(val);
    }
    return 0;
}

