Our code style is based on [GoogleCodeStyle](https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml).
In Idea, please, manually adjust setting 'hard wrap at' to 150 symbols and 'Class count to use import with *' to 10 imports.

#### How to run Surwave-be in Docker:

**Prerequisites:** Gradle, Docker and jdk11 installed (make sure that JAVA_HOME environment variable points to jdk11 path).

1 - Download project sources

Next, in console in project root:<br/>
2 - run `gradle wrapper`<br/>
3 - run `./gradlew build`<br/>
4 - run `docker-compose build`<br/>
5 - run `docker-compose up -d`

Swagger should be available after several seconds at http://localhost:8080/swagger-ui.html

**If you want to run backend part without container - change datasource url in application.yml from db to localhost**