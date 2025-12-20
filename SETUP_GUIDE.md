# MyAir Assignment 4 - Setup & Testing Guide

## 🎯 Overview

This guide will help you set up and test the complete MyAir system with:
- **Backend**: Node.js + Express + MySQL REST API Server
- **Frontend**: Android App with Volley network integration
- **Connection**: Localhost via USB tethering or same WiFi network

---

## 📋 Prerequisites

Before starting, ensure you have:
- ✅ Node.js installed (v14 or higher)
- ✅ MySQL Server installed and running
- ✅ Android Studio
- ✅ Physical Android device OR emulator
- ✅ USB cable (if using tethering)

---

## 🗄️ Step 1: Setup MySQL Database

### Option A: Using MySQL Command Line

```bash
# Open MySQL command line
mysql -u root -p

# Create database
CREATE DATABASE myair_db;

# Exit
exit;
```

### Option B: Using MySQL Workbench

1. Open MySQL Workbench
2. Connect to your local MySQL server
3. Run the SQL script: `MyAirServer/setup_database.sql`
4. Or manually create database: `CREATE DATABASE myair_db;`

### Configure Database Credentials

If your MySQL has a password or different username:

1. Open `MyAirServer/config/database.js`
2. Update line 4:
   ```javascript
   const sequelize = new Sequelize('myair_db', 'YOUR_USERNAME', 'YOUR_PASSWORD', {
   ```

---

## 🚀 Step 2: Start the Node.js Server

### Install Dependencies

```bash
cd c:\Users\educa\AndroidStudioProjects\MyAir\MyAirServer
npm install
```

### Start Server

```bash
npm start
```

### Expected Output

```
✓ Database connection established successfully
✓ Database models synchronized
═══════════════════════════════════════════════════
✓ MyAir REST API Server running on port 3000
✓ Server URL: http://localhost:3000
✓ API Documentation: http://localhost:3000
═══════════════════════════════════════════════════

Available endpoints:
  Passengers: /api/passengers
  Bookings:   /api/bookings

Press Ctrl+C to stop the server
```

### Test Server

Open browser and visit: `http://localhost:3000`

You should see the API documentation JSON.

---

## 🌐 Step 3: Find Your Computer's IP Address

### Windows

```bash
ipconfig
```

Look for **IPv4 Address** under your active network adapter:
- **WiFi**: Wireless LAN adapter Wi-Fi
- **Tethering**: Ethernet adapter (when phone is connected)

Example output:
```
IPv4 Address. . . . . . . . . . . : 192.168.1.100
```

### Mac/Linux

```bash
ifconfig
```

Look for `inet` address under your active network interface.

---

## 📱 Step 4: Configure Android App

### Update Server IP Address

1. Open Android Studio
2. Navigate to: `app/src/main/java/com/example/myair/ApiConfig.java`
3. Update line 11 with your computer's IP:

```java
public static final String BASE_URL = "http://192.168.1.100:3000";
```

**Replace `192.168.1.100` with YOUR computer's IP address!**

### Sync Gradle

1. Click "Sync Now" when prompted
2. Wait for Gradle sync to complete

---

## 🔌 Step 5: Connect Phone to Server

### Option A: USB Tethering (Recommended)

1. **Enable USB Tethering on Phone**:
   - Settings → Network & Internet → Hotspot & Tethering
   - Enable "USB Tethering"

2. **Find Tethered IP**:
   ```bash
   ipconfig
   ```
   Look for Ethernet adapter (usually `192.168.42.xxx`)

3. **Update ApiConfig.java**:
   ```java
   public static final String BASE_URL = "http://192.168.42.129:3000";
   ```

### Option B: Same WiFi Network

1. **Connect Both Devices**:
   - Connect phone to WiFi
   - Connect computer to same WiFi network

2. **Use Computer's WiFi IP**:
   ```java
   public static final String BASE_URL = "http://192.168.1.100:3000";
   ```

---

## ✅ Step 6: Test the Application

### Build and Run

1. In Android Studio, click **Run** (green play button)
2. Select your device
3. Wait for app to install and launch

### Test CRUD Operations

#### Test 1: Create Passenger
1. Open MyAir app
2. Tap "Manage Passengers"
3. Fill in passenger form:
   - Full Name: John Doe
   - Email: john@example.com
   - Phone: +1234567890
   - Date of Birth: Select any date
   - Membership: Premium
   - Active: Checked
4. Tap "Save Passenger"
5. ✅ Should see: "Passenger saved successfully"
6. ✅ Check server logs: Should see POST request

#### Test 2: View Passengers
1. Switch to "View All" tab
2. ✅ Should see: "Synced with server" toast
3. ✅ Should see: John Doe in the list
4. ✅ Check server logs: Should see GET request

#### Test 3: Update Passenger
1. Tap **Edit** icon on John Doe
2. Change name to "John Smith"
3. Tap "Update Passenger"
4. ✅ Should see: "Passenger updated successfully"
5. ✅ Check list: Name should be updated
6. ✅ Check server logs: Should see PUT request

#### Test 4: Create Booking
1. Tap on John Smith card to view details
2. Tap blue **+** button
3. Fill in booking form:
   - Flight Number: UA123
   - Booking Date: Select future date
   - Seat Number: 12A
   - Status: Confirmed
4. Tap "Save Booking"
5. ✅ Should see: "Booking saved successfully"
6. ✅ Should see: Booking appears in passenger details
7. ✅ Check server logs: Should see POST request

#### Test 5: Delete Passenger
1. Go back to "View All" tab
2. Tap **Delete** icon on John Smith
3. Confirm deletion
4. ✅ Should see: "Passenger deleted"
5. ✅ Passenger should disappear from list
6. ✅ Check server logs: Should see DELETE request

### Test Offline Mode

1. **Turn off WiFi/Tethering**
2. Try to create a passenger
3. ✅ Should see: "Saved locally. Server sync failed"
4. ✅ Passenger should still appear in list (from SQLite)
5. **Turn WiFi/Tethering back on**
6. Create another passenger
7. ✅ Should see: "Passenger saved successfully"

---

## 📦 Step 7: Build APK

### Build Debug APK

```bash
cd c:\Users\educa\AndroidStudioProjects\MyAir
./gradlew assembleDebug
```

### Locate APK

APK will be at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Install APK on Device

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

Or manually:
1. Copy APK to phone
2. Open file manager on phone
3. Tap APK file
4. Allow installation from unknown sources
5. Install

---

## 🔍 Verification Checklist

### Server Verification
- [ ] MySQL database `myair_db` created
- [ ] Server starts without errors
- [ ] Can access `http://localhost:3000` in browser
- [ ] Server logs show requests when app is used

### Android App Verification
- [ ] App installs successfully
- [ ] No crash on launch
- [ ] Can create passenger (saves locally + syncs to server)
- [ ] Can view passengers (loads from server)
- [ ] Can update passenger (updates locally + syncs to server)
- [ ] Can delete passenger (deletes locally + syncs to server)
- [ ] Can create booking (saves locally + syncs to server)
- [ ] Offline mode works (saves to SQLite when server unreachable)

### Network Verification
- [ ] Phone can ping computer's IP
- [ ] Server receives requests from Android app
- [ ] JSON data flows correctly
- [ ] Error handling works gracefully

---

## 🐛 Troubleshooting

### "Unable to connect to server"

**Solution**:
1. Check server is running: `npm start`
2. Verify IP address in `ApiConfig.java` matches computer's IP
3. Check firewall isn't blocking port 3000
4. Ensure phone and computer are on same network (or tethering is enabled)

### "Database connection failed"

**Solution**:
1. Ensure MySQL server is running
2. Check database credentials in `config/database.js`
3. Verify database `myair_db` exists

### "Synced locally. Server sync failed"

**Solution**:
1. This is normal if server is unreachable
2. Data is saved to SQLite (offline mode)
3. Check server logs for errors
4. Verify network connection

### APK Installation Blocked

**Solution**:
1. Enable "Install from Unknown Sources" in phone settings
2. Or use `adb install` command

---

## 📊 Testing with Postman (Optional)

You can test server endpoints directly:

### Create Passenger
```
POST http://YOUR_IP:3000/api/passengers
Content-Type: application/json

{
  "full_name": "Test User",
  "email": "test@example.com",
  "phone": "1234567890",
  "date_of_birth": "1990-01-01",
  "membership_level": "Economy",
  "is_active": true
}
```

### Get All Passengers
```
GET http://YOUR_IP:3000/api/passengers
```

### Delete Passenger
```
DELETE http://YOUR_IP:3000/api/passengers/1
```

---

## 🎓 Assignment Requirements Met

✅ **Requirement 1**: Web-based application with local database  
✅ **Requirement 2**: CRUD operations on 2+ linked tables  
✅ **Requirement 3**: Retrieve lists as JSON (Object/Array)  
✅ **Requirement 4**: NetworkActivity invoked from app  
✅ **Requirement 5**: RecyclerView + Form + Volley POST  
✅ **Requirement 6**: Display in app + Volley GET  
✅ **Requirement 7**: Localhost only (tethering)  
✅ **Requirement 8**: APK bundle created  

---

## 📝 Notes

- **Dual Storage**: App uses both SQLite (local) and Server (remote) for best user experience
- **Offline First**: All operations save to SQLite first, then sync to server
- **Graceful Degradation**: App works offline if server is unreachable
- **Real-time Sync**: Server data is fetched on app launch and after operations

---

## 🎉 Success!

If all tests pass, you have successfully completed Assignment 4! 🚀

Your MyAir app now has:
- ✅ Full CRUD operations
- ✅ Network communication via Volley
- ✅ JSON data exchange
- ✅ Localhost server integration
- ✅ Dual storage (SQLite + Server)
- ✅ Production-ready APK
