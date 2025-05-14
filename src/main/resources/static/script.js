const API_BASE_URL = 'http://localhost:8080';

const cities = ["Mumbai", "Bangalore", "Hyderabad", "Chennai","Lucknow", "Pune"];
const classNames = ["mumbai", "bangalore", "hyderabad", "chennai", "lucknow", "pune"];
const token = getCookie("token");

const navDiv = document.getElementById('nav_div');
const movieList = document.getElementById('movie-list');

const mainDivForTicket = document.createElement("div");
mainDivForTicket.className = "main-div-for-ticket";

const sidebarExit = document.createElement("i");
sidebarExit.className = "fa-solid fa-arrow-left fa-xl";
sidebarExit.classList.add("sidebar-exit");

let locationDimOverlay = document.createElement("div");

const profile = document.querySelector('.profile');
let sidebar = document.createElement("div");

let sessionId = getCookie('sessionId');

const circleLoader = document.querySelector('.spinner');

document.addEventListener("DOMContentLoaded", function() {

    function isMobileNotInDesktopMode() {
        const ua = navigator.userAgent.toLowerCase();
      
        const isMobileDevice = /android|iphone|ipad|ipod/i.test(ua);
        const isPretendingToBeDesktop = /windows|macintosh|linux/i.test(ua) && !/mobile/i.test(ua);
      
        return isMobileDevice && !isPretendingToBeDesktop;
    }

    if (isMobileNotInDesktopMode()) {
        someErrorOccurred();
        return;
    }
    if (!sessionId) {
        sessionId = generateSessionId();
        setCookie('sessionId', sessionId, 3600);
    }

    createsidebar();
    toggleSiderBar();
    disableEnableLoginSignup();
    listMovies();
    searchMovie();
})


let movieDataFromBackened = [];
function createsidebar(){

    let sidebarProfileInfo = document.createElement("div");
    sidebarProfileInfo.className = "sidebar-profile-info";
    const sidebarName = document.createElement("div");
    const sidebarLogo = document.createElement("i");
    
    sidebarLogo.className = "fa-solid fa-user fa-xl"; 
    sidebarLogo.classList.add("sidebar-logo");
    sidebarName.className = "sidebar-name";
    
    
    sidebarName.innerText = getCookie("name") ? getCookie("name") : "Profile";

    let orders = document.createElement("div");
    orders.className = "orders";
    const ordersLogo = document.createElement("i");
    ordersLogo.className = "fa-solid fa-suitcase fa-xl"; 
    ordersLogo.classList.add("orders-logo");
    const ordersName = document.createElement("div");
    ordersName.className = "orders-name";
    ordersName.innerText = "Your Orders";

    sidebarProfileInfo.appendChild(sidebarLogo);
    sidebarProfileInfo.appendChild(sidebarName);
    sidebarProfileInfo.appendChild(sidebarExit);
    sidebar.appendChild(sidebarProfileInfo);
    
    orders.appendChild(ordersLogo);
    orders.appendChild(ordersName);
    sidebar.appendChild(orders);

    orders.addEventListener("click", () => {
        if(token){

            if(mainDivForTicket.innerHTML === ""){
                tickets();
            } 
            else{
                mainDivForTicket.innerHTML = "";
            }
        }
    });

}
function disableEnableLoginSignup(){
    const navSignup = document.querySelector('.nav-signup');
    const navSignIn = document.querySelector('.nav-signin');

    if(getCookie("name").length > 0 && token) {
        navSignIn.disabled = true;
        navSignIn.innerText = getCookie("name");
        navSignup.innerText = "Logout";
    }
    else{
        document.cookie = "name=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
        document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
        document.cookie = "emailId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
        navSignIn.disabled = false;
        navSignIn.innerText = "Sign In";
        navSignup.innerText = "Sign Up";
    }
}
function listMovies() {

    circleLoader.style.display = "flex";

    if(window.innerHeight > 900){
        circleLoader.style.setProperty('--dotLoaderSize', `100px`);
    }

    setTimeout(()=>{

        const chooseLocation = document.querySelector('.choose-location');
        chooseLocation.style.display = "none";
        movieList.innerHTML = "";

        fetch(`${API_BASE_URL}/movie/allListedMovies`)
        .then(response => response.json())
        .then(data => {

            if(getCookie("city") == 0) {

                if(window.innerHeight > 900){
                    document.querySelector('.location').style.setProperty('--marginLeft', `15%`);
                }
                
                document.body.classList.add("no-scroll");
                locationDimOverlay.classList.add("screen-dim");
                document.body.appendChild(locationDimOverlay);
                profile.style.pointerEvents = "none";
                navDiv.style.pointerEvents = "none";
                movieList.style.pointerEvents = "none";
                chooseLocation.style.display = "flex";
                setFunctionalities();          
                movieDataFromBackened = data;
                updateMovieList(data);
                circleLoader.style.display = "none";
            }
            else{
                document.body.classList.remove("no-scroll");
                locationDimOverlay.remove();
                profile.style.pointerEvents = "auto";
                navDiv.style.pointerEvents = "auto";
                movieList.style.pointerEvents = "auto";
                onLoad(getCookie("city"));
                loadMovieDataFromBackend(getCookie("city"));
            }
        })
    }, 1000);
}

function loadMovieDataFromBackend(city) {
    movieList.innerHTML = "";
    circleLoader.style.display = "flex";
    fetch(`${API_BASE_URL}/movie/listedMoviesCityWise/${city}`)
        .then(response => response.json())
        .then(data => {
            movieDataFromBackened = data;
            updateMovieList(data);
            circleLoader.style.display = "none";
        })
}


function onLoad(currentLocation) {
    const dropdownButton = document.querySelector(".dropdown-button");
    const dropdownMenu = document.querySelector(".dropdown-menu");

    dropdownMenu.innerHTML = "";
    const locationIcon = document.createElement("i");
    cities.forEach((city) => {
        const listItem = document.createElement("li");
        listItem.textContent = city;
        listItem.addEventListener("click", () => {

            setCookie("city", city, 3600);

            locationIcon.className = "fa-solid fa-location-dot";
            locationIcon.classList.add("nav-bar-location-icon");
            locationIcon.id = "location";
            dropdownButton.textContent = city;
            dropdownMenu.style.display = "none";
            dropdownButton.appendChild(locationIcon);
            loadMovieDataFromBackend(city);
            searchMovie();
        });
        dropdownMenu.appendChild(listItem);
    });

    
    locationIcon.className = "fa-solid fa-location-dot";
    locationIcon.id = "location";
    locationIcon.classList.add("nav-bar-location-icon");

    dropdownButton.textContent = currentLocation === "all" ? "Location" : currentLocation;
    dropdownButton.appendChild(locationIcon);

    dropdownButton.addEventListener("click", () => {
        dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
    });
}


function setFunctionalities() {

    const location = document.querySelector('.location');
    const citiesDiv = document.querySelector('.cities-div');
    const chooseLocation = document.querySelector('.choose-location');
    const blurringDiv = document.querySelector('.blurring-div');
    
    blurringDiv.classList.add('blurred');
    location.classList.add('active');
    location.style.display = 'flex';

    chooseLocation.addEventListener("click", () => {

        document.body.classList.remove("no-scroll");
        locationDimOverlay.remove();
        navDiv.style.pointerEvents = "auto";
        movieList.style.pointerEvents = "auto";
        profile.style.pointerEvents = "auto";

        location.classList.remove('active');
        location.style.display = 'none';
        chooseLocation.display = 'none';
        blurringDiv.classList.remove('blurred');
        onLoad("all");
        loadMovieDataFromBackend("all");
        searchMovie();
    });

    location.appendChild(chooseLocation);

    cities.forEach((city, index) => {
        const h4 = document.createElement("h4");
        h4.classList.add(classNames[index]);
    
        const icon = document.createElement("i");
        icon.classList.add("fa-solid", "fa-location-dot");
    
        h4.appendChild(icon);
        h4.append(" " + city);

        h4.addEventListener("click", () => {

            document.body.classList.remove("no-scroll");
            locationDimOverlay.remove();
            navDiv.style.pointerEvents = "auto";
            movieList.style.pointerEvents = "auto";
            profile.style.pointerEvents = "auto";
            
            setCookie("city", city, 3600);
            location.classList.remove('active');
            location.style.display = 'none';
            chooseLocation.display = 'none';
            blurringDiv.classList.remove('blurred');
            onLoad(city);
            loadMovieDataFromBackend(city);
            searchMovie();
        });
    
        citiesDiv.appendChild(h4);
    });
    location.appendChild(citiesDiv);
}

function updateMovieList(data) {
    
    const movieList = document.getElementById("movie-list");
    movieList.innerHTML = "";

    data.forEach(element => {
        const movieContainer = document.createElement("div");

        movieContainer.addEventListener("click", () => {
            window.location.href = `movieDetails.html?movieId=${element.movieId}&city=${getCookie("city")}`;
        });

        const movieImageDiv = document.createElement("div");
        const movieName = document.createElement("div");
        const movieGenre = document.createElement("div");
        const movieImage = document.createElement("img");

        movieContainer.className = "movie-container";
        movieImageDiv.className = "movie-image-div";
        movieName.className = "movie-name";
        movieGenre.className = "movie-genre"

        movieName.textContent = element.movieName;
        movieGenre.textContent = element.genre;
        
        movieImage.src = API_BASE_URL+element.movieImageUrl;
        movieImage.alt = element.movieName;

        movieImageDiv.appendChild(movieImage);
        movieContainer.appendChild(movieImageDiv);
        movieContainer.appendChild(movieName);
        movieContainer.appendChild(movieGenre);

        movieList.appendChild(movieContainer);
    });  
}

function searchMovie(){
    document.querySelector(".search-input").addEventListener("input", (event)=>{

        let currentMovies = movieDataFromBackened.filter((item) => 
            item.movieName?.toLowerCase().includes(event.target.value.toLowerCase())
        );    
        updateMovieList(currentMovies);
    });
}

function generateSessionId() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0,
            v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}
document.querySelector(".nav-signup").addEventListener("click", (event) => {
    
    if(event.target.innerText === "Logout") {
        document.cookie = "name=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
        document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
        document.cookie = "emailId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
        document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
        setCookie('sessionId', generateSessionId(), 3600);
        document.querySelector(".nav-signin").disabled = false;
        document.querySelector(".nav-signin").innerText = "Sign In";
        document.querySelector(".nav-signup").innerText = "Sign Up";
        window.location.href = "./index.html";
    }
    else{
        window.location.href = "./signup.html?returnUrl=index.html";
    }
    fetch(`${API_BASE_URL}/users/loadUser`);
});


document.querySelector(".nav-signin").addEventListener("click", (event) => {
    fetch(`${API_BASE_URL}/users/loadUser`);
    window.location.href = "./login.html?returnUrl=index.html";
});


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

function tickets(){

    if (!token) {
        return;
    }

    mainDivForTicket.innerHTML = "";

    const laodingAllTickets = document.createElement("div");
    laodingAllTickets.className = "loading-all-tickets";
    laodingAllTickets.innerText = "Loading..."
    mainDivForTicket.appendChild(laodingAllTickets);
    sidebar.appendChild(mainDivForTicket);

    fetch(`${API_BASE_URL}/tickets/viewAllTicketOfUser/{username}?username=${getCookie("username")}`, {

        method: "GET",
        headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
        }
    
    })

    .then(response => response.json())
    .then(tickets =>{

        if(tickets.length === 0){
            laodingAllTickets.innerText = "Wow, such empty 🫗";
            return;
        }
        laodingAllTickets.style.display = "none";
        tickets.forEach(ticket => {
            createTicketCard(ticket.ticketId, ticket.movieName, ticket.theaterInfo, ticket.showTime, ticket.showDate, ticket.totalAmt, 
                            ticket.seatNos, ticket.movieImageURL, ticket.movieLanguage)
        });
    })
}
function createTicketCard(ticketId, nameOfTheMovie, theaterInfo, showTime, showDate, totalAmt, seatNos, movieImageURL, movieLanguage) {

    const ticketCard = document.createElement("div");
    ticketCard.className = "ticket-card";

    const divFirst = document.createElement("div");
    const divFirstA = document.createElement("div");
    const divFirstB = document.createElement("div");

    const divSecond = document.createElement("div");
    const divSecondA = document.createElement("div");
    const divSecondB = document.createElement("div");  
    const divSecondBone = document.createElement("div");
    const divSecondBtwo = document.createElement("div");


    divFirst.className = "div-first";
    divSecond.className = "div-second";
    divFirstA.className = "div-first-a";
    divFirstB.className = "div-first-b";
    divSecondA.className = "div-second-a";
    divSecondB.className = "div-second-b";
    divSecondBone.className = "div-second-b-one";
    divSecondBtwo.className = "div-second-b-two";

    const movieImageDiv = document.createElement("img");
    movieImageDiv.className = "ticket-movie-image";
    movieImageDiv.src = API_BASE_URL+movieImageURL;
    movieImageDiv.alt = `${nameOfTheMovie} Poster`;
    divFirstA.appendChild(movieImageDiv);
    divFirst.appendChild(divFirstA);

    const movieTitleAndCancel = document.createElement("div");
    movieTitleAndCancel.className = "movie-title-and-cancel";

    const cancel = document.createElement("div");
    cancel.className = "cancel-btn";
    cancel.textContent = "Cancel";

    const movieTitleDiv = document.createElement("div");
    movieTitleDiv.className = "ticket-movie-title";
    movieTitleDiv.textContent = nameOfTheMovie;

    movieTitleAndCancel.appendChild(movieTitleDiv);
    movieTitleAndCancel.appendChild(cancel);

    divFirstB.appendChild(movieTitleAndCancel);
    divFirst.appendChild(divFirstB);

    cancel.addEventListener("click", () => {

        cancel.textContent = "Cancelling.."

        fetch(`${API_BASE_URL}/tickets/cancelTicket?ticketId=${ticketId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        })
        setTimeout(()=>{
            mainDivForTicket.removeChild(ticketCard);
        }, 1000);
        
    });

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

    divSecond.appendChild(divSecondA);
    divSecond.appendChild(divSecondB);

    divFirstB.appendChild(divSecond);
    ticketCard.appendChild(divFirst);

    mainDivForTicket.appendChild(ticketCard);
    sidebar.appendChild(mainDivForTicket);
}

function toggleSiderBar(){
    let dimOverlay = null;
    sidebar.classList.remove("hidden");
    sidebar.classList.add("sidebar");

    profile.addEventListener("click", () => {
        if (!dimOverlay) {
            dimOverlay = document.createElement("div");
            dimOverlay.classList.add("screen-dim");
            document.body.appendChild(dimOverlay);
            document.body.classList.add("no-scroll");
            sidebar.style.display = "block";
            document.body.appendChild(sidebar);
        } 
        else {
            dimOverlay.remove();
            dimOverlay = null;
            document.body.classList.remove("no-scroll");
            sidebar.style.display = "none";
            
        }
    });
    
    sidebarExit.addEventListener("click", () => {
        document.body.classList.remove("no-scroll");
        dimOverlay.remove();
        dimOverlay = null;
        sidebar.style.display = "none";
    });
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


function someErrorOccurred(){

    document.body.innerHTML = null; 

    let locationDimOverlay = document.createElement('div');
    let blurringDiv = document.createElement('div');
    const someError = document.createElement('div');
    const someErrorInfo = document.createElement('div');

    someErrorInfo.innerText = "Please switch to desktop mode.";

    blurringDiv.className = "blurring-div";
    someError.className = "some-error";
    someErrorInfo.className = "some-error-info";

    someError.appendChild(someErrorInfo);
    blurringDiv.appendChild(someError);
    document.body.appendChild(blurringDiv);

    blurringDiv.style.pointerEvents = "none";
    document.body.classList.add("no-scroll");
    locationDimOverlay.classList.add("screen-dim");
    document.body.appendChild(locationDimOverlay);
    document.querySelector('.blurring-div').classList.add('blurred');
    someError.classList.add('active');
    someError.style.display = 'flex';
}


