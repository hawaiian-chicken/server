# 1. Java 21 기반 이미지 사용 (Amazon Corretto)
FROM amazoncorretto:21

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사 (아래 gradle 빌드 후 생성되는 JAR을 기준으로)
COPY build/libs/*.jar app.jar

# 4. JAR 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]