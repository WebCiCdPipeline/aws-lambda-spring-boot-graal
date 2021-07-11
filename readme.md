This is an example of a Spring WEB MVC or Spring Boot Graal AWS Lambda app.
The purpose of this is to show how to integrate graal, spring boot (web) and aws lambda
with @RestControllers using a docker container. The lamba runtime api implementation doesn't need to be in Java or use an event handler. It just needs to make the HTTP requests to lambda apis and spring boot.

!!!Important!!!
This is not ready for production. The artifact should be copied out of the container that generates it to a separate, much smaller docker container. There is no error handling. The lambda error apis are not called. This is a proof of concept only.




docker build -t hello-world "C:/docker"

aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin ReplaceWithAWSAccountId.dkr.ecr.us-east-1.amazonaws.com

docker tag hello-world:latest ReplaceWithAWSAccountId.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest

docker push ReplaceWithAWSAccountId.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest

docker -rm run -it bin/sh --name somethingRandom hello-world