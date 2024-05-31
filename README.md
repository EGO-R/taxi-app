taxi-app
============
Этот репозиторий содержит исходный код для веб-приложения таксопарка. С помощью этого приложения вы можете создавать заявки на поездки как пассажир или принимать их как водитель.
Приложение имеет функционал регистрации, аутентификации и авторизации. Поддерживается 3 типа пользователей: обычный пользователь, водитель и админ. Любой пользователь может создать заявку на поездку, 
а принять её может любой водитель. Также каждый пользователь может посмотреть историю своих поездок и информацию о них. К поездкам, в которых пользователь не участвовал, доступ получить нельзя. 
Админ может просматривать и изменять информацию о любых пользователях, поездках и машинах.

Веб-приложение создавалось с помощью фреймворка Spring, а конкретнее Spring Boot, Spring Web, Spring Data JPA и Spring Security. В качестве СУБД использовалась PostgreSQL. Сборщиком проекта был выбран Gragle.
Для динамического создания HTML использовался Thymeleaf, а для упрощения разработки была применена библиотека Lombok. Приложение может быть контейнеризировано с помощью Docker и docker-compose, необходимые файлы уже созданы.

Установка
============
Склонируйте репозиторий на свой локальный компьютер:
```
git clone https://github.com/EGO-R/taxi-app
```

Использование
============
Запустите приложение с помощью docker-compose:
```
docker-compose up --build
```
По умолчанию, сервер будет запущен на порту 8080.

Приложение предоставляет следующие эндпоинты:
--------------
GET /: Начальная страница. Содержит кнопку для авторизации

GET /auth: Страница аутентификации. Содержит формы для входа или регистрации

POST /auth/sign-in: Аутентифицирует пользователя. Ожидает передачу в теле запроса параметра username с именем пользователя и password с паролем. Возвращает JWT-токен в cookie и перенаправляет пользователя на домашнюю страницу.

POST /auth/sign-up: Регистрирует пользователя. Ожидает передачу в теле запроса параметра username с именем пользователя, email с почтой и password с паролем. Возвращает JWT-токен в cookie и перенаправляет пользователя на домашнюю страницу.

GET /logout: Выходит из аккаунта пользователя. Удаляет cookie с токеном и перенаправляет на страницу аутентификации.

GET /home: Домашняя страница. Приветствует пользователя и имеет меню сверху для удобной навигации по сайту.

GET /history: Страница истории поездок пользователя.

GET /drive/{id}: Страница информации о поездке. Имеет кнопки для принятия, отмены и завершения поездки.

GET /drive/cancel/{id}: Страница отмены поездки. После отмены возвращает пользователя на страницу информации о поездке.

GET /drive/accept/{id}: Страница принятия поездки. Присваивает водителя поездке и меняет её состояние. После принятия возвращает пользователя на страницу информации о поездке.

GET /drive/finish/{id}: Страница завершения поездки. После завершения возвращает пользователя на страницу информации о поездке.

GET /request: Страница создания заявки на поездку. Содержит форму для создания поездки.

POST /request: Создаёт поездку. Ожидает передачу в теле запроса параметра department с адресом отправления и target с адресом места прибытия. После создания поездки перенаправляет пользователя на страницу поездки.

GET /awaiting: Страница ожидающих поездок. Доступна только водителям и админам.

GET /admin: Домашняя страница админа. Содержит приветствие и меню для навигации по разделу для админов. Страницы, содержащие такой префикс доступны только админам.

GET /admin/users: Страница списка всех пользователей.

GET /admin/users/{id}: Страница информации о пользователе. Содержит форму, в которой можно менять информацию о пользователе.

POST /admin/users/{id}: Изменяет информацию о пользователе. Ожидает передачу в теле запроса изменённую информацию о пользователе. Перенаправляет пользователя на страницу информации о пользователе.

GET /admin/drives: Страница списка всех поездок.

GET /admin/drives/{id}: Страница информации о поездке. Содержит форму, в которой можно менять информацию о поездке.

POST /admin/drives/{id}: Изменяет информацию о поездке. Ожидает передачу в теле запроса изменённую информацию о поездке. Перенаправляет пользователя на страницу информации о поездке.

GET /admin/cars: Страница списка всех машин таксопарка.

GET /admin/cars/{id}: Страница информации о машине. Содержит форму, в которой можно менять информацию о машине.

POST /admin/cars/{id}: Изменяет информацию о машине. Ожидает передачу в теле запроса изменённую информацию о машине. Перенаправляет пользователя на страницу информации о машине.

GET /admin/cars/add: Страница создания новой машины. Содержит форму, в которой можно указывать информацию о машине.

POST /admin/cars/{id}: Создаёт новую машину. Ожидает передачу в теле запроса информацию о машине. Перенаправляет пользователя на страницу создания новой машины.

GET /get/admin: Делает пользователя админом. Перенаправляет пользователя на домашнюю страницу админа. Используется только для тестирования.

GET /get/driver: Делает пользователя водителем. Содержит форму, в которой нужно выбрать свободную машину. Используется только для тестирования.

POST /get/driver: Делает пользователя водителем. Ожидает передачу в теле запроса параметра carId с id свободной машины. Возвращает страницу приветствия водителя. Используется только для тестирования.

GET /get/user: Делает пользователя обычным пользователем. Содержит приветствие пользователя. Используется только для тестирования.

Конфигурация:
============
Можно настроить некоторые параметры проекта, отредактировав файл src/main/resources/application.yml.