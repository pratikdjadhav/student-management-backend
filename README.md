# 🎓 Student Management System - REST API

A secure and feature-rich Student Management REST API built with **Java**, **Spring Boot**, **MySQL**, and **JWT Authentication**. Designed for coaching centers to manage students, courses, and fee tracking efficiently.

🌐 **Live API:** https://student-management-backend-production-98f6.up.railway.app
📖 **Swagger Docs:** https://student-management-backend-production-98f6.up.railway.app/swagger-ui/index.html
🖥️ **Frontend:** https://student-management-system-ui.netlify.app
💻 **GitHub:** https://github.com/pratikdjadhav

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming Language |
| Spring Boot 3.x | Backend Framework |
| Spring Web | REST API |
| Spring Data JPA | Database ORM |
| Hibernate | JPA Implementation |
| MySQL | Relational Database |
| Spring Security | Authentication & Authorization |
| JWT (jjwt 0.11.5) | Stateless Token Authentication |
| BCrypt | Password Encryption |
| Swagger / OpenAPI | API Documentation |
| Lombok | Reduce Boilerplate Code |
| Maven | Build Tool |
| Railway | Cloud Deployment |

---

## 📁 Project Structure

```
src/main/java/dev/pratik/studentmanagement/
├── auth/
│   ├── AuthController.java          # Register & Login APIs
│   ├── AuthRequest.java             # Login/Register request body
│   └── AuthResponse.java            # JWT token response
├── controller/
│   ├── CourseController.java        # Course REST API endpoints
│   └── StudentController.java       # Student REST API endpoints
├── dto/
│   ├── StudentRequest.java          # Student input with validation
│   └── StudentResponse.java         # Student output with fee details
├── exception/
│   ├── ErrorResponse.java           # Standard error response
│   ├── GlobalExceptionHandler.java  # Catches all exceptions
│   └── ResourceNotFoundException.java # Custom 404 exception
├── model/
│   ├── Course.java                  # Course entity
│   ├── Student.java                 # Student entity
│   └── StudentStatus.java           # ACTIVE/INACTIVE enum
├── repository/
│   ├── CourseRepository.java        # Course database operations
│   └── StudentRepository.java       # Student database operations
├── security/
│   ├── JwtAuthenticationFilter.java # Validates JWT on every request
│   ├── JwtUtil.java                 # JWT generation & validation
│   ├── SecurityConfig.java          # Spring Security configuration
│   ├── SwaggerConfig.java           # Swagger/OpenAPI configuration
│   └── UserDetailsServiceImpl.java  # User authentication service
├── service/
│   ├── CourseService.java           # Course business logic
│   └── StudentService.java          # Student business logic
└── StudentManagementApplication.java
```

---

## 🏗️ Architecture

```
HTTP Request
     ↓
JwtAuthenticationFilter  (validates JWT token)
     ↓
Controller               (receives request)
     ↓
Service                  (business logic)
     ↓
Repository               (database operation)
     ↓
MySQL Database
     ↓
Response flows back to user
```

---

## 🔗 Entity Relationships

```
Course (One)  ←——→  Student (Many)
```

- One Course can have many Students
- Each Student belongs to one Course
- Student fees are calculated based on Course fees

---

## 🚀 Getting Started Locally

### Prerequisites
- Java 17+
- MySQL 8+
- Maven

### Setup

1. **Clone the repository**
```bash
git clone https://github.com/pratikdjadhav/student-management.git
cd student-management
```

2. **Create MySQL database**
```sql
CREATE DATABASE student_db;
```

3. **Configure database — create `application.properties`:**
```properties
spring.application.name=student-management
spring.datasource.url=jdbc:mysql://localhost:3306/student_db
spring.datasource.username=root
spring.datasource.password=your_password_here
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
jwt.secret=StudentManagementSecretKey123456789012345678901234
```

4. **Run the application**
```bash
mvn spring-boot:run
```

---

## 📡 API Endpoints

### 🔓 Auth APIs (Public)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### 🔐 Course APIs (JWT Required)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/courses` | Add a new course |
| GET | `/api/courses` | Get all courses |
| GET | `/api/courses/{id}` | Get course by ID |
| PUT | `/api/courses/{id}` | Update a course |
| DELETE | `/api/courses/{id}` | Delete a course |

### 🔐 Student APIs (JWT Required)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/students` | Add a new student |
| GET | `/api/students` | Get all students (paginated) |
| GET | `/api/students/{id}` | Get student by ID |
| PUT | `/api/students/{id}` | Update a student |
| DELETE | `/api/students/{id}` | Delete a student |
| GET | `/api/students/course/{courseId}` | Get students by course |
| GET | `/api/students/fees/collected` | Total collected fees |
| GET | `/api/students/fees/pending` | Total pending fees |

---

## 📝 Sample API Usage

### 1. Register
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "admin",
    "password": "123456"
}
```

### 2. Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "admin",
    "password": "123456"
}
```
Response:
```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

### 3. Add Course
```http
POST /api/courses
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
    "name": "BCA",
    "duration": "3 Years",
    "description": "Bachelor of Computer Applications",
    "fees": 25000
}
```

### 4. Add Student
```http
POST /api/students
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
    "name": "John Doe",
    "email": "john@gmail.com",
    "phone": "9876543210",
    "address": "Pune",
    "paidFees": 15000,
    "enrollmentDate": "2026-05-23",
    "status": "ACTIVE",
    "courseId": 1
}
```
Response:
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@gmail.com",
    "courseName": "BCA",
    "courseFees": 25000.0,
    "paidFees": 15000.0,
    "pendingFees": 10000.0,
    "status": "ACTIVE"
}
```

---

## 🔐 Security

- All APIs protected with JWT authentication
- Passwords encrypted using **BCrypt**
- JWT tokens expire after **10 hours**
- Stateless authentication — no server sessions
- Register and Login endpoints publicly accessible

---

## 👨‍💻 Author

**Pratik Jadhav**
- GitHub: [@pratikdjadhav](https://github.com/pratikdjadhav)
- LinkedIn: [Pratik Jadhav](https://linkedin.com/in/jadhavpratikd)

---

⭐ If you found this project helpful, please give it a star!
