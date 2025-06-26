# CRM System with AI Task Assistant

## 📌 Overview
This is a Customer Relationship Management (CRM) system built with Java Spring Boot. It includes AI integration using Spring AI and Ollama to help project managers generate tasks using natural language.

## 💡 Features

- User registration and login with JWT authentication
- Role-based access control (Admin, User)
- Position based access (CEO, Project Manager, Team Lead, Senior, Middle, Junior)
- Project and task management
- AI assistant that creates tasks, projects from natural language prompts
- Task dashboard for team members
- Clean layered architecture (Controller, Service, Repository)
- MySQL database
- Local LLM integration via Ollama (Llama3.2 model)

## 🤖 AI Assistant
You can type prompts like:

`Assign a task to John to create a marketing report for Company X.`

The AI will generate:
- Title: Marketing Report
- Description
- Recommendations
- Deadline (7 days by default)

## 🧱 Technologies Used

- Java 17
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA
- Spring AI
- Ollama (local LLM hosting)
- Thymeleaf
- MySQL
- Maven

## 🗂️ Project Structure
```
src
├───main
│   ├───java
│   │   └───com
│   │       └───noxsid
│   │           └───nks
│   │               └───crmai
│   │                   ├───auth        # Auth logic
│   │                   ├───config      # JWT and security configs
│   │                   ├───controllers # Web controllers (MVC)  
│   │                   ├───data        # Data model classes     
│   │                   ├───handler     # Handlers to control log in/sign up  
│   │                   ├───jwt         # Jwt Filter
│   │                   ├───repository  # JPA interfaces
│   │                   ├───service     # Business logic
│   │                   └───tools       # Tools for ai model
│   └───resources                       # Application properties
│       ├───static                  
│       │   └───css                     # Style for View
│       └───templates                   # Html templates for View
│           └───fragments               # Html fragments like sidebar...
└───test
    └───java
         └───com
             └───noxsid
                 └───nks
                     └───crmai          # Test cases
```

## 🛡️ Security

- JWT authentication
- Role-based access to endpoints
- Token entity to manage sessions

## 📦 Database (MySQL)

Main entities:
- `User`
- `Project`
- `Task`
- `Token`

Relationships:
- User ↔ Project (Many-to-Many)
- Project → Task (One-to-Many)
- User → Task (One-to-Many)

## 🧪 Testing

- Unit tests for service layer
- Integration tests for auth and task flows

## ▶️ How to Run

1. Clone the repository:
```
git clone https://github.com/noxs1d/aicrm
cd aicrm
```
2. Run Ollama locally:
```bash
   ollama run llama3.2
```

3. Configure `application.properties`:
```properties
spring.application.name=crmai
server.port=8000
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.database=mysql
spring.datasource.url= YOUR-JDBC_URL
spring.datasource.username= YOUR-USERNAME
spring.datasource.password= YOUR-PASSWORD
security.jwt.secret_key= YOUR-SECRET_KEY
security.jwt.access_token_expiration=36000000
security.jwt.refresh_token_expiration=252000000
#ai
spring.ai.ollama.chat.options.model=llama3.2
```
4. Start the app:
```
./mvnw spring-boot:run
```
5. Open in browser: http://localhost:8000

## 📎 Figma UI
    
Figma Design [Link](https://www.figma.com/design/bZDzuJ47aOnx7FmTHFBr0u/Untitled?node-id=0-1&p=f&t=wvkhUEcPjP2Dpv2q-0) 

## 📌 Future Improvements
 - Add calendar and timeline views

 - Save AI prompt history

 - Improve multi-user chat interface

 - Add file attachments to tasks

