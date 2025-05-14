const API_BASE_URL = 'http://localhost:8080';

  
let nameOfTheMovie = null;
let languageOfTheMovie = null;
let genreOfTheMovie = null;

document.addEventListener("DOMContentLoaded", function () {

    const circleLoader = document.querySelector('.spinner');
    circleLoader.style.display = "flex";

    if(window.innerHeight > 900){
        circleLoader.style.setProperty('--dotLoaderSize', `100px`);
    }
    
    const urlParams = new URLSearchParams(window.location.search);

    const dropdownButton = document.querySelector(".dropdown-button");
    const locationIcon = document.createElement("i");
    locationIcon.className = "fa-solid fa-location-dot";
    locationIcon.classList.add("nav-bar-location-icon");
    locationIcon.id = "location";
    dropdownButton.textContent = urlParams.get("city");
    dropdownButton.appendChild(locationIcon);

    const movieId = urlParams.get("movieId");
    nameOfTheMovie = urlParams.get("movieName");
    languageOfTheMovie = urlParams.get("movieLanguage");
    genreOfTheMovie = urlParams.get("movieGenre");

    setTimeout(()=>{
        fetch(`${API_BASE_URL}/movie/allShowsoftheMovie/${movieId}/${urlParams.get("city")}`)
        .then(response => response.json())
        .then(allShowsoftheMovie => {
            circleLoader.style.display = "none";
            updateShowsList(allShowsoftheMovie);
        });
    }, 1000)
});

function updateShowsList(allShowsoftheMovie) {
    const showsContainer = document.getElementById("shows-list");

    const movieNameDiv = document.createElement("div");
    const movieGenre = document.createElement("div");
    const movieDateMainDiv = document.createElement("div");
    const movieDate = document.createElement("div");

    movieNameDiv.className = "movie-name-div";
    movieDateMainDiv.className = "movie-date-div-main-div";
    movieDate.className = "movie-date-div";
    movieGenre.className = "movie-genre-div";

    movieNameDiv.textContent = nameOfTheMovie+" - ("+languageOfTheMovie +")";
    movieGenre.textContent = genreOfTheMovie;
    
    movieNameDiv.appendChild(movieGenre);
    movieDateMainDiv.appendChild(movieDate);

    showsContainer.appendChild(movieNameDiv);
    showsContainer.appendChild(movieDateMainDiv);

    Object.entries(allShowsoftheMovie).forEach(([name, shows]) => {
        const showTimeMainDiv = document.createElement("div");
        const showDiv = document.createElement("div");
        showTimeMainDiv.className = "show-time-main-div";
        showDiv.className = "show-div";

        const theaterName = document.createElement("div");
        theaterName.className = "theater-name-div";
        theaterName.textContent = `${name}`;

        movieDate.textContent = `${shows[0].showDate}`;
        
        showDiv.appendChild(theaterName);

        shows.forEach(show => {
            const showTime = document.createElement("div");
            showTime.className = "show-time-div";
        
            showTime.textContent = `${show.showTime}`;

            showTime.addEventListener("click", ()=>{

                const paramsObj = {
                    currentShowIdObj: show.showId,
                    nameOfTheMovieObj: nameOfTheMovie,
                    theaterNameObj: name,
                    showTimeObj: show.showTime,
                    showDateObj: shows[0].showDate
                };
                sessionStorage.setItem('showDetails', JSON.stringify(paramsObj));

                window.location.href = `theater.html`;
            });
            showTimeMainDiv.appendChild(showTime);
            showDiv.appendChild(showTimeMainDiv);
        });
        showsContainer.appendChild(showDiv);
    });
}

