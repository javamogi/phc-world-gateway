FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*
COPY build/libs/phc-world-gateway-1.0.jar GatewayService.jar
ENTRYPOINT ["java","-jar","GatewayService.jar"]