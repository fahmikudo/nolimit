# Blog Post Service
## About The Services
The service is just a simple REST service to provide blog post application. It uses a spring boot with mysql.
There are several endpoints that will be provided from this service, as follows:
1. Endpoint User Registration
2. Endpoint User Login
3. Endpoint Get All Posts
4. Endpoint Get Post By Id
5. Endpoint Create Post with authenticated user
6. Endpoint Edit Post with authenticated user
7. Endpoint Delete Post with authenticated user

## Technology
For building and running the application it's need:
* Java 17
* Maven 3.x.x
* Spring Boot 3.x.x
* MySQL 8.x.x
* Mockito
* Docker

## How to run application
### Running the application with docker
There are several ways to run this service. However, now we will try to run this application using Docker
1. Go to root folder of blog post
2. Open terminal then run this command
````
docker compose up -d
````
3. Open URL `http://localhost:8901/swagger-ui.html` to see API Documentation 
4. To stop application run this command
````
docker compose down
````

## API Documentation
