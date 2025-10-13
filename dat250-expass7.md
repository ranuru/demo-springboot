# Report DAT250 Experiment 7

I set up the dockerfile in a new branch from my commit in the expass2 assignment, running through the steps given in the assignment description. I made sure that the gradle version was compatible with java 21, and that the user did not have root privileges. I had one issue with the .jar file not being found, which I solved by just adding *.jar to the dockerfile. I then tested the dockerfile by building and running the image, and it worked as expected. I also added the image build step to the github actions workflow, which also worked as expected.

The dockerfile is as follows (it can be found in the v2 branch of my repository):

```Dockerfile 
#build
FROM gradle:8.4-jdk21 AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --no-daemon

#run
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd -r appuser && useradd -r -g appuser appuser

COPY --from=builder app/build/libs/*.jar app.jar

RUN chown -R appuser:appuser /app
USER appuser

ENTRYPOINT ["java","-jar","app.jar"]
```
