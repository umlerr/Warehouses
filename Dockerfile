FROM openjdk:17-jdk-slim

# Устанавливаем необходимые библиотеки
RUN apt-get update && \
    apt-get install -y \
    libgtk-3-0 \
    libgl1-mesa-glx

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/your-javafx-app.jar /app/your-javafx-app.jar