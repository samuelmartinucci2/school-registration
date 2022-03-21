## Environment:
- Java version: 1.8
- Maven version: 3.*
- Spring Boot version: 2.2.1.RELEASE

## Commands
- run:
```bash
mvn clean package; java -jar target/school-registration-1.0-SNAPSHOT.jar
```
- install:
```bash
mvn clean install
```
- test:
```bash
mvn clean test
```

## REST APIs:

Sample requests shared via Postman: https://www.getpostman.com/collections/5c531954366bff6064ec

Swagger UI is also available.

# Course (also available on /swagger-ui.html#/course-api-rest-controller)
- create:
`POST` request to `/course` :

```json
{"name": "course 1"}
```

- update:
`PUT` request to `/course/{courseId}` :

```json
{"name": "course 1"}
```

- list:
  `GET` request to `/course`

- find by id:
  `GET` request to `/course/{courseId}`

- delete:
  `DELETE` request to `/course/{courseId}`

- find by student enrolled:
  `GET` request to `/course/enrollment/{studentId}`

- find no enrollment courses:
  `GET` request to `/course/enrollment/none`

# Student (also available on /swagger-ui.html#/student-api-rest-controller)
- create:
  `POST` request to `/student` :

```json
{"name": "student 1"}
```

- update:
  `PUT` request to `/student/{studentId}` :

```json
{"name": "student 1"}
```

- list:
  `GET` request to `/student`

- find by id:
  `GET` request to `/course/{studentId}`

- delete:
  `DELETE` request to `/course/{studentId}`

- find by course enrolled:
  `GET` request to `/student/enrollment/{courseId}`

- find no subscription students:
  `GET` request to `/student/enrollment/none`

# Enrollment (also available on /swagger-ui.html#/enrollment-api-rest-controller)

- subscribe:
  `POST` request to `/enrollment/subscribe` :

```json
{"studentId": 1, "courseId": 1 }
```