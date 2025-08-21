# StockNest API

This is a comprehensive Stock Trading and Portfolio Management API built with Spring Boot.

## Features

- User Authentication & Authorization (JWT)
- Stock Portfolio Management
- AngelOne Trading API Integration
- Stock Data & Analysis
- Admin Panel
- Email Notifications
- Download Services

## API Endpoints

### Authentication

- POST `/api/auth/register` - User registration
- POST `/api/auth/login` - User login

### AngelOne Trading

- POST `/api/angelone/login` - AngelOne OAuth login
- GET `/api/angelone/profile` - Get user profile
- GET `/api/angelone/holdings` - Get holdings
- POST `/api/angelone/order` - Place order
- POST `/api/angelone/logout` - Logout

### Portfolio Management

- GET `/api/portfolio/user/{userId}` - Get user portfolio
- POST `/api/portfolio` - Create/Update portfolio
- DELETE `/api/portfolio/{id}` - Delete portfolio

## Setup

1. Clone the repository
2. Configure `application.properties` with your MongoDB URI and JWT secret
3. Set up AngelOne API credentials
4. Run with `mvn spring-boot:run`

## Deployment

The application is configured for deployment on Render.com with automatic builds from GitHub.

Environment Variables Required:

- `MONGODB_URI`
- `JWT_SECRET`
- `ANGELONE_API_KEY`
- `ANGELONE_API_SECRET`

Last updated: 2025-08-21
