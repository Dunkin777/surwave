#### How to run Surwave-be in Docker:

**Prerequisites:** Docker and JDK 11 installed (make sure that JAVA_HOME environment variable points to JDK 11 path).

1 - Download project sources

2 - Create `application-local.yml` file and copy `application-local.yml.example` content to it.
If you're planning to use OAuth2 authentication locally - change surwave.authType to `oauth2`. Otherwise, comment out or remove `security` section completely.

Next, in console from project root directory:<br/>
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

For default basic security credentials will be "guest/guest" for user role and "admin/guest" for admin role.

**If you want to run backend part with Google authorization set authType property to `oauth2` in `application-local.yml` or run last step without CORE_PROFILES**

**Before adding any code, please, import surwaveCodeStyle.xml (can be found in the project root) as a project codestyle.**
