# lostem-server
## 소개
로스템 프로젝트의 서버 부분입니다. API와 배포 부분을 다루고 있습니다.


## 기술 스택 및 라이브러리
- 기술 스택 : Java, Spring Boot, MySQL, Postman, Git, GitHub, AWS EC2, AWS RDS, AWS S3
- 라이브러리 : Spring Web, Spring Data JPA, Lombok, Spring Security, JWT  
  


## ERD 데이터 모델링
![image](https://github.com/LK-loty/lostem-back/assets/110155447/a919621e-987a-4be9-85c5-bc0ec55aceab)



## 프로젝트 구조



## API 명세서
API 명세서는 [이곳](./api_doc.pdf)에서 확인할 수 있습니다.


## 시작 및 실행
1. 프로젝트를 클론합니다.
- 요구사항  
  Java 17 이상, Spring Boot 3.1 이상, MySQL 8.0  
  AWS S3 생성 및 권한이 부여된 키
  
2. 루트 디렉터리 또는 src/main/resource 아래에 다음과 같은 application.yml 설정 파일을 작성합니다.
```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/yourdatabase
    username: yourusername
    password: yourpassword

  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
      show-sql: true

  redis:
    host: localhost
    port: 6379

  jwt:
    header: Authorization
    issuer: yourname
    secret: yoursecretkey
    token-validity : 86400000
    refresh-validity : 172800000

  servlet:
    multipart:
      enabled: true
      max-file-size: 50MB
      max-request-size: 50MB

  mail:
    host: smtp.gmail.com
    port: 587
    username: yourgmail
    password: yourgmailsecretpassword
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000
    auth-code-expiration-millis: 1800000

cloud:
  aws:
    credentials:
      accessKey: youraccesskey
      secretKey: yoursecretkey
    region:
      static: ap-northeast-2
    s3:
      bucketName: yourbucketname
    stack:
      auto: false
```

3. 프로젝트의 루트 디렉터리에서 다음 명령어를 실행합니다.
```
./gradlew wrapper
```
위의 명령어가 실행 권한이 필요한 경우 실행 권한을 부여합니다. 리눅스의 경우 다음과 같이 권한을 부여해줍니다.
```
chmod +x ./gradlew
```
필요한 파일들이 설치되면 빌드파일을 생성합니다.
```
./gradlew build
```
4. build/libs 디렉터리 아래에 JAR 파일이 생성됨을 확인합니다.
5. 해당 위치에서 다음 명령어를 실행합니다.
```
nohup java -jar lostem-0.0.1-SNAPSHOT.jar
```
7. 8080 포트에 프로세스가 실행됩니다.


## 느낀점
- 아직 코드를 깔끔하게 정리하는 데 익숙하지 않았다. RESTful 한지도 의문이다. 좀 더 배우고 많은 프로젝트들을 구현해보고 참고도 해보며 계속해서 코드를 다듬을 예정이다. 
- 서버 배포 부분이 찾아본 것에 비해 다양한 방식이 있어보였다. 또한 도커와 쿠버네티스 및 CI/CD 구현도 해보고 싶었는데, 아직 많이 이해하지는 못해서 중단되었지만 다시 도입할 것이다.
- 소켓의 채팅 부분도 이렇게 구성되지 않을까 싶어서 구현했지만, 실제 정확한 동작에 대해서는 확실하지는 않았다. 더 나은 방식이 있는지 고민하고, 괜찮으면 키워드의 새 글 알림에도 실시간 추가하면 좋을 것 같다.
