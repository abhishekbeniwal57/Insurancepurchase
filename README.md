# Insurance Purchase API

A Spring Boot application for managing insurance policies, purchases, and policy document generation.

## Features

- User registration and authentication
- List available insurance policies
- Purchase insurance policies
- Download policy documents
- Recommend insurance policies based on user demographics

## Tech Stack

- Java 17
- Spring Boot 3.2.0
- Spring Security with JWT
- H2 Database (in-memory)
- iText PDF for document generation
- Maven for dependency management

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:

   ```
   git clone https://github.com/yourusername/insurance-purchase-api.git
   cd insurance-purchase-api
   ```

2. Build the project:

   ```
   mvn clean install
   ```

3. Run the application:

   ```
   mvn spring-boot:run
   ```

   Alternatively, use the provided scripts:

   - Windows: `start.bat` or `run.bat`
   - PowerShell: `start.ps1`

The application will be available at `http://localhost:8080`

## API Usage

See the [API_DOCUMENTATION.md](API_DOCUMENTATION.md) file for detailed API documentation.

### Basic Workflow

1. Register a user:

   ```bash
   curl -X POST http://localhost:8080/api/users/register \
     -H "Content-Type: application/json" \
     -d '{
       "username": "john.doe",
       "password": "securePassword123",
       "email": "john.doe@example.com",
       "firstName": "John",
       "lastName": "Doe",
       "age": 30,
       "gender": "MALE",
       "income": 75000
     }'
   ```

2. List available insurances:

   ```bash
   curl -X GET http://localhost:8080/api/insurances \
     -H "Authorization: Bearer <your_jwt_token>"
   ```

3. Get recommended insurances:

   ```bash
   curl -X GET http://localhost:8080/api/insurances/recommended \
     -H "Authorization: Bearer <your_jwt_token>"
   ```

4. Purchase an insurance:

   ```bash
   curl -X POST http://localhost:8080/api/purchases \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <your_jwt_token>" \
     -d '{
       "insuranceId": "uuid-of-insurance"
     }'
   ```

5. Download policy document:
   ```bash
   curl -X GET http://localhost:8080/api/purchases/{purchaseId}/policy \
     -H "Authorization: Bearer <your_jwt_token>" \
     -o policy.pdf
   ```

## Deployment

### Deploy to AWS Elastic Beanstalk

1. Package the application:

   ```
   mvn clean package
   ```

2. Create a new Elastic Beanstalk application:

   - Go to AWS Elastic Beanstalk console
   - Click "Create Application"
   - Enter application name and description
   - Choose "Java" platform and appropriate version
   - Upload the JAR file from `target/insurance-purchase-api-0.0.1-SNAPSHOT.jar`
   - Configure environment settings (Database, monitoring, etc.)
   - Click "Create application"

3. Configure environment variables:
   - In the Elastic Beanstalk console, go to Configuration > Software
   - Add environment variables such as JWT_SECRET, DATABASE_URL, etc.

### Deploy to Heroku

1. Create a `Procfile` in the root directory:

   ```
   web: java -jar target/insurance-purchase-api-0.0.1-SNAPSHOT.jar
   ```

2. Initialize Git repository (if not already done):

   ```
   git init
   git add .
   git commit -m "Initial commit"
   ```

3. Create a Heroku app and deploy:

   ```
   heroku create
   git push heroku master
   ```

4. Configure environment variables:
   ```
   heroku config:set JWT_SECRET=your_jwt_secret
   ```

## License

This project is licensed under the MIT License - see the LICENSE file for details.
