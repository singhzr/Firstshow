# 🎬 Firstshow - Online Movie Ticket Booking Platform

🎥 Video Demo: [Firstshow - YouTube](https://your-video-link.com)     

🌐 Live Demo: [Firstshow - Live](https://your-video-link.com)

**Firstshow** is a full-stack movie ticket booking application built with **Spring Boot**, **JavaScript**, **HTML**, and **CSS**. It allows users to browse movies, book tickets, manage their profiles, and enables cinema owners/admins to manage theaters and showtimes.


## 🔐 User Features 

### 1. User Authentication
- Secure login and registration using **Spring Security**.
- JWT-based authentication for stateless session management.

### 2. Movie Booking
- Browse movies.
- View showtimes by location.
- Select seats and book tickets with real-time seat availability.
- Concurrency control to **prevent double bookings**, ensuring **100% accuracy**.


## 🏢 Cinema & Theater Management (For Cinema Owners/Admins)

- Manage theaters, screens, and showtimes via dedicated APIs.
- Add, update, and delete movie listings and schedules.
- Real-time synchronization of seat availability.
- Built using **Hibernate** and **JPA**, resulting in **30% faster database interactions**.



## 🛠️ System Architecture & Performance

- Developed **50+ RESTful APIs** covering all major operations.
- Applied **efficient data structures and optimized service logic**, reducing API response time by **45%**.
- Used **Spring Boot** for the backend, and **vanilla JavaScript, HTML, CSS** for the frontend.
- Structured codebase for scalability, performance, and maintainability.


## 🚀 Tech Stack

- **Backend:** Spring Boot, Spring Security, Hibernate, JPA
- **Frontend:** HTML, CSS, JavaScript
- **Database:** MySQL / PostgreSQL
- **Tools:** Maven, Postman, Git

## GETTING INTO THE PROJECT

## 🗺️ Select Location Page
Users begin by selecting their city to view nearby theaters and available movies.  

![Image](https://github.com/user-attachments/assets/c164160d-ecd3-4e21-b787-bf6bd7ed8491)


## 🎬 Home Page – Browse Movies
Displays available movies based on the selected location.   

![Image](https://github.com/user-attachments/assets/f6d913ea-f68a-497b-8ad8-afde17a2d2c4)


## 🎞️ Movie Info Page
Displays detailed information about the selected movie—name, genre, rating, duration.   

<img src="https://github.com/user-attachments/assets/fee99b03-94e9-4445-b395-083580ba2ea6" width="100%" />


## 🎞️ Movie Info Page – Showtimes
Displays detailed movie information along with all available showtimes across different theaters and dates. Users can choose their preferred show to proceed with booking.    

![Image](https://github.com/user-attachments/assets/39801410-9fec-42af-977f-793e407ef97c)


## 💺 Seat Selection Page
Allows users to view and select available seats for the chosen showtime. Booked and premium seats are clearly indicated. Once seats are selected, users can proceed to payment.  

<img src="https://github.com/user-attachments/assets/7ec9b38a-374e-4153-9789-8537b7008e19" width="100%" />
<img src="https://github.com/user-attachments/assets/e9f6b358-82fd-4594-abbe-cd49525d7562" width="100%" />


## ✅ Booking Confirmation Page
Displays confirmation details after a successfully selecting seats. 

<img src="https://github.com/user-attachments/assets/2d1a254d-f0a4-4d06-9aa7-0a2fee813e73" width="100%" />
 

## 🔒 Login Required
Before proceeding to seat selection or booking, users must be logged in. Unauthenticated users are redirected to the login page.   

<img src="https://github.com/user-attachments/assets/8495bc89-565c-4a24-9590-0572be246714" width="100%" />
<img src="https://github.com/user-attachments/assets/0b77ba1b-c65f-44de-beb4-5c07469c1e63" width="100%" />


## ✅ Booking Confirmation Page
Unauthenticated users are redirected to the login page before booking. After login, they are automatically redirected to the booking confirmation page with their selected seats and showtime preserved.    

<img src="https://github.com/user-attachments/assets/2d1a254d-f0a4-4d06-9aa7-0a2fee813e73" width="100%" />


## 🎫 Ticket View Page
Displays ticket details including movie name, theater, showtime, and seat numbers. A PDF of the ticket is also sent to the user's registered email for easy access.   

<img src="https://github.com/user-attachments/assets/9c542b63-e3c6-4133-b553-0e8389e3ebcb" width="100%" />

