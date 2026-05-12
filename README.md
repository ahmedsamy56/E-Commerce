# E-Commerce Backend & Frontend Project using Servlet

This project is a full-stack E-Commerce system that provides a complete shopping experience for users and a management dashboard for administrators. It is built using Clean Architecture on the backend and Angular on the frontend.

---

## User Features

* User registration and login
* JWT-based authentication and authorization
* View all products
* Filter products by category
* View detailed product information
* Add product reviews (rating + comment)
* Edit and delete own reviews
* View all personal reviews in profile
* Add products to cart
* Checkout with delivery details
* View order history
* Track order status updates
* Delete account

---

## Admin Features


* Simple statistics overview
* Full CRUD operations for categories
* Full CRUD operations for products
* View all orders
* View order details
* Update order status

---

## Backend Architecture

The backend follows **Clean Architecture** with 4 layers:

* Core Layer
* Application Layer
* Infrastructure Layer
* API Layer

### Tech Features

* Repository Pattern
* DTO Mapping using Object Mapper
* Centralized Error Handling using ErrorHandlerFilter
* FluentValidation is used for input validation (inspired by .NET’s)  [(Repo)](https://github.com/BurakKontas/fluentvalidation)
* Standard API Response using Response<T> and ResponseHandler
* Authentication & Authorization
* Rate Limiting using Redis
* Caching using Redis
* Dockerized backend
* Docker Compose for full stack setup (Backend + MySQL + Redis)
* Database initialization inside container
* CI/CD pipeline using GitHub Actions

---

## Frontend

* Built using Angular
* Integrated with backend APIs
* Supports both user and admin


## Database Design [dbdiagram](https://dbdiagram.io/d/69fb79c17a923b947236bdc4)


![Database Diagram](/Client/E-Commerce/public/Untitled.png)

