# TimeBlock Service

A microservice for managing personal time blocks in the Schedule Planner application.

## 🚀 Features

- **CRUD Operations**: Create, Read, Update, Delete time blocks
- **Conflict Detection**: Automatic detection of scheduling conflicts
- **Student-specific**: Time blocks are associated with individual students
- **Flexible Scheduling**: Support for recurring and one-time time blocks
- **RESTful API**: Well-documented REST endpoints
- **Service Discovery**: Integrated with Eureka for microservices architecture

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA**
- **MySQL Database**
- **Spring Cloud Eureka Client**
- **OpenAPI 3 / Swagger**
- **Maven**

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Eureka Server running on port 8761

## 🔧 Configuration

### Environment Variables for AWS Deployment

Set these environment variables in AWS Elastic Beanstalk:

```bash
DB_HOST=mysql-training.cd4qsem4uf1x.us-west-2.rds.amazonaws.com
DB_PORT=3306
DB_NAME=team3capstonedb
DB_USERNAME=admin
DB_PASSWORD=Be.Cognizant2025!
```

### Local Development

The application can run locally with default values specified in `application.properties`.

## 🚀 Getting Started

### 1. Clone and Build

```bash
git clone <repository-url>
cd timeblock-service
mvn clean install
```

### 2. Run the Service

```bash
mvn spring-boot:run
```

The service will start on port `8084`.

### 3. Verify Service

- **Health Check**: `GET http://localhost:8084/api/timeblocks/health`
- **Swagger UI**: `http://localhost:8084/swagger-ui.html`
- **Eureka Registration**: Check `http://localhost:8761`

## 📚 API Endpoints

### TimeBlock Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/timeblocks/student/{studentId}` | Get all time blocks for a student |
| `GET` | `/api/timeblocks/student/{studentId}/day/{day}` | Get time blocks for a specific day |
| `GET` | `/api/timeblocks/{id}` | Get a specific time block by ID |
| `POST` | `/api/timeblocks` | Create a new time block |
| `PUT` | `/api/timeblocks/{id}` | Update an existing time block |
| `DELETE` | `/api/timeblocks/{id}` | Delete a time block |
| `GET` | `/api/timeblocks/student/{studentId}/type/{type}` | Get time blocks by type |
| `GET` | `/api/timeblocks/health` | Service health check |

### Request/Response Examples

#### Create Time Block

```json
POST /api/timeblocks
{
  "title": "Study Session",
  "day": "Monday",
  "startTime": "2:00 PM",
  "endTime": "4:00 PM",
  "type": "study",
  "description": "Mathematics study session",
  "color": "#FF5733",
  "studentId": 1,
  "weeks": 15
}
```

#### Response

```json
{
  "id": 1,
  "title": "Study Session",
  "day": "Monday",
  "startTime": "2:00 PM",
  "endTime": "4:00 PM",
  "type": "study",
  "description": "Mathematics study session",
  "color": "#FF5733",
  "studentId": 1,
  "weeks": 15
}
```

## 🔒 Security Features

- **Input Validation**: Comprehensive validation of all input data
- **Conflict Detection**: Automatic prevention of scheduling conflicts
- **Error Handling**: Robust error handling with meaningful messages
- **CORS Support**: Configured for frontend integration

## 📊 Database Schema

### time_blocks Table

| Column | Type | Description |
|--------|------|-------------|
| `time_block_id` | INT (PK) | Unique identifier |
| `title` | VARCHAR(255) | Time block title |
| `day` | VARCHAR(20) | Day of the week |
| `start_time` | VARCHAR(20) | Start time (12-hour format) |
| `end_time` | VARCHAR(20) | End time (12-hour format) |
| `type` | VARCHAR(50) | Type of time block |
| `description` | TEXT | Optional description |
| `color` | VARCHAR(20) | Display color |
| `student_id` | INT | Associated student ID |
| `weeks` | INT | Number of weeks (default: 15) |
| `created_at` | DATETIME | Creation timestamp |
| `updated_at` | DATETIME | Last update timestamp |

## 🐳 Docker Support

### Dockerfile

```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app

COPY target/timeblock-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build and Run

```bash
docker build -t timeblock-service:latest .
docker run -p 8084:8084 --env-file .env timeblock-service:latest
```

## 🌐 AWS Deployment

### Elastic Beanstalk

1. **Create Application**: Create new EB application
2. **Environment Variables**: Set database configuration
3. **Deploy**: Upload JAR file or use CodeBuild integration
4. **Health Check**: Verify `/api/timeblocks/health` endpoint

### CodeBuild Integration

The `buildspec.yml` file is configured for AWS CodeBuild:

- Builds with Java 17 (Corretto)
- Creates deployable JAR artifact
- Includes Procfile for EB deployment

## 🧪 Testing

### Unit Tests

```bash
mvn test
```

### Integration Testing

Test the API endpoints using the provided Swagger UI or tools like Postman.

## 📈 Monitoring

- **Health Endpoint**: `/api/timeblocks/health`
- **Actuator Endpoints**: `/actuator/health`, `/actuator/info`
- **Eureka Registration**: Automatic service registration and health checks

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the Apache License 2.0.

## 🆘 Support

For support and questions, contact the Schedule Planner development team. 