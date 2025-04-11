# Insurance Purchase API Documentation

## Authentication

All endpoints except registration require authentication. Include the JWT token in the Authorization header:

```bash
Authorization: Bearer <your_jwt_token>
```

### Getting a JWT Token

To get a JWT token, you need to authenticate using your credentials:

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "password": "securePassword123"
  }'
```

Response will contain your JWT token:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "expiresIn": 3600
}
```

## User Management

### Register a New User

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

### Get User by ID

```bash
curl -X GET http://localhost:8080/api/users/{userId} \
  -H "Authorization: Bearer <your_jwt_token>"
```

### Get User by Username

```bash
curl -X GET http://localhost:8080/api/users/username/{username} \
  -H "Authorization: Bearer <your_jwt_token>"
```

## Insurance Management

### Get All Insurances

```bash
curl -X GET http://localhost:8080/api/insurances \
  -H "Authorization: Bearer <your_jwt_token>"
```

### Get Insurance by ID

```bash
curl -X GET http://localhost:8080/api/insurances/{insuranceId} \
  -H "Authorization: Bearer <your_jwt_token>"
```

### Get Insurances by Category

```bash
curl -X GET http://localhost:8080/api/insurances/category/{category} \
  -H "Authorization: Bearer <your_jwt_token>"
```

Valid categories: `LIFE`, `HEALTH`, `AUTO`, `HOME`, `TRAVEL`

### Get Recommended Insurances

```bash
curl -X GET http://localhost:8080/api/insurances/recommended \
  -H "Authorization: Bearer <your_jwt_token>"
```

This endpoint returns insurance policies recommended for the authenticated user based on their profile information (age, gender, income).

### Get Insurances by Type

```bash
curl -X GET http://localhost:8080/api/insurances/type/{type} \
  -H "Authorization: Bearer <your_jwt_token>"
```

### Get Active Insurances

```bash
curl -X GET http://localhost:8080/api/insurances/active/{true|false} \
  -H "Authorization: Bearer <your_jwt_token>"
```

## Purchase Management

### Purchase Insurance

```bash
curl -X POST http://localhost:8080/api/purchases \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_jwt_token>" \
  -d '{
    "insuranceId": "uuid-of-insurance"
  }'
```

### Get User's Purchases

```bash
curl -X GET http://localhost:8080/api/purchases \
  -H "Authorization: Bearer <your_jwt_token>"
```

### Get Purchase by ID or Policy Number

```bash
curl -X GET http://localhost:8080/api/purchases/{idOrPolicyNumber} \
  -H "Authorization: Bearer <your_jwt_token>"
```

You can provide either:

- A purchase UUID (e.g., `123e4567-e89b-12d3-a456-426614174000`)
- A policy number (e.g., `POL-12345678-12345678-20240409123456`)

### Download Policy Document

```bash
curl -X GET http://localhost:8080/api/purchases/{idOrPolicyNumber}/policy \
  -H "Authorization: Bearer <your_jwt_token>" \
  -o policy_document.pdf
```

This endpoint returns a PDF file containing the insurance policy document. The file will be saved locally with the name specified after the `-o` parameter.

## Response Examples

### Insurance List Response

```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Life Insurance Premium",
    "description": "Comprehensive life insurance coverage",
    "category": "LIFE",
    "premium": 100.0,
    "coverageAmount": 100000.0,
    "active": true,
    "minAge": 30,
    "maxAge": 50,
    "minIncome": 50000.0,
    "recommendedGender": "ALL"
  }
]
```

### Purchase Response

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "policyNumber": "POL-12345678-12345678-20240409123456",
  "purchaseDate": "2024-04-09T10:00:00",
  "premium": 100.0,
  "status": "ACTIVE",
  "policyDocumentPath": "policies/policy_POL-12345678-12345678-20240409123456.pdf",
  "insurance": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Life Insurance Premium",
    "description": "Comprehensive life insurance coverage",
    "category": "LIFE",
    "premium": 100.0,
    "coverageAmount": 100000.0
  },
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "john.doe",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```

## Notes

1. Replace `{userId}`, `{username}`, `{insuranceId}`, `{idOrPolicyNumber}`, `{category}`, and `{type}` with actual values
2. Replace `<your_jwt_token>` with the actual JWT token received after authentication
3. The server URL (`http://localhost:8080`) should be replaced with the actual deployed server URL
4. All requests except registration require authentication
5. The policy document download will save a PDF file to your local directory
6. Policy numbers follow the format: `POL-[User ID Prefix]-[Insurance ID Prefix]-[Timestamp]`
