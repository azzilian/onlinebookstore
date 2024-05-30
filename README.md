<div align="center">
<img alt="Redoc logo" src="doc/images/vecteezy_cat-with-book-studying-logo-mascot-character-design_23286095.png"
width="200px" />

# Online Bookstore

</div>

## Introduction

Greetings! Allow me to introduce you to the Online Bookstore app.
This project encompasses the essential functionalities needed to operate a real-time bookstore, such as:
  
  User registration and authorization

  User shopping cart: includes functional from filling the cart with products to creating an order

  Creating and managing books available in the store and orders by store personnel

# Why Online Bookstore?

This project will appeal to individuals who need a simple and efficient tool to solve business problems
without being burdened by unnecessary features.
It does not require special training for users,
whether they are store personnel or customers.
Features

# How It Works

Video - //TODO

Thanks to the integrated Liquibase, administrators do not need to manually create tables and relationships.
They only need to create the database and then populate it with new products.
Users need to register or log in by sending the appropriate request.
After authorization, users can use their JWT token or registration details to send requests,
add desired items to their cart by sorting books by categories or from the general list
and create an order.
As the order is processed, the administrator updates the order status,
allowing the user to track the progress of their order.
In short here is the list of features it can do

    1. User Registration and Authentication
    2. Book Browsing and Filtering by Categories
    3. Shopping Cart Management - add, update, delete books in cart
    4. Order Processing - create order using items in cart, check users orders
    5. Admin Dashboard for Managing Books, Book Categories and Orders
    6. Secure RESTful API Endpoints


# Installation
## Setup Instructions

Clone the repository:

    git clone git@github.com:azzilian/onlinebookstore.git

Build the project using Maven:

    mvn clean install

Configure the database: Create a new MySQL database:

    CREATE DATABASE bookstoreDB;

Update the application.properties file with your MySQL database credentials (Usage of environment variables recommended):

    spring.datasource.url=jdbc:mysql://localhost/bookstoreDB?serverTimezone=UTC
    spring.datasource.username=${USERNAME}
    spring.datasource.password=${PASSWORD}
    spring.jackson.deserialization.fail-on-unknown-properties=true
    spring.datasource.driver-class=com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=validate
    spring.jpa.show-sql=true
    jwt.expiration=${EXPIRATION}
    jwt.secret=${SECRETKEY}

Install dependencies and build the project:

    mvn clean install

Run the application:

    mvn spring-boot:run

The server will start on http://localhost:8080.

Access the API documentation at:

    http://localhost:8080/swagger-ui.html

## Running Tests

To run the tests, use the following Maven command:

    mvn test

## Installation using Docker

Build the Docker image:

    docker build -t books-service .

Run the Docker compose:

     docker-compose up --build    

# Endpoints

## User Endpoints

<details>
  <summary>Register a new user</summary>

- URL: `/api/auth/register`
- Method: `POST`
- Request Body: JSON with user details (email, password, repeatPassword, firstName, lastName, shippingAddress)
- Example Request:
  ```json
  {
   "email": "john.doe@example.com",
   "password": "securePassword123",
   "repeatPassword": "securePassword123",
   "firstName": "John",
   "lastName": "Doe",
   "shippingAddress": "123 Main St, City, Country"
  }

- Response Status: `201 Created`
</details>

<details>
<summary>Authenticate a user</summary>

- URL: `/api/auth/login`
- Method: `POST`
- Request Body: JSON with user details (email, password)
- Example Request:
  ```json
   {
    "email": "john.doe@example.com",
    "password": "securePassword123"
  }
    ```
- Response Status: `200 OK`  
</details>

## Book Endpoints

<details>
  <summary>Create a New Book</summary>

- URL: `/api/books`
- Method: `POST`
- Request Body: JSON with book details (title, author, ISBN, price, description, cover image, category IDs)
- Example Request:
  ```json
  {
    "title": "BookTitle",
    "author": "Book Author",
    "isbn": "9780306406157",
    "price": 10.50,
    "description": "BookDescription",
    "coverImage": "ImageUrl",
    "categoryIds": [1]
  }
  ```
- Response Status: `201 Created`
</details>

<details>
  <summary>Get All Books</summary>

- URL: `/api/books`
- Method: `GET`
- Example Response:
  ```json
  [
    {
      "id": 1,
      "title": "Lenore",
      "author": "Edgar Allan Poe",
      "isbn": "9780306406157",
      "price": 10.50,
      "description": "test",
      "coverImage": "test@test.com",
      "categoryIds": [1]
    }
  ]
  ```
- Response Status: `200 OK`
</details>

<details>
  <summary>Find Book by ID</summary>

- URL: `/api/books/{id}`
- Method: `GET`
- Example Response:
  ```json
  {
    "id": 1,
    "title": "Lenore",
    "author": "Edgar Allan Poe",
    "isbn": "9780306406157",
    "price": 10.50,
    "description": "test",
    "coverImage": "test@test.com",
    "categoryIds": [1]
  }
  ```
- Response Status: `200 OK`
</details>

<details>
  <summary>Update Book</summary>

- URL: `/api/books/{id}`
- Method: `PUT`
- Request Body: JSON with updated book details
- Response Status: `200 OK`
</details>

<details>
  <summary>Delete Book</summary>

- URL: `/api/books/{id}`
- Method: `DELETE`
- Response Status: `204 No Content`
</details>

## Category Endpoints

<details>
  <summary>Create a New Category</summary>

- URL: `/api/categories`
- Method: `POST`
- Request Body: JSON with category details (name, description)
- Response Status: `201 Created`
</details>

<details>
  <summary>Get All Categories</summary>

- URL: `/api/categories`
- Method: `GET`
- Response Status: `200 OK`
</details>

<details>
  <summary>Find Category by ID</summary>

- URL: `/api/categories/{id}`
- Method: `GET`
- Response Status: `200 OK`
</details>

<details>
  <summary>Update Category</summary>

- URL: `/api/categories/{id}`
- Method: `PUT`
- Request Body: JSON with updated category details
- Response Status: `200 OK`
</details>

<details>
  <summary>Delete Category</summary>

- URL: `/api/categories/{id}`
- Method: `DELETE`
- Response Status: `204 No Content`
</details>

## Shopping cart Endpoints
<details>
  <summary>Retrieve user's shopping cart</summary>

- URL: `/api/cart`
- Method: `GET`
- Response Status: `200 OK`

</details>

<details>
  <summary>Add book to the shopping cart</summary>

- URL: `/api/cart`
- Method: `POST`
- Request Body: JSON with book details(bookId, quantity)
- Example Request:
  ```json
  {
    "bookId": 1,
    "quantity": 12
  }
  ``` 
- Response Status: `200 OK`
</details>

<details>
  <summary>Update quantity of a book in the shopping cart</summary>

- URL: `/api/cart/cart-items/{cartItemId}`
- Method: `PUT`
- Request Body: JSON with updated book details(quantity)
- Example Request:
  ```json
  {
    "quantity": 12
  }
  ``` 
- Response Status: `200 OK`
</details>

<details>
  <summary>Remove a book from the shopping cart</summary>

- URL: `/api/cart/cart-items/{cartItemId}`
- Method: `DELETE`
- Response Status: `204 No Content`
</details>


## Order Endpoints

<details>
  <summary>Place an order</summary>

- URL: `/api/orders`
- Method: `POST`
- Request Body: JSON with order details(shippingAddress)
- Example Request:
  ```json
  {
     "shippingAddress": "City, Street ave, 1"
  }
  ``` 
- Response Status: `200 OK`
</details>

<details>
  <summary>Update order status</summary>

- URL: `/api/orders/{id}`
- Method: `PATCH`
- Request Body: JSON with update order details(orderStatus - PENDING, COMPLETED, DELIVERING)
- Example Request:
  ```json
  {
     "orderStatus" : "COMPLETED"
  }
  ``` 
- Response Status: `200 OK`
</details>

<details>
  <summary>Retrieve user's order history</summary>

- URL: `/api/orders`
- Method: `GET`
- Response Status: `200 OK`

</details>

<details>
  <summary>Retrieve all OrderItems for a specific order</summary>

- URL: `/api/orders/{orderId}/items`
- Method: `GET`
- Response Status: `200 OK`

</details>

<details>
  <summary>Retrieve a specific OrderItem within an order</summary>

- URL: `/api/orders/{orderId}/items/{itemId}`
- Method: `GET`
- Response Status: `200 OK`

</details>

# Technologies used:

Maven: Project management and dependency resolution.

Spring Boot: Core framework for building the application.

Spring Data JPA: Manages database interactions.

Spring Security & JwtUtil: Handles authentication and authorization.

MySQL: Main database for the application.

Liquibase: Manages database migrations.

H2 Database: In-memory database for testing.

JUnit & Mockito: For unit testing and mocking.

Swagger: API documentation and testing tool.

Checkstyle: Ensures code style consistency.

Docker: Application testing & deployment

# System requirements
- Java 17
- Maven 3.8+
- Spring Boot 2.7+
- MySQL 8.0+

# Contributing
Vladyslav Vyshynskyi