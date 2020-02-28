Our code style is based on [GoogleCodeStyle](https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml).
In Idea, please, manually adjust setting 'hard wrap at' to 150 symbols and 'Class count to use import with *' to 10 imports.

#### How to run Surwave-be in Docker:

**Prerequisites:** Docker and JDK 11 installed (make sure that JAVA_HOME environment variable points to JDK 11 path).

1 - Download project sources
2 - Create `application-local.yml` file and copy `application-local.yml.example` content to it.

Next, in console in project root:<br/>
3 - Run build gradle task:
```
./gradlew build
```
4 - Run docker containers build: 
```
docker-compose build
```

5 - Run created containers:
```
CORE_PROFILES=local docker-compose up -d
```

Swagger should be available after several seconds at http://localhost:8080/swagger-ui.html

For default basic security credentials will be "guest/guest".

**If you want to run backend part with Google authorization set authType property to `oauth2` in `application-local.yml` or run last step without CORE_PROFILES**
