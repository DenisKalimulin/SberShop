# Маркетплейс SberShop

**SberShop** — это приложение для управления товарами, заказами, оформлением заказов, оплатой, а также взаимодействием с внешними системами (например, доставка, платежные сервисы, аналитика).
Зарегистрированные пользователи могут как покупать товары, так и продавать. 

## Архитектура проекта

- **Монолитное приложение** с многомодульной структурой.
- **Синхронные запросы и ответы** реализованы через **REST** с использованием **Spring Web**.
- **Асинхронное взаимодействие с внешними сервисами** с использованием **Apache Kafka** (например, отправка сообщений о сформированных заказах, уведомления и т.д.).
- **База данных**: PostgreSQL с использованием **Spring Data JPA**.
- Используется **Spring Boot 3** для создания и запуска приложения.

## Основной стек технологий

- **Spring Boot 3**
- **Spring Data JPA** — для работы с базой данных
- **Apache Kafka** — для обработки асинхронных событий
- **JUnit5 + Mockito** — для unit-тестирования
- **OpenAPI/Swagger** — для документирования REST API
- **SLF4J/Logback** — для логирования

## Модули приложения

1. **repository**  
   Модуль, содержащий сущности и репозитории, а также все классы, относящиеся к работе с базой данных.

2. **dto**  
   Модуль, содержащий все классы DTO (Data Transfer Objects), используемые для передачи данных между слоями приложения.

3. **service**  
   Модуль с бизнес-логикой, сервисами для работы с товарами, заказами, пользователями. Содержит вызовы репозиториев и взаимодействие с Apache Kafka.

4. **web**  
   Модуль с REST контроллерами и главным классом приложения для обработки HTTP-запросов.

5. **exception**  
   Модуль с глобальным обработчиком исключений и кастомными исключениями для централизованной обработки ошибок в приложении.

6. **config**  
   Модуль с конфигурацией приложения, включая настройки безопасности, Kafka, и другие необходимые компоненты.

## Основные функции

- **Регистрация пользователей**: покупатели и продавцы могут зарегистрироваться в системе.
- **Управление товарами**: пользователи могут добавлять, обновлять и удалять товары.
- **Оформление заказа**: покупатели могут создавать и оплачивать заказы.
- **Интеграция с внешними сервисами**: поддержка взаимодействия с внешними сервисами, такими как платежные системы и службы доставки.
- **Роли пользователей**: поддержка различных ролей (например, продавец, покупатель, администратор) с различными правами доступа.

## Запуск проекта

Для запуска проекта на локальной машине, выполните следующие шаги:
//TODO

   

