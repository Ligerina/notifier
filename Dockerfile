# 1. Базовый образ с Java 21
FROM eclipse-temurin:21-jdk as runtime

# 2. Укажем рабочую директорию внутри контейнера
WORKDIR /app

# 3. Копируем .jar файл (любой jar из build/libs/)
COPY build/libs/*.jar app.jar

# 4. Указываем точку входа
ENTRYPOINT ["java", "-jar", "app.jar"]
