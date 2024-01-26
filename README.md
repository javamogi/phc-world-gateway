# phc-world-gateway
* 마이크로 서비스 아키텍처에서 API 게이트웨이 역할을 수행하는 Spring Cloud Gateway Server입니다.  
* API 게이트웨이는 클라이언트와 백엔드 서비스 간의 통신을 관리하며, 다양한 기능을 제공하여 마이크로서비스 애플리케이션의 전체적인 효율성과 보안을 향상시킵니다.
*** 
* JWT 토큰 검증
  * Custom Filter 구현 후 백엔드 서비스 요청 시 구현한 Filter를 설정하여 백엔드 서비스 실행 전에 토큰을 검증합니다.
***
### Stack
> * JAVA 17
> * Spring-Boot 3.2.1
> * Gradle 8.5
> * Spring Cloud Gateway
> * Spring Cloud Eureka Client
> * Spring Cloud Bus
> * RabbitMQ
> * JWT
*** 
### MSA
![PHC-WORL_MSA_Architecture_Config](https://github.com/javamogi/phc-world-config/assets/40781237/e0e153c8-7534-4c8d-b1a5-1cd92b0c2de0)
*** 
#### [PHC-WORLD Eureka Server](https://github.com/javamogi/phcworld-discovery)
#### [PHC-WORLD Config](https://github.com/javamogi/phc-world-config)
#### [PHC-WORLD Config File Repository(private)](https://github.com/javamogi/phc-world-git-repo)
#### [PHC-WORLD User-Service](https://github.com/javamogi/phc-world-user-service)
#### [PHC-WORLD Board-Service](https://github.com/javamogi/phc-world-board-service)
#### [PHC-WORLD Answer-Service](https://github.com/javamogi/phc-world-board-answer-service)

