#  Quora Clone – Backend Project

A backend implementation of a **Quora-like Question & Answer platform**, built using **Spring Boot** with **JWT-based authentication** and **role-based authorization**. This project focuses on clean architecture, real-world backend practices, and scalable API design.

---

##  Project Overview

This project replicates the **core backend functionality of Quora**, allowing users to:

* Register and log in securely
* Ask questions
* Answer questions
* Vote on questions and answers
* Manage authentication & authorization using JWT

It is designed as a **learning-focused but production-style backend**, following best practices like DTO usage, layered architecture, validation, and exception handling.

---

##  Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security** (JWT Authentication)
* **Spring Data JPA**
* **Hibernate**
* **MySQL** (Database)
* **Gradle**
* **Postman** (API testing)

---

##  Architecture

The project follows a **layered architecture**:

```
Controller  →  Service  →  Repository  →  Database
     ↓            ↓
    DTOs      Business Logic
```

### Key Layers

* **Controller Layer** – Handles HTTP requests & responses
* **Service Layer** – Contains business logic
* **Repository Layer** – Database interaction using JPA
* **DTO Layer** – Request/Response objects for clean APIs
* **Security Layer** – JWT, filters, authentication & authorization

---

##  Authentication & Security

* JWT token is **generated only at login**, not at registration
* Spring Security uses **UserDetails & AuthenticationProvider**
* Passwords are securely stored using **BCrypt**
* Role-based access control for protected APIs

### Auth Flow

1. User registers → No token generated
2. User logs in → JWT token generated
3. Token is sent in headers for protected APIs

```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

---

##  Major Features

###     User

* Register
* Login (JWT-based)

###      Question

* Create question
* View questions
* Vote / Unvote question

###      Answer

* Post answer
* Update answer
* Vote / Unvote answer

###     Voting System

* Upvote & Downvote support
* Prevents duplicate votes
* Clean service-level logic

---

##   Important Packages

```
com.example.QuoraBackend
│
├── controller        # REST Controllers
├── service           # Business logic
├── repository        # JPA repositories
├── dto               # Request & Response DTOs
├── entity            # Database entities
├── security          # JWT, filters, config
├── exception         # Global exception handling
└── configuration     # App & security config
```

---

##   API Testing

* APIs tested using **Postman**
* Proper HTTP status codes used (200, 201, 400, 401, 403)
* Validation errors handled gracefully

---

##  Current Status

Backend APIs completed

Authentication & Authorization stable

Voting system implemented

DTO & validation structure finalized

Frontend integration in progress

---

##  Future Enhancements

* Pagination & sorting
* Comment system
* User profile management
* Admin moderation
* Caching with Redis

---

## vision

This project represents a strong step toward mastering **Spring Boot backend development**, focusing on fundamentals, best practices, and real-world problem-solving.

> *"Where curiosity becomes knowledge."*
