# CS499 Computer Science ePortfolio

## Introduction
My name is Grant McCord. I am a Computer Science student at Southern New Hampshire University with a background in C++ development and professional experience in software validation and programming. Throughout my program, I have expanded my skill set into full stack web development, mobile applications, databases, and software engineering practices.

This ePortfolio highlights my growth through selected artifacts that I enhanced during my capstone course. These artifacts demonstrate my abilities in software design, algorithms and data structures, and database development.

---

## Table of Contents
- Professional Self-Assessment
- Code Review
- Software Design and Engineering: Android Inventory App Enhancement
- Algorithms and Data Structures: Grazioso Salvare MEAN Stack Enhancement
- Databases: Grazioso Salvare MEAN Stack Enhancement

---

## Professional Self-Assessment

Completing the Computer Science program and building this ePortfolio gave me the opportunity to reflect on how much I have grown as a developer. I entered the program with a strong background in C++ programming, software validation, and real-world industry experience, but the coursework helped me expand far beyond that foundation. Through the program, I developed practical skills in full-stack web development, mobile application development, database design, software engineering, and secure coding practices. Just as important, the capstone allowed me to organize that growth into a professional portfolio that demonstrates both technical ability and continued adaptability.

One of the biggest strengths highlighted through this portfolio is my ability to learn new technologies and apply them in meaningful ways. While my professional background has centered around C++, this program pushed me into areas that were new to me, including Android development with Java, SQLite database integration, and MEAN stack development using MongoDB, Express, Angular, and Node.js. Learning these technologies showed that I am not limited to one language or platform. Instead, I can transfer core computer science principles across different environments and continue growing as technology changes.

The program also strengthened my professional goals and values. At this stage of my career, my goal is not simply to add credentials, but to remain relevant, capable, and valuable in a constantly evolving industry. I value practical solutions, maintainable design, continuous learning, and building software that solves real problems for users. Completing this degree reinforced the idea that strong developers never stop learning, regardless of experience level.

Collaboration and communication are essential in computer science, and the program gave me many opportunities to practice both. Although many assignments were completed individually, software development always exists in a broader team environment. Throughout my career, I have worked with developers, testers, management, and stakeholders who each view a project from different perspectives. This program reinforced the importance of writing clear documentation, presenting technical ideas to non-technical audiences, and creating software that helps others make informed decisions. The written assignments, milestone submissions, code review video, and project documentation in this portfolio all reflect those communication skills.

My understanding of data structures and algorithms also grew throughout the program. These concepts are not only academic topics—they are the foundation of efficient software design. In my projects, I applied sorting, filtering, data organization, and structured retrieval to improve usability and performance. Whether managing inventory records in the Android application or processing rescue data in the Grazioso Salvare dashboard, I had to think carefully about how data should be stored, accessed, and presented to users.

Software engineering and database design were central themes across my artifacts. The Android Inventory App required designing a maintainable mobile application with a usable interface and reliable local data storage. The Grazioso Salvare enhancement expanded those skills into a full-stack architecture where the frontend, backend, and database all had to work together correctly. Building APIs, managing CRUD operations, handling routing, and designing data flow across multiple layers gave me a stronger understanding of how modern software systems are engineered.

Security was another important area of growth. Through secure coding coursework and related assignments, I learned to approach software with a security mindset. That includes thinking about vulnerabilities early, validating input, protecting sensitive data, controlling access, and recognizing how design decisions can create or reduce risk. Security is no longer something I view as a final step—it is part of the design process from the beginning.

The artifacts in this portfolio work together to show the full range of my abilities. The Android Inventory App demonstrates mobile development, user-focused design, and practical feature enhancement. The Grazioso Salvare MEAN stack project demonstrates full-stack engineering, database integration, algorithms, and scalable architecture. The code review ties these projects together by showing how I evaluate software, identify improvements, and communicate technical decisions. Together, these artifacts represent both my technical capabilities and my professional experience.

Overall, this ePortfolio reflects who I am as a developer: an experienced programmer who continues to adapt, learn, and grow. It demonstrates that I can combine industry experience with modern tools and current academic training to deliver practical, well-designed software solutions. 

---

## Code Review

[\[Code Review\]](https://youtu.be/iGguOwgDV1g)

In this code review, I walk through the original functionality of my applications, identify areas for improvement, and explain the enhancements I implemented. The focus is on improving structure, expanding functionality, and strengthening database integration.

---

## Software Design and Engineering: Android Inventory App Enhancement

### Artifact Description
The artifact used for this category is my Android Inventory Application, originally developed as part of my mobile development coursework. The application allows users to manage inventory items, including tracking stock levels and organizing product data.

### Justification for Inclusion
I selected this artifact because it demonstrates my ability to design and build a functional mobile application using Java and Android development tools. It also shows how I improved an existing application by expanding its functionality and improving its usability.

### Enhancements Implemented
The enhancements I made include adding stock status classification and implementing sorting functionality. These changes improved the usability of the application by making it easier to identify inventory levels and organize items efficiently.

![Dashboard](images/inv_app_main.jpg)

I also added a reports page to provide a visual overview of the inventory data. This page includes a bar chart that displays the number of items that are in stock, low stock, and out of stock. The goal of this feature was to make it easier to quickly understand inventory trends without needing to scan through individual records.

![Dashboard](images/inv_app_reports.jpg)

### Reflection

Working on the Android Inventory App enhancement gave me a better appreciation for what it takes to improve an existing application instead of simply building one from scratch. It is often easier to create a new project than to step back into older code, understand previous design decisions, and carefully extend it without breaking existing functionality. Through this process, I learned the importance of maintainable code structure, clear organization, and planning ahead for future changes. I also gained more experience working with Android activities, navigation, layouts, and connecting interface changes to the underlying data layer.

One of the biggest challenges was integrating the new features cleanly into the application. Adding stock status classification required updating how inventory quantities were interpreted and displayed throughout the app. The sorting functionality also required changes to how records were retrieved and presented so the user experience remained simple and responsive. Another challenge was creating the reports page and connecting chart data to the inventory records in a meaningful way. I had to think not only as a programmer, but also as an end user who needed quick access to useful information.

As I worked through these issues, I relied heavily on testing and iterative improvements. Small changes were made, tested, and refined until the application behaved as expected. Feedback from the course and from reviewing the project requirements helped guide these improvements. Rather than adding features just for complexity, I focused on changes that made the application more practical and easier to use.

The artifact was improved significantly from the original version. It moved beyond a basic inventory tracker into a more complete application that provides better organization, clearer stock visibility, and useful reporting tools. These enhancements made the project feel closer to something that could be used in a real workplace environment.

---

## Algorithms and Data Structures: Grazioso Salvare MEAN Stack Enhancement

### Artifact Description
This artifact is a MEAN stack web application based on the Grazioso Salvare project. It uses MongoDB, Express, Angular, and Node.js to manage and display animal rescue data.

![Dashboard](images/dashboard.jpg)

### Justification for Inclusion
Web stack development is a critical skill in today’s workplace, and I chose this project because it is complex enough to clearly demonstrate the course outcomes I’ve achieved. I knew going into it that it would require a significant amount of work, integration, and testing—and it did—but that is also what makes it valuable. This artifact shows my ability to design, build, and integrate a full-stack application while applying algorithms, data structures, and real-world development practices.

This project also demonstrates my ability to develop a single-page application (SPA) that interacts directly with a MongoDB database to retrieve, display, and edit records. Through this work, I was able to connect a frontend interface with a backend API and database, showing how data flows through the entire system in a practical, real-world scenario.


### Enhancements Implemented
Enhancements included implementing sorting and filtering logic for rescue data, as well as handling aggregated results from the database. These changes improved the efficiency and usability of the application by allowing users to quickly find relevant information.

### Reflection
This enhancement helped me better understand how data structures and algorithms are applied in real-world applications. One challenge was ensuring that data was processed efficiently while still keeping the code readable and maintainable.

---

## Databases: Grazioso Salvare MEAN Stack Enhancement

### Artifact Description
This artifact is the same MEAN stack application, with a focus on the database layer using MongoDB. The goal of this enhancement was to demonstrate how the application interacts with the database beyond basic data retrieval, and to show a better understanding of full CRUD operations and data flow between the front end and back end.

I added a basic admin login, record lookup, and edit functionality. The admin login uses a simple hard-coded username and password along with a session identifier. This was intentionally kept lightweight since the goal was to demonstrate how authentication connects across the stack rather than build a fully secure system. Even though it is simple, it shows how user access can be controlled before allowing changes to data.

![Dashboard](images/admin_panel.jpg)

I also implemented record lookup by ID, which allows a specific record to be retrieved from MongoDB instead of just displaying all records. This required updating the Express routes to support fetching a single document and connecting that to the Angular front end. Once a record is selected, the application routes to a separate edit page where the data can be modified.

![Dashboard](images/find_by_id.jpg)

The edit functionality interacts directly with the database by sending updated data back through the API and performing an update operation in MongoDB. Not every field is editable, but the main fields can be changed, which demonstrates how updates are handled in a real application. This also required handling state between pages and making sure the correct record data is loaded before editing.

![Dashboard](images/edit_outcome.jpg)

### Justification for Inclusion
I selected this artifact because it demonstrates my ability to design and interact with a database in a full stack application.

### Enhancements Implemented
I expanded the database functionality by implementing CRUD operations, adding API endpoints to retrieve and update records, and using MongoDB queries and aggregation to process data.

These enhancements allowed the application to support more complex interactions with the database and improved overall functionality.

### Reflection
The Grazioso Salvare MEAN stack enhancement was one of the most valuable projects in my capstone because it required bringing together multiple technologies into one working system. Unlike a smaller assignment focused on only one language or concept, this project required the frontend, backend, and database layers to communicate correctly. That integration process gave me a stronger understanding of how modern web applications are structured and how each layer depends on the others.

One of the biggest lessons I learned was how important architecture and planning are in full-stack development. Decisions made in the database schema affected the API design, and API decisions affected the Angular frontend. I also learned that debugging in a multi-layer application is very different from debugging a standalone program. A problem might come from the frontend request, backend routing, server logic, or database query, so troubleshooting required patience and a step-by-step process.

There were several challenges during development. Getting the server and client connected correctly took time, especially with routing, CORS configuration, and making sure data was returned in the expected format. I also wanted to preserve important features from the original dashboard, such as sorting, row highlighting, and map updates, while redesigning the project into a single-page web application. Later enhancements such as login functionality, record editing, and reporting pages required additional changes across multiple layers of the system.

The artifact was greatly improved from the original version. It evolved from a classroom dashboard into a more complete full-stack application with stronger separation of concerns, better user interaction, expandable features, and real database integration. It demonstrates a much deeper understanding of software engineering than the original project alone.

This enhancement strongly supported multiple course outcomes. It demonstrated my ability to design and evaluate computing solutions using algorithmic principles and sound computer science practices while balancing trade-offs in design choices. It also showed my ability to use modern tools and technologies by integrating MongoDB, Express, Angular, and Node.js into one working solution that delivers practical value. Through project documentation and presentation, it supported professional written and visual communication. By creating a dashboard intended to present useful data for users and decision making, it also reflected collaborative and organizational goals. Finally, developing the application required a security mindset through consideration of data handling, user access, and secure design practices.

---

## Conclusion

The artifacts included in this portfolio represent my growth as a developer throughout the Computer Science program. From mobile development to full stack applications, I have gained experience in multiple areas of software development.

These projects demonstrate my ability to design, build, and enhance applications while applying best practices in software engineering, algorithms, and database management.