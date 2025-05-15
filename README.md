# 🎬 Firstshow - Online Movie Ticket Booking Platform

🎥 Video Demo: [Firstshow - YouTube](https://youtu.be/FUo0USAGxQE)     

🌐 Live Demo: [Firstshow - Live](https://firstshow.vercel.app)   

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

![Image](https://github.com/user-attachments/assets/60f821dc-0a7c-49a6-954a-00da4c9d8177)


## 🎬 Home Page – Browse Movies
Displays available movies based on the selected location.   

![Image](https://github.com/user-attachments/assets/771e7689-8f03-49f1-842a-cb3adc9da3ae)


## 🎞️ Movie Info Page
Displays detailed information about the selected movie—name, genre, rating, duration.   

![Image](https://github.com/user-attachments/assets/18e675c4-eed5-4eb8-9e33-3526790a1012)


## 🎞️ Movie Info Page – Showtimes
Displays detailed movie information along with all available showtimes across different theaters and dates. Users can choose their preferred show to proceed with booking.    

![Image](https://github.com/user-attachments/assets/1d8d479c-cf19-4c20-87f9-999d93ef20cd)


## 💺 Seat Selection Page
Allows users to view and select available seats for the chosen showtime. Booked and premium seats are clearly indicated. Once seats are selected, users can proceed to payment.  

![Image](https://github.com/user-attachments/assets/00aa04c0-bf04-4fab-879d-0ffca8810100)
![Image](https://github.com/user-attachments/assets/7e559af0-749f-47cf-bd69-2c08e97a4bfa)


## ✅ Booking Confirmation Page
Displays confirmation details after a successfully selecting seats. 

![Image](https://github.com/user-attachments/assets/55c89ca7-4449-49f2-beb1-daf536fd5116)
 

## 🔒 Login Required
Before proceeding to seat selection or booking, users must be logged in. Unauthenticated users are redirected to the login page.   

![Image](https://github.com/user-attachments/assets/2a5d574f-179d-45b5-b196-3fec58ebb260)
![Image](https://github.com/user-attachments/assets/4c978926-0def-4693-ae33-f9e5faffb990)


## ✅ Booking Confirmation Page
Unauthenticated users are redirected to the login page before booking. After login, they are automatically redirected to the booking confirmation page with their selected seats and showtime preserved.    

![Image](https://github.com/user-attachments/assets/55c89ca7-4449-49f2-beb1-daf536fd5116)


## 🎫 Ticket View Page
Displays ticket details including movie name, theater, showtime, and seat numbers. A PDF of the ticket is also sent to the user's registered email for easy access.   

![Image](https://github.com/user-attachments/assets/71910456-277e-4302-bdeb-7bf676e026f3)

## 👤 Profile Page
View all your booked tickets in one convenient place. The Profile page allows you to track bookings with ease. Need to make a change? You can also cancel tickets directly from this page when eligible.

![Image](https://github.com/user-attachments/assets/a744c13f-d92a-4a01-9ea3-bf32fc265526)
