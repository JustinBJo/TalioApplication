# TALIO Application

Talio is a multi-board task management application built with Spring Boot and JavaFX.
It allows users to create and manage tasks in a highly visual and organized manner using boards, lists, and cards.
The application supports client-server communication via HTTP and JSON, and can handle multiple clients simultaneously.

## Description of project

Talio is designed to help users manage their tasks efficiently using a client-server architecture.
It supports multiple boards, each containing various lists and cards to organise tasks.
Users can create, edit, and delete these entities, and can also collaborate with others in real-time.

The application provides additional features such as nested task lists, task descriptions, and synchronization over multiple clients.
A user can also use admin mode to access and edit all the boards that reside in the server.
The password to access the admin features is "admin1234".


## Group members

| Profile Picture | Name        | Email                       |
|---|-------------|-----------------------------|
| ![](https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/2613/avatar.png?width=400) | Justin Jo | B.Jo@student.tudelft.nl       |
| ![](https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/6091/avatar.png?width=400) | Francisco Cunha | F.SiqueiraCarneirodaCunhaNeto-1@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Vlad Ionita | V.Ionita@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Luca-Serban Ionescu | ionescu-8@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Maria Cristescu | m.a.cristescu@student.tudelft.nl |
| ![](https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/4881/avatar.png?width=400) | Renyi Yang      | R.Yang-7@student.tudelft.nl                        | 


## How to run it
To set up the development environment and run the application, follow these steps:

### Prerequisites
- Java JDK 19 or later
- An IDE such as IntelliJ or Eclipse

### Cloning the repository
Clone the repository to your local machine using the following command:
```
git clone https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-45.git
```

### Running the application
1. Navigate to the server/src/main/java/server directory.
2. Run the Main class to start the server application.
3. Navigate to the client/src/main/java/client directory.
4. Run the Main class to start the client application.
5. Enter the server's IP address in the client application to connect to the server.
   - The default server is running on port 8080 on your local machine.


## How to contribute to it
1. Log in to gitlab.ewi.tudelft.nl using TUDelft SSO.
2. Clone the repository to your local machine.
3. Create a new branch for the feature or bugfix you're working on.
4. Commit your changes and push them to your branch.
5. Create a merge request from your branch to the main branch when you're ready for your changes to be reviewed.


## Copyright / License
- Managed by TUDelft CSE1105 Team
- Developed by Justin Jo, Francisco Cunha, Luca Ionescu, Maria Cristescu, and Vlad Ionita

