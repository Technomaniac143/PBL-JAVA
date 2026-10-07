# Project Setup & Execution Guide

This document outlines the necessary commands to compile, run, and interact with both the Java Backend (PBL Implementation) and the React Frontend.

## 1. Running the Java Backend (CLI Application)

The backend is built purely in Java with an Object-Oriented design and relies on standard JDBC for database interactions.

### Prerequisites:
- Ensure you have **Java (JDK 17 or later)** installed on your machine.
- (Optional) A local instance of MySQL running on `localhost:3306` if you wish to test actual database persistence.

### Compilation:
Open your terminal (Command Prompt or PowerShell) at the root of the `java project` folder and run the following command to compile all Java files and place the compiled `.class` files into a new `out` directory:

```powershell
# Windows (PowerShell)
(Get-ChildItem -Recurse src/*.java).FullName | Out-File -FilePath sources.txt -Encoding utf8
javac -d out -cp "lib/mysql-connector-j-8.3.0.jar" (Get-Content sources.txt)
```

### Execution:
Once compiled, you can run the application using any of the following commands:

```powershell
# Option A: Run executable JAR directly (Recommended)
java -jar App.jar

# Option B: Run via out directory
java -cp out com.orderprocessing.main.MainMenu

# Option C: Run specifying lib JAR
java -cp "out;lib/mysql-connector-j-8.3.0.jar" com.orderprocessing.main.MainMenu
```

*Note: The MySQL JDBC driver (`com.mysql.cj.jdbc.Driver`) is embedded and automatically registered. If a local MySQL server is active on `localhost:3306`, orders persist directly to database `food_db`. If MySQL server is offline, the application seamlessly operates in simulated database mode for PBL presentation.*

---

## 2. Running the React Frontend

The stunning minimalist frontend was built using Vite and React.

### Prerequisites:
- Ensure you have **Node.js** (v16+) installed.

### Setup & Execution:
Navigate to the frontend directory, install the necessary dependencies, and start the development server:

```powershell
# 1. Navigate into the frontend folder
cd frontend

# 2. Install dependencies (only required the first time)
npm install

# 3. Start the development server
npm run dev
```

Once the server starts, open your browser and navigate to:
**http://localhost:5173**
