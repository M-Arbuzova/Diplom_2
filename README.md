Diplom_2
1. Описание проекта
   Это вторая часть дипломного проекта,  выполненного в рамках образовательной программы Яндекс Практикума. В рамках задачи требуется протестировать "ручки" API на сайт, который представляет собой космический фастфуд: можно собрать и заказать бургер из необычных ингредиентов.

2. Сайт:
   https://stellarburgers.nomoreparties.site/

3. Требуется проверить:
   Создание пользователя:
   создать уникального пользователя;
   создать пользователя, который уже зарегистрирован;
   создать пользователя и не заполнить одно из обязательных полей.
   Логин пользователя:
   логин под существующим пользователем,
   логин с неверным логином и паролем.
   Изменение данных пользователя:
   с авторизацией,
   без авторизации,
   Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
   Создание заказа:
   с авторизацией,
   без авторизации,
   с ингредиентами,
   без ингредиентов,
   с неверным хешем ингредиентов.
   Получение заказов конкретного пользователя:
   авторизованный пользователь,
   неавторизованный пользователь.

4. Используемые инструменты:
- IntelliJ IDEA Community Edition 2022.3.1
- Java 11
- Maven 4.0.0
- Junit 4.13.2
- Allure
- Postman
- Rest Assured