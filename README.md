Coliving-helper
===
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Dompurrr_coliving-helper&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Dompurrr_coliving-helper)  

Микросервисное приложение, облегчающее совместное проживаение, а именно:  
- Отслеживание списка проживающих.
- Отслеживание общих покупок.
- Отслеживание дежурств.  

Архитектура
---
Приложение реализовано из ряда микросервисов, а именно:
- tg-dispatcher - микросервис для взаимодействия с api telegram, делающее первичную проверку и добавляющий сообщения в очередь.
- node - основной микросервис, выполняющий обработку поступивших сообщений и основную бизнес-логику.
Так же используются контейнеры с:
- PostgreSQL
- RabbitMQ

Используемые технологии:
---
- Spring boot
  + Spring AMQP
  + Spring Web
  + Spring Data JPA
- [PostgreSQL driver](https://github.com/pgjdbc/pgjdbc)
- [TelegramBots](https://github.com/rubenlagus/TelegramBots)
- Lombok
- Log4j
- Github actions
  + SonarSource
  + Labeler