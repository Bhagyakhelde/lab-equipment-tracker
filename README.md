# ECE Lab Equipment Tracking System

A desktop application to manage laboratory equipment borrowing and returns in an ECE department, built with Java, MySQL, JDBC, and Java Swing.

## Problem Statement
Equipment tracking in college ECE labs is often manual, leading to lost items and no visibility into who has what. This system solves that with a digital tracking solution.

## Features
- Role-based login (Admin / Student)
- Real-time equipment availability dashboard
- Borrow and Return equipment with automatic quantity updates
- Availability validation — prevents borrowing when stock is 0
- Overdue items report using SQL JOIN queries
- Admin dashboard with live stats (Total, Available, Borrowed)

## Tech Stack
- Language: Java
- UI: Java Swing
- Database: MySQL
- Connectivity: JDBC with PreparedStatement (SQL injection prevention)
- Tools: Eclipse IDE, MySQL Workbench

## Database Schema
3 normalized tables: users, equipment, borrow_records with foreign key relationships

## How to Run
1. Import the database schema into MySQL
2. Update DB credentials in DBConnection.java
3. Add MySQL Connector JAR to classpath
4. Run LoginScreen.java
5. Login: Username Admin User / Password admin123

## Screenshots

### Login Screen


![Login](Screenshot%20(103).png)

### Admin Dashboard
![Dashboard](Screenshot%20(104).png)

### Borrow Feature
![Borrow](Screenshot%20(105).png)
