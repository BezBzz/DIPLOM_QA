# План автоматизации тестирования комплексного сервиса покупки туров

## Валидными данными при заполнении полей следует считать:

    1. Номер карты: 16 цифр
    2. Месяц: число от 01 до 12
    3. Год: две последние цифры текущего или последующих годов, но не более 5 лет от текущего года
    4. Владелец: Фамилия и Имя на латинице через пробел
    5. СVC/CVV: три цифры

## Номера карт, используемых для тестирования:

- Номер валидной карты: "4444 4444 4444 4441" статус карты "APPROVED" (одобрена)
- Номер не валидной карты: "4444 4444 4444 4442" статус карты "DECLINED" (отклонена)

## Перечень автоматизируемых сценариев:

### Примечание:
- ссылки перехода на станицу покупки тура **доступны только при запущенной SUT**.

# Frontend
---
## Предусловия тестов:

*Шаги:*

    1. В браузере перейти по адресу localhost:8080
    2. Нажать кнопку "Купить"

### Примечание:
- Поля "Владелец", "СVC/CVV" генерируются рандомно Faker-ом в соответствии с банковскими правилами заполнения реквизитов карт (см.информацию в начале страницы).
- Тесты производятся на карте со статусом *Approved* если не указана иная карта.
- Тесты производятся в режиме "Оплата по карте", далее их повторяем в режиме "Оплата в кредит".

#### поле "Номер карты":

**1. Заявка заполнена валидными данными карты со статусом *Approved* успешно одобрена банком**

    Во все поля вводим валидные значения.

*Ожидаемый результат:* выведено сообщение "Успешно. Операция одобрена банком."

**2. Заявка заполнена данными карты со статусом *Declined* отклонена банком**

    Ввести номер карты со статусом Declined, во все поля вводим валидные значения.

*Ожидаемый результат:* всплывающее сообщение "Ошибка! Банк отказал в проведении операции!"

**3. Заявка заполнена данными карты, отсутствующими в БД банка, отклонена банком**

    Ввести номер не существующей в БД карты, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы возможна, всплывающее сообщение "Ошибка! Банк отказал в проведении операции"

**4. Значение поля "Номер карты" пустое, все остальные поля формы валидные**

    Поле оставить пустым, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверный формат" под полем "Номер карты"

**5. Значение поля "Номер карты" содержит одну цифру, все остальные поля формы валидные**

    Ввести одну цифру, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверный формат" под полем "Номер карты"

**6. Значение поля "Номер карты" содержит нули, все остальные поля формы валидные**

    Ввести нули, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверный формат" под полем "Номер карты"

**7. Значение поля "Номер карты" из 15 цифр, все остальные поля формы валидные**

    Ввести 15 цифр, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверный формат" под полем "Номер карты"

#### поле "Месяц":

**8. Значение поля "Месяц" пустое, все остальные поля формы валидные**

    Поле оставить пустым, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Поле обязательно для заполнения" под полем "Месяц"

**9. Значение поля "Месяц" больше "12"**

    Заполняется значением больше 12, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверно указан срок действия карты" под полем "Месяц"

**10. Значение поля "Месяц" "00"**

    Заполняется значением "00", во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверно указан срок действия карты" под полем "Месяц"

**11. Значение поля "Месяц" - месяц, предыдущий от текущего, остальные поля - валидные данные**

    Заполняется значением месяца, предыдущего от текущего, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверно указан срок действия карты" под полем "Месяц"

#### поле "Год":

**12. Значение поля "Год" пусто**

    Поле оставляется пустым, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение «Поле обязательно для заполнения» под полем "Год"

**13. Значение поля "Год" "00"**

    Заполняется значением "00", во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Истек срок действия карты" под полем "Год"

**14. Значение поля "Год" прошлого периода**

    Заполняется значением прошлого года, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Истек срок действия карты" под полем "Год"

**15. Значение поля "Год" текущий +5 (карта, как правило, выдается на 5 лет)**

    Заполняется значением "2027", во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверно указан срок действия карты" под полем "Год"

#### поле "CVC/CVV":

**16. Значение поля "CVC/CVV" пусто**

    Поле оставляется пустым, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Поле обязательно для заполнения" под полем "CVC/CVV"

**17. Значение поля "CVC/CVV" 1 цифра**

    Заполняется одной цифрой, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверный формат" под полем "CVC/CVV"

**18. Значение поля "CVC/CVV" 2 цифры**

    Заполняется двумя цифрами, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверный формат" под полем "CVC/CVV"

**19. Значение поля "CVC/CVV" 000**

    Заполняется нулями, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* форма отправилась успешно, всплывающее сообщение "Успешно. Операция одобрена банком!"

**20. Значение поля "Владелец" оформлено в верхнем регистре**

    Поле оформлено в верхнем регистре на латинице, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* форма отправилась успешно, всплывающее сообщение "Успешно. Операция одобрена банком!"

#### поле "Владелец":

**21. Значение поля "Владелец" через дефис**

    Поле заполнено через дефис на латинице, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* форма отправилась успешно, всплывающее сообщение "Успешно. Операция одобрена банком!"

**22. Значение поля "Владелец" пустое**

    Поле оставляется пустым, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Поле обязательно для заполнения" под полем "Владелец"

**23. Значение поля "Владелец" фамилия на латинице, имя отсутсвует**

    Заполнено на латинице одним словом, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверный формат" под полем "Владелец"

**24. Значение поля "Владелец" фамилия и имя на кирилице**

    Заполнено на кирилице одним словом, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверный формат" под полем "Владелец"

**25. Значение поля "Владелец" заполнено цифрами**

    Заполнено цифрами, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверный формат" под полем "Владелец"

**26. Значение поля "Владелец" заполнено спец.символами**

    Заполнено спец.символами, во все остальные поля вводим валидные значения.

*Ожидаемый результат:* отправка формы невозможна, предупреждение "Неверный формат" под полем "Владелец"

---
# Backend:

**1. Отправка POST запроса платежа с валидно заполненным body и данными действующей карты на http://localhost:8080/payment**

*Ожидаемый результат:* статус 200, появление соответствующей записей в БД

**2. Отправка POST запроса на кредит с валидно заполненным body и данными действующей карты на http://localhost:8080/credit**

*Ожидаемый результат:* статус 200, появление соответствующей записей в БД

**3. Отправка POST запроса платежа с валидно заполненным body и данными declined карты на http://localhost:8080/payment**

*Ожидаемый результат:* статус 200, появление соответствующей записей в БД

**4. Отправка POST запроса на кредит с валидно заполненным body и данными declined карты на http://localhost:8080/credit**

*Ожидаемый результат:* статус 200, появление соответствующей записей в БД

**5. Отправка POST запроса с пустым body на http://localhost:8080/payment**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**6. Отправка POST запроса с пустым body на http://localhost:8080/credit**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**7. Отправка POST запроса платежа с пустым значением у атрибута number в body (остальные данные заполнены валидно) на http://localhost:8080/payment**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**8. Отправка POST запроса на кредит с пустым значением у атрибута number в body (остальные данные заполнены валидно) на http://localhost:8080/credit**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**9. Отправка POST запроса платежа с пустым значением у атрибута month в body (остальные данные заполнены валидно) на http://localhost:8080/payment**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**10. Отправка POST запроса на кредит с пустым значением у атрибута month в body (остальные данные заполнены валидно) на http://localhost:8080/credit**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**11. Отправка POST запроса платежа с пустым значением у атрибута year в body (остальные данные заполнены валидно) на http://localhost:8080/payment**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**12. Отправка POST запроса на кредит с пустым значением у атрибута year в body (остальные данные заполнены валидно) на http://localhost:8080/credit**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**13. Отправка POST запроса платежа с пустым значением у атрибута holder в body (остальные данные заполнены валидно) на http://localhost:8080/payment**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**14. Отправка POST запроса на кредит с пустым значением у атрибута holder в body (остальные данные заполнены валидно) на http://localhost:8080/credit**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**15. Отправка POST запроса платежа с пустым значением у атрибута cvc в body (остальные данные заполнены валидно) на http://localhost:8080/payment**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

**16. Отправка POST запроса на кредит с пустым значением у атрибута cvc в body (остальные данные заполнены валидно) на http://localhost:8080/credit**

*Ожидаемый результат:* статус 400, в БД не появляются новые записи

## Перечень используемых инструментов с обоснованием выбора:
|                   **Инструмент**                   | **Обоснование выбора** |
|:--------------------------------------------------:| :---: |
|                       JDK 17                       |т.к. будет использоваться Java - надежность и совместимость |
|                   IntelliJ IDEA                    | Удобная среда подготовки авто-тестов|
|                       Maven                        | Инструмент управления зависимостями|
|                      DBeaver                       | Для просмотра базы данных|
|                      JUnit 5                       |  Широта возможностей и стабильность|
|                      Selenide                      | Фреймворк для процесса автоматизированного тестирования ПО. Позволяет комфортно создавать програмный код при тестировании веб-интерфейса|
|                       Faker                        | Генерация данных искусственного наполнителя для приложения и его потребностей в тестировании|
| MySQL connector Java, PostgreSQL и Commons DBUtils | Доступ к базе данных из кода авто-тестов|
|                       Github                       |  Хранилище SUT и авто-тестов;
|                       Allure                       | Наглядная визуализация создания отчетов о выполнении авто-тестов;
|                  Allure-Selenide                   |Интеграция одного инструмента с другим|

## Перечень и описание возможных рисков при автоматизации:

- Своеобразная настройка SUT при запуске
- Заявлена поддержка двух СУБД
- Неверный выбор инструментов для тестирования
- При использовании какого-то инструмента с малоизвестным функционалом можно попасть в зависимость от технологий и потерять значительное количество времени.
- Необходимость согласований.
- Если речь идёт о многих аспектах, особенно при протяжённой цепочке согласований. Отсутствие как таковой документации на приложение. Возникнут затруднения в процессе работы над проектом, что будет проявляться всё сильнее в долгосрочной перспективе.
- Особые запросы для автоматизации.
- В случае строгих требований к безопасности: например, выполнения тестов в CI с использованием VPN.
- Зависимость авто-тестов от текущей реализации веб-элементов, даже не значительное их изменение может привести к падению авто-тестов
- Отсутствие проверки графической составляющей авто-тестами
- Состояние верстки при тех или иных действиях посетителя, комфортна ли выбранная цветовая схема оформления и тд.

## Интервальная оценка с учётом рисков (в часах):
- С учётом вышеназванных рисков на автоматизацию требуется ориентировочно, 90 рабочих часов

## План сдачи работ:

- [ ] Подготовка к проведению тестирования (запуск SUT, подготовка плана автоматизации) - 24 часов
- [ ] Написание и прогон авто-тестов - 40 часов;
- [ ] Составление ```Issue``` - 8 часов;
- [ ] Оформление отчёта по итогам тестирования - 8 часов;
- [ ] Оформление отчёта по итогам автоматизации - 10 часов.