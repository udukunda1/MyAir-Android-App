# MyAir - United Airlines Mobile App

A modern Android application for United Airlines passengers, featuring flight management and passenger account management.

## Features

### Main Features
- **Flight Status** - Check real-time flight status
- **Book a Flight** - Search and book flights
- **Check-in** - Mobile check-in for flights
- **Passenger Management** - Manage passenger accounts and bookings

### Passenger Management System
- ✅ **Add Passengers** - Create new passenger accounts with complete profile information
- ✅ **Edit Passengers** - Update existing passenger details
- ✅ **View All Passengers** - Browse all registered passengers in a list
- ✅ **View Passenger Details** - Click any passenger to see full profile and booking history
- ✅ **Delete Passengers** - Remove passenger accounts with confirmation
- ✅ **Profile Images** - Upload and display passenger profile photos
- ✅ **Form Validation** - Real-time validation for all input fields

### Flight Booking System
- ✅ **Book Flights** - Create flight bookings linked to passengers
- ✅ **View Booking History** - See all bookings for each passenger
- ✅ **Booking Status** - Track booking status (Confirmed/Pending/Cancelled)
- ✅ **Color-Coded Status** - Visual indicators for booking status

## Database Schema

### PassengerAccounts Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER (PK) | Auto-increment primary key |
| full_name | TEXT | Passenger full name |
| email | TEXT | Email address |
| phone | TEXT | Phone number |
| date_of_birth | TEXT | Date of birth |
| membership_level | TEXT | Economy/Premium/Business/First Class |
| is_active | INTEGER | Active status (1=active, 0=inactive) |
| profile_image | TEXT | Path to profile image |

### FlightBookings Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER (PK) | Auto-increment primary key |
| passenger_id | INTEGER (FK) | References PassengerAccounts.id |
| flight_number | TEXT | Flight number |
| booking_date | TEXT | Date of booking |
| seat_number | TEXT | Assigned seat |
| status | TEXT | Booking status |

**Relationship**: FlightBookings.passenger_id → PassengerAccounts.id (CASCADE DELETE)

## Technology Stack

- **Language**: Java
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Database**: SQLite
- **UI Components**: 
  - Material Design Components
  - ViewPager2 for tabbed interface
  - RecyclerView for lists
  - Material DatePicker
  - TextInputLayout for forms

## Project Structure

```
app/src/main/
├── java/com/example/myair/
│   ├── MainActivity.java              # Main landing page
│   ├── PassengerActivity.java         # Passenger management activity
│   ├── PassengerDetailsActivity.java  # Passenger details view
│   ├── BookFlightActivity.java        # Flight booking form
│   ├── AddPassengerFragment.java      # Form fragment for add/edit
│   ├── PassengerListFragment.java     # List fragment with RecyclerView
│   ├── PassengerAdapter.java          # Passenger RecyclerView adapter
│   ├── BookingAdapter.java            # Booking RecyclerView adapter
│   ├── DatabaseHelper.java            # SQLite database helper
│   ├── Passenger.java                 # Passenger model class
│   └── FlightBooking.java             # Flight booking model class
├── res/
│   ├── layout/
│   │   ├── activity_main.xml          # Main screen layout
│   │   ├── activity_passenger.xml     # Passenger activity layout
│   │   ├── activity_passenger_details.xml # Passenger details layout
│   │   ├── activity_book_flight.xml   # Flight booking form layout
│   │   ├── fragment_add_passenger.xml # Form layout
│   │   ├── fragment_passenger_list.xml# List layout
│   │   ├── item_passenger.xml         # Passenger card layout
│   │   └── item_booking.xml           # Booking card layout
│   └── values/
│       ├── strings.xml                # All text strings
│       ├── colors.xml                 # Color definitions
│       └── dimens.xml                 # Dimension values
└── AndroidManifest.xml
```

## Setup Instructions

### Prerequisites
- Android Studio (latest version)
- JDK 11 or higher
- Android SDK 36

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

### Build Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test
```

## Usage

### Accessing Passenger Management
1. Launch the app
2. Click on **"Manage Passengers"** button on the main screen
3. Use the tabs to switch between "Add Passenger" and "View All"

### Adding a New Passenger
1. Go to **"Add Passenger"** tab
2. Fill in all required fields:
   - Full Name (required)
   - Email (required, validated)
   - Phone Number (required)
   - Date of Birth (required)
   - Membership Level (dropdown)
   - Active Account (checkbox)
   - Profile Image (optional)
3. Click **"Save Passenger"**

### Editing a Passenger
1. Go to **"View All"** tab
2. Click the **Edit** icon on any passenger card
3. Modify the fields as needed
4. Click **"Update Passenger"**

### Viewing Passenger Details
1. Go to **"View All"** tab
2. **Click on any passenger card**
3. View complete profile and booking history

### Booking a Flight
1. From Passenger Details screen, click the **blue "+" button**
2. Fill in flight details:
   - Flight Number (required)
   - Booking Date (required)
   - Seat Number (required)
   - Status (Confirmed/Pending/Cancelled)
3. Click **"Save Booking"**

### Deleting a Passenger
1. Go to **"View All"** tab
2. Click the **Delete** icon on any passenger card
3. Confirm deletion in the dialog
4. Note: All bookings for that passenger are also deleted (CASCADE)

## Form Validation

The app includes comprehensive form validation:
- ✅ Required field checks
- ✅ Email format validation
- ✅ Real-time error messages
- ✅ Toast notifications for success/error states

## Permissions

- `READ_MEDIA_IMAGES` - Required for selecting profile images from gallery (Android 13+)

## Design Principles

- **Material Design** - Follows Google's Material Design guidelines
- **United Airlines Branding** - Uses official United Airlines colors
- **Resource Management** - All strings, colors, and dimensions in resource files
- **No Hardcoded Values** - Maintains flexibility and easy customization

## Color Scheme

- **Primary Blue**: `#002244` (united_blue)
- **Light Blue**: `#64B5F6` (united_light_blue)
- **Yellow**: `#F9A825` (united_yellow)
- **Dark Blue**: `#001020` (united_dark_blue)

## Assignment Requirements Met

✅ Activity3 loads data from database (SQLite)  
✅ Invoked from previous assignment (MainActivity button)  
✅ CRUD operations (Create, Read, Update, Delete)  
✅ Form with all required input types:
  - Text fields (Name, Email, Phone)
  - Date picker (Date of Birth)
  - Dropdown (Membership Level)
  - Checkbox (Active Account)
  - Image upload (Profile Image)  
✅ Real-world objects (Passenger Accounts)  
✅ Dynamic fragment loading (ViewPager2)  
✅ Proper styling (dimens, colors, strings, styles)  
✅ RecyclerView widget with dynamic updates  
✅ Two related database tables (PassengerAccounts ↔ FlightBookings)  
✅ Each table has ≥3 columns (8 and 6 columns respectively)

## Known Issues

None at this time.

## Future Enhancements

- [ ] Add search/filter functionality for passenger list
- [ ] Add passenger statistics dashboard
- [ ] Export passenger data to CSV
- [ ] Add biometric authentication
- [ ] Implement cloud sync for passenger data
- [ ] Add booking modification/cancellation
- [ ] Implement flight search and real-time availability

## License

This is an educational project for Android development coursework.

## Author

Developed as part of Android Development Assignment

## Version History

- **v1.1** (2025-12-17) - Added passenger details and flight booking features
  - Passenger details view with full profile display
  - Flight booking system with status tracking
  - Click passenger cards to view details
  - Color-coded booking status indicators
  - Floating action button for quick booking access
  - Demonstrates two-table database relationship with CASCADE DELETE

- **v1.0** (2025-12-17) - Initial release with passenger management feature
  - SQLite database with 2 related tables
  - Complete CRUD operations
  - Material Design UI
  - Form validation
  - Image upload functionality
