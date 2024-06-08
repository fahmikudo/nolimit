# Metrics Employee Services
## About The Services
The service is just a simple metrics employee REST service. It uses an elasticsearch as a source of data.
There are several endpoints that will be provided from this service, as follows:
1. Count of Employees
2. Average Salary
3. Minimum and Maximum Salary
4. Age Distribution
5. Gender Distribution
6. Marital Status Distribution
7. Date of Joining Histogram
8. Top Interests
9. Designation Distribution

## Technology
For building and running the application it's need:
* Java 17
* Maven 3.x.x
* Spring Boot 3.x.x
* Docker

## How to run application
### Running the application with docker
There are several ways to run this service. However, now we will try to run this application using Docker
1. Go to root folder of metrics employee
2. Open terminal then run this command
````
docker compose up -d
````
3. Execute this command using cURL to initialize index on elasticsearch
````
curl -XPUT 'http://127.0.0.1:9200/companydatabase?pretty' \
-H 'Content-Type: application/json' \
-d '{
    "mappings" : {
        "employees" : {
            "properties" : {
                "FirstName" : { "type" : "text" },
                "LastName" : { "type" : "text" },
                "Designation" : { "type" : "text", "fielddata": true },
                "Salary" : { "type" : "integer" },
                "DateOfJoining" : { "type" : "date", "format": "yyyy-MM-dd" },
                "Address" : { "type" : "text" },
                "Gender" : { "type" : "text", "fielddata": true },
                "Age" : { "type" : "integer" },
                "MaritalStatus" : { "type" : "text", "fielddata": true },
                "Interests" : { "type" : "text", "fielddata": true }
            }
        }
    }
}'
````
4. Initialize data into `companydatabase` index with command. You can change the path file of `Employee50k.json`
````
curl -XPOST "http://127.0.0.1:9200/companydatabase/_bulk" --header "Content-Type: application/json" --data-binary "@C:\\Employees50K.json
````

## Guides
The following guides illustrate how to use or call some endpoint. You can use tool for http request like cURL or Postman.
1. Get Count of Employee
````
Request:
curl --location 'http://localhost:8900/api/employees/count-of-employees'

Response:
{
    "code": 200,
    "message": "SUCCESS",
    "data": 50000
}
````
2. Get Average Salary
````
Request:
curl --location 'http://localhost:8900/api/employees/average-salary'

Response:
{
    "code": 200,
    "message": "SUCCESS",
    "data": 57843.9
}
````
3. Get Minimum and Maximum Salary
````
Request:
curl --location 'http://localhost:8900/api/employees/minimum-maximum-salary'

Response:
{
    "code": 200,
    "message": "SUCCESS",
    "data": {
        "maximum_salary": 154000.0,
        "minimum_salary": 25000.0
    }
}
````
4. Get Age Distribution
````
Request:
curl --location 'http://localhost:8900/api/employees/age-distribution'

Response:
{
    "code": 200,
    "message": "SUCCESS",
    "data": {
        "65.0": 36,
        "35.0": 2810,
        "20.0": 3572,
        "40.0": 658,
        "45.0": 424,
        "25.0": 25766,
        "50.0": 339,
        "55.0": 188,
        "30.0": 16066,
        "60.0": 141
    }
}
````
5. Get Gender Distribution
````
Request:
curl --location 'http://localhost:8900/api/employees/gender-distribution'

Response:
{
    "code": 200,
    "message": "SUCCESS",
    "data": {
        "female": 24917,
        "male": 25083
    }
}
````
6. Get Marital Status Distribution
````
Request:
curl --location 'http://localhost:8900/api/employees/marital-status-distribution'

Response:
{
    "code": 200,
    "message": "SUCCESS",
    "data": {
        "unmarried": 25001,
        "married": 24999
    }
}
````
7. Get Date of Joining Histogram
````
Request:
curl --location 'http://localhost:8900/api/employees/joining-date-histogram'

Response:
{
    "code": 200,
    "message": "SUCCESS",
    "data": {
        "1984-11-01": 0,
        "2016-05-01": 98,
        "1970-06-01": 0,
        "2015-12-01": 67,
        "2001-07-01": 8
    }
}
````
8. Get Top Interests
````
Request:
curl --location 'http://localhost:8900/api/employees/top-interests'

Response:
{
    "code": 200,
    "message": "SUCCESS",
    "data": {
        "a": 3788,
        "cars": 2614,
        "making": 3193,
        "diving": 2609,
        "r": 2589,
        "music": 3278,
        "c": 2589,
        "to": 3845,
        "collecting": 9555,
        "watching": 3811
    }
}
````
9. Get Designation Employees
````
Request:
curl --location 'http://localhost:8900/api/employees/designation-distribution'

Response:
{
    "code": 200,
    "message": "SUCCESS",
    "data": {
        "senior": 8601,
        "software": 38713,
        "manager": 1806,
        "resource": 860,
        "architect": 860,
        "team": 4300,
        "trainee": 4300,
        "engineer": 38713,
        "human": 860,
        "lead": 4300
    }
}
````
Or you can see it using openAPI by using this link `http://localhost:8900/swagger-ui.html`
![metrics-employee-swagger](https://github.com/fahmikudo/nolimit/assets/20161826/0dd4e1ef-0d6d-43eb-abe1-021bc5e03b19)


