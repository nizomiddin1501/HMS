#Hotel Management System#

#Overview#
Hotel Management System is a web application for hotels to manage various operations like booking, user management, payment processing and order tracking. The system provides interfaces for users, rooms and reservations, as well as the ability to manage payments. This project includes role-based access control (RBAC) and comprehensive administrative functions for efficient hotel business management.

#Features#
##User Management:
Registration and login functionality for users.
Profile management (updating user information).
Role-based access control for administrators and general users.

##Room Management:
View available rooms.
Reservation, renewal and cancellation of rooms.
Manage rooms for administrators (CRUD operations).

##Order Management:
Create a new order (booking).
Update and cancel order details.
Order status management (PENDING, CONFIRMED, CANCELED).
Integration with the payment system.

##Payment System:
Payment processing (PENDING, PAID, FAILED, REFUNDED states).
Order confirmation after successful payment.
Automatic order cancellation in case of payment failure.

##Booking Management:
Choose check-in and check-out dates.
Real-time monitoring of room availability.
Summary of bookings for users.

##Administrative Dashboard:
Management of rooms, users and orders.
View detailed reports on bookings and payments.

#Technology Stack 

##Backend:

Java (Spring Boot framework)
Hibernate (ORM for database handling)
JPA (Java Persistence API for database interaction)
Maven (Build tool)
 
##Database:

PostgreSQL (Relational database management system)
APIs:

Swagger (API documentation and testing)
Tools and Libraries:

Lombok (To reduce boilerplate code)
Spring Security (For user authentication and authorization)

#Project Structure#

hotel-management-system/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── revolusion/developers/hms/            # Project source code
│   │   │       ├── config/                           # Configuration classes for security and application settings
│   │   │       ├── controller/                       # REST Controllers for handling incoming API requests
│   │   │       ├── entity/                           # JPA Entity classes (Order, Room, Payment, User, etc.)
│   │   │       ├── exceptions/                       # Custom exceptions for error handling
│   │   │       ├── payload/                          # Data Transfer Objects (DTOs) for passing data between layers
│   │   │       ├── repository/                       # Data access layer for database interaction
│   │   │       ├── service/                          # Business logic for handling core application operations
│   │   └── resources/
│   │       ├── static/                               # Static resources (CSS, JS, images, etc.)
│   │       ├── templates/                            # Thymeleaf HTML templates for front-end views
│   │       ├── application.properties                # Application configuration properties
│   └── test/
│       └── java/                                     # Test cases for controllers, services, and repositories
│
└── README.md                                         # Project documentation and instructions


#Entities and Operations#

1. User Entity
2. UserPayment Entity
3. Hotel
4. Room
5. RoomCategory
6. Role
7. Review
8. Order
9. Payment
#Order and Payment Flow#
##Order Creation:##
User selects a room, sets check-in and check-out dates, and places an order.
Order status is set to PENDING.

##Payment Creation:##
After creating an order, a payment is created with status PENDING.\

##Payment Update:##
rPayment status is updated to PAID or FAILED based on the payment processing result.
If payment is PAID, order status is updated to CONFIRMED.
If payment is FAILED, order status is updated to CANCELLED.

##Order Completion:##
Once the stay is completed, order status can be updated to COMPLETED.

#APIs#
The project is well-documented using Swagger. You can explore all available APIs through the following Swagger documentation URL:
http://localhost:8080/swagger-ui/index.html

#API Endpoints:#
##1. User API:
GET /api/users/{id}: Get User by ID
PUT /api/users/{id}: Update user
DELETE /api/users/{id}: Delete User
GET /api/users: Get all Users with Pagination
POST /api/users: Create a new User

##2. UserPayment API:
GET /api/userPayments/{id}: Get UserPayment by ID
PUT /api/userPayments/{id}: Update userPayment
DELETE /api/userPayments/{id}: Delete UserPayment
GET /api/userPayments: Get all UserPayments with Pagination
POST /api/userPayments: Create a new UserPayment

##3. Room API:
GET /api/rooms/{id}: Get Room by ID
PUT /api/rooms/{id}: Update room
DELETE /api/rooms/{id}: Delete Room
GET /api/rooms: Get all Rooms with Pagination
POST /api/rooms: Create a new Room

##4. RoomCategory API: 
GET /api/roomCategories/{id}: Get RoomCategory by ID
PUT /api/roomCategories/{id}: Update roomCategory
DELETE /api/roomCategories/{id}: Delete RoomCategory
GET /api/roomCategories: Get all RoomCategories with Pagination
POST /api/roomCategories: Create a new RoomCategory

##5. Role API:
GET /api/roles/{id}: Get Role by ID
PUT /api/roles/{id}: Update role
DELETE /api/roles/{id}: Delete Role
GET /api/roles: Get all Roles with Pagination
POST /api/roles: Create a new Role

##6. Review API:
GET /api/reviews/{id}: Get Review by ID
PUT /api/reviews/{id}: Update review
DELETE /api/reviews/{id}: Delete Review
GET /api/reviews: Get all Reviews with Pagination
POST /api/reviews: Create a new Review

##7. Payment API:
GET /api/payments/{id}: Get Payment by ID
PUT /api/payments/{id}: Update payment
DELETE /api/payments/{id}: Delete Payment
GET /api/payments: Get all Payments with Pagination
POST /api/payments: Create a new Payment

##8. Order API:
GET /api/orders/{id}: Get Order by ID
PUT /api/orders/{id}: Update order
DELETE /api/orders/{id}: Delete Order
GET /api/orders: Get all Orders with Pagination
POST /api/orders: Create a new Order

##9. Hotel API:
GET /api/hotels/{id}: Get Hotel by ID
PUT /api/hotels/{id}: Update hotel
DELETE /api/hotels/{id}: Delete Hotel
GET /api/hotels: Get all Hotels with Pagination
POST /api/hotels: Create a new Hotel

#Installation#
To set up the project locally, follow these steps:

#Prerequisites
#Java 11 or higher installed.
#Maven installed.
#PostgreSQL installed and running.
#Git installed.

#Steps#
##1. Clone the Repository:
   git clone  https://github.com/nizomiddin1501/HMS.git
##2. Navigate to the Project Directory:
   cd hotel-management-system
##3. Database Setup:
   *Create a PostgreSQL database named hotel_management.
   *Create necessary tables or let Hibernate handle it via JPA.
   *Update the application.properties file with your PostgreSQL credentials:

     spring.datasource.url=jdbc:postgresql://localhost:5432/HMS
     spring.datasource.username=postgres
     spring.datasource.password=1234

     spring.jpa.hibernate.ddl-auto=update
     spring.jpa.show-sql=true
     springdoc.swagger-ui.enabled=true 
     spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

##4. Run the Application:
   *Use Maven to build and run the application:
     mvn clean install
     mvn spring-boot:run   
##5. Access the Application:
   for browcer 
     http://localhost:8080
   Access Swagger API documentation at:
     http://localhost:8080/swagger-ui/index.html

#Contact#
*Email: nizomiddinmirzanazarov@gmail.com
*GitHub: https://github.com/nizomiddin1501
*My Portfolio: https://nizomiddin-portfolio.netlify.app/
