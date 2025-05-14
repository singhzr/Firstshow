const API_BASE_URL = 'http://localhost:8080';

  
document.addEventListener("DOMContentLoaded", () => {
    
    const urlParams = new URLSearchParams(window.location.search);

    const dropdownButton = document.querySelector(".dropdown-button");
    const locationIcon = document.createElement("i");
    locationIcon.className = "fa-solid fa-location-dot";
    locationIcon.classList.add("nav-bar-location-icon");
    locationIcon.id = "location";
    dropdownButton.textContent = urlParams.get("city");
    dropdownButton.appendChild(locationIcon);

    const movieId = urlParams.get("movieId");
    

    fetch(`${API_BASE_URL}/movie/movieData/${movieId}`)
    .then(response => response.json())
    .then(movie => {
        updateMovieList(movie);
    })
});

function updateMovieList(movie) {

    const movieContainer = document.querySelector(".movie-container");

    const movieImageDiv = document.createElement("div");
    const movieImage = document.createElement("img");

    movieContainer.className = "movie-container";
    movieImageDiv.className = "movie-image-div";

    movieImage.src = API_BASE_URL+movie.movieImageUrl;

    movieImageDiv.appendChild(movieImage);
    movieContainer.appendChild(movieImageDiv);

    const movieDetails = document.createElement("div");
    movieDetails.className = "movie-details";

    const durationAndLanguageAndReleaseDate = document.createElement("div");
    const genre = document.createElement("div");
    const rating = document.createElement("div");
    const name = document.createElement("div");
    const bookTickets = document.createElement("div");

    name.className = "movie-name";
    rating.className = "movie-rating";
    durationAndLanguageAndReleaseDate.className = "movie-duration-language-release-date";
    genre.className = "movie-genre";
    bookTickets.className = "book-tickets";
    bookTickets.textContent = "Book Tickets";

    bookTickets.addEventListener("click", () => {
        const urlParams = new URLSearchParams(window.location.search);
        window.location.href = window.location.href = `allShowsOfMovie.html?city=${urlParams.get("city")}&movieId=${movie.movieId}&movieName=${movie.movieName}&movieLanguage=${movie.movieLanguage}&movieGenre=${movie.genre}`;
    });
    
    name.textContent = movie.movieName;
    rating.textContent = "⭐"+movie.rating+"/10";
    durationAndLanguageAndReleaseDate.textContent = movie.duration + " minutes | " + movie.movieLanguage + " | " + movie.releaseDate;
    genre.textContent = movie.genre;
    
    movieDetails.appendChild(name);
    movieDetails.appendChild(rating);
    movieDetails.appendChild(durationAndLanguageAndReleaseDate);
    movieDetails.appendChild(genre);
    movieDetails.appendChild(bookTickets);

    movieContainer.appendChild(movieDetails);
}

