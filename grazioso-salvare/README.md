# Grazioso Salvare Rescue Dashboard

This project is a full-stack MEAN application based on the original CS-340 Grazioso Salvare Rescue Dashboard. The application was redesigned using MongoDB, Express, Angular, and Node.js to create a more modern, dynamic, and interactive web experience.

The dashboard allows users to view and interact with rescue animal data through filtering, sorting, row selection, and map updates based on selected records. Additional enhancements include administrator features and reporting pages.

This project was created as part of the CS-499 Computer Science Capstone to demonstrate skills in software engineering, database integration, front-end development, and full-stack design.

---

# Technologies Used

* MongoDB
* Express.js
* Angular
* Node.js
* TypeScript
* HTML
* CSS
* JavaScript

---

# Features

* View rescue animal records
* Sort and filter records
* Highlight selected rows
* Dynamic map updates
* Angular front-end interface
* Express / Node.js REST API
* MongoDB database integration
* Administrative enhancements
* Expandable reporting pages

---

# Project Structure

CS499/
|-- client/   Angular front end
|-- server/   Node / Express backend
|-- README.md

---

# Prerequisites

Before running the project, make sure the following are installed:

* Node.js
* npm
* MongoDB
* Angular CLI

Install Angular CLI if needed:

npm install -g @angular/cli

---

# How to Run the Application

## 1. Clone the Repository

git clone https://github.com/gmccord-snhu/CS499.git
cd CS499

---

## 2. Start MongoDB

Make sure your MongoDB service is running locally.

Example connection:

mongodb://localhost:27017/AAC

---

## 3. Run the Server

Open a terminal:

cd server
npm install
node app.js

The backend API should now be running.

---

## 4. Run the Client

Open a second terminal:

cd client
npm install
ng serve

The Angular application should now be available at:

http://localhost:4200

---

# Usage

1. Launch the backend server
2. Launch the Angular client
3. Open the browser to http://localhost:4200
4. View rescue animal records
5. Sort, filter, and select rows
6. View map updates based on selected data

---

# Future Enhancements

* User login / administrator authentication
* Record editing and deletion
* Outcome reporting charts
* Improved UI styling
* Deployment to cloud hosting

---

# Purpose

This project was developed for academic and portfolio purposes as part of the SNHU CS-499 Capstone course.

---

# Author

Grant McCord
