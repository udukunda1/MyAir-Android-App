# MyAir REST API Server

Node.js + Express + MySQL (Sequelize) REST API server for MyAir Android App.

## Setup Instructions

### Prerequisites
- Node.js (v14 or higher)
- MySQL Server
- npm

### Installation

1. **Install dependencies**:
   ```bash
   npm install
   ```

2. **Configure MySQL**:
   - Create a MySQL database named `myair_db`
   - Update credentials in `config/database.js` if needed (default: root with no password)

3. **Start the server**:
   ```bash
   npm start
   ```

   Or for development with auto-reload:
   ```bash
   npm run dev
   ```

## API Endpoints

### Passengers

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/passengers` | Get all passengers |
| GET | `/api/passengers/:id` | Get single passenger |
| POST | `/api/passengers` | Create new passenger |
| PUT | `/api/passengers/:id` | Update passenger |
| DELETE | `/api/passengers/:id` | Delete passenger |

### Bookings

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/bookings` | Get all bookings |
| GET | `/api/bookings/passenger/:passengerId` | Get bookings by passenger |
| GET | `/api/bookings/:id` | Get single booking |
| POST | `/api/bookings` | Create new booking |
| PUT | `/api/bookings/:id` | Update booking |
| DELETE | `/api/bookings/:id` | Delete booking |

## Database Schema

### Passengers Table
- id (INTEGER, Primary Key)
- full_name (STRING)
- email (STRING, UNIQUE)
- phone (STRING)
- date_of_birth (DATE)
- membership_level (ENUM: Economy, Premium, Business, First Class)
- is_active (BOOLEAN)
- profile_image (STRING)

### Bookings Table
- id (INTEGER, Primary Key)
- passenger_id (INTEGER, Foreign Key → Passengers.id, CASCADE DELETE)
- flight_number (STRING)
- booking_date (DATE)
- seat_number (STRING)
- status (ENUM: Confirmed, Pending, Cancelled)

## Testing

Use Postman, Thunder Client, or curl to test endpoints:

```bash
# Get all passengers
curl http://localhost:3000/api/passengers

# Create a passenger
curl -X POST http://localhost:3000/api/passengers \
  -H "Content-Type: application/json" \
  -d '{"full_name":"John Doe","email":"john@example.com","phone":"1234567890","membership_level":"Premium","is_active":true}'
```

## For Android App

Update the IP address in Android app's `ApiConfig.java`:
- Find your computer's IP: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
- Use: `http://YOUR_IP_ADDRESS:3000`
- Example: `http://192.168.1.100:3000`
