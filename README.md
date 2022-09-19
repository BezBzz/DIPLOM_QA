# Дипломный проект профессии «Тестировщик»

Дипломный проект представляет собой автоматизацию тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

## Документы
* [Как правильно работать над дипломом?](https://github.com/BezBzz/DIPLOM_QA/blob/master/docs/work.md)
* [Описание приложения](https://github.com/BezBzz/DIPLOM_QA/blob/master/docs/description.md)
* [Перечень автоматизируемых сценариев](https://github.com/BezBzz/DIPLOM_QA/blob/master/docs/Plan.md)
* [Отчет по итогам тестирования](https://github.com/BezBzz/DIPLOM_QA/blob/master/docs/report.md)
* [Отчет по итогам автоматизированного тестирования](https://github.com/BezBzz/DIPLOM_QA/blob/master/docs/report2.md)

## На локальном компьютере заранее должны быть установлены:

1. IntelliJ IDEA 
2. JDK11
3. Docker Desktop
4. Git Bash
5. Браузер

## Подготовка среды перед тестированием:

**1.** Склонировать в локальный репозиторий [Дипломный проект](https://github.com/BezBzz/DIPLOM_QA.git) и открыть его в IDE IntelliJ IDEA

**2.** Запустить Docker Desktop

**3.** В терминале запустить контейнеры с помощью команды:

    docker-compose up -d

**4.** Запустить целевой веб-сервис командой:

     для mySQL: 
    java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar

     для postgresgl:
     java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar

# Процедура запуска авто-тестов:

**1.** Во втором терминале запустить тесты:

    для mySQL:
    ./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"

    для postgresgl: 
    ./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"

**2.** Создать отчёт Allure и открыть в браузере:

    ./gradlew allureServe

# Действия после завершения авто-тестов:

**1.** Для завершения работы allureServe выполнить команду:

    Ctrl+C

**2.** Для остановки работы контейнеров выполнить команду:

    docker-compose down