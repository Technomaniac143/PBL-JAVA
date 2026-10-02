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
Get-ChildItem -Recurse *.java | Where-Object { $_.FullName -notmatch "\\frontend\\" } | ForEach-Object { $_.FullName } > sources.txt
javac -d out @sources.txt
```

### Execution:
Once compiled, you can run the `MainMenu` class, which serves as the entry point to your CLI application:

```powershell
java -cp out com.orderprocessing.main.MainMenu
```

*Note: If you run this and get a JDBC Driver error, it means you need to download the `mysql-connector-java.jar` and include it in your classpath (`java -cp "out;mysql-connector-java.jar" com.orderprocessing.main.MainMenu`). For the scope of the PBL demo, the application catches the SQLException and continues seamlessly.*

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
