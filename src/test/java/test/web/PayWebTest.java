package test.web;

import card.Card;
import card.CardFactory;
import com.codeborne.selenide.logevents.SelenideLogger;
import db.DbUtil;
import db.dto.CreditDto;
import db.dto.OrderDto;
import db.dto.PayDto;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import webpage.TripPage;
import webpage.TripPagePayForm;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayWebTest {

    private static TripPage page;
    private static final String url = "http://localhost:8080/";

    @BeforeAll
    public static void setupClass() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    @BeforeEach
    public void init() {
        open(url);
        page = new TripPage();
        DbUtil.prepareDB();
    }

    @AfterAll
    public static void setDownClass() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Заявка по карте со статусом APPROVED")
    public void testPayWithApprovedCard() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getApprovedCard();
        payForm.payByCard(card);

        payForm.checkSuccessNotification();

        List<OrderDto> orders = DbUtil.getOrderQueryResult();
        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();

        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals(1, orders.size());

        assertEquals(45_000, payments.get(0).getAmount());
        assertEquals("approved", payments.get(0).getStatus().toLowerCase());
        assertEquals(payments.get(0).getTransaction_id(), orders.get(0).getPayment_id());

    }

    @Test
    @DisplayName("Заявка по карте со статусом DECLINED")
    public void testPayWithDeclinedCard() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getApprovedCard();
        payForm.payByCard(card);

        payForm.checkErrorNotification();

        List<OrderDto> orders = DbUtil.getOrderQueryResult();
        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();

        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals(1, orders.size());

        PayDto payment = payments.get(0);
        OrderDto order = orders.get(0);

        assertEquals(45_000, payment.getAmount());
        assertEquals("declined", payment.getStatus().toLowerCase());
        assertEquals(payment.getTransaction_id(), order.getPayment_id());
    }

    @Test
    @DisplayName("Заявка заполнена данными карты, отсутствующими в БД банка")
    public void testPayWithUnknownCard() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getUnknownCard();
        payForm.payByCard(card);

        payForm.checkErrorNotification();

        List<OrderDto> orders = DbUtil.getOrderQueryResult();
        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();

        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals(1, orders.size());

        PayDto payment = payments.get(0);
        OrderDto order = orders.get(0);

        assertEquals(45_000, payment.getAmount());
        assertEquals("declined", payment.getStatus().toLowerCase());
        assertEquals(payment.getTransaction_id(), order.getPayment_id());
    }

    @Test
    @DisplayName("Значение поля \"Номер карты\" пустое, все остальные поля формы валидные")
    public void testPayWithEmptyCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getEmptyNumberCard();
        payForm.payByCard(card);

        payForm.wrongFormatCardNotification();
    }

    @Test
    @DisplayName("Значение поля \"Номер карты\" содержит одну цифру, все остальные поля формы валидные")
    public void testPayWithOneNumberCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getOneNumberCard();
        payForm.payByCard(card);

        payForm.wrongFormatCardNotification();
    }

    @Test
    @DisplayName("Значение поля \"Номер карты\" содержит нули, все остальные поля формы валидные")
    public void testPayWithZerosNumberCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getZerosNumberCard();
        payForm.payByCard(card);

        payForm.wrongFormatCardNotification();
    }

    @Test
    @DisplayName("Значение поля \"Номер карты\" из 15 цифр, все остальные поля формы валидные")
    public void testPayWithFifteenNumberCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getFifteenNumberCard();
        payForm.payByCard(card);

        payForm.wrongFormatCardNotification();
    }

    //    8. Значение поля "Месяц" пустое, все остальные поля формы валидные
//
//    Поле оставить пустым, во все остальные поля вводим валидные значения.
//    Ожидаемый результат: отправка формы невозможна, предупреждение "Поле обязательно для заполнения" под полем "Месяц"
    @Test
    @DisplayName("Значение поля \"Месяц\" пустое, все остальные поля формы валидные")
    public void testPayWithEmptyMonthCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getEmptyMonthCard();
        payForm.payByCard(card);

        payForm.monthNeedToBeFilledNotification();
    }

    @Test
    @DisplayName("Значение поля \"Месяц\" больше \"12\"")
    public void testPayWithMonthValueBiggerTwelveCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getMonthValueBiggerTwelveCard();
        payForm.payByCard(card);

        payForm.monthWrongDateNotification();
    }

    @Test
    @DisplayName("Значение поля \"Месяц\" \"00\"")
    public void testPayWithMonthZeroZeroValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getMonthZeroZeroValueCard();
        payForm.payByCard(card);

        payForm.monthWrongDateNotification();
    }

    @Test
    @DisplayName("Значение поля \"Месяц\" - месяц, предыдущий от текущего, остальные поля - валидные данные")
    public void testPayWithPreviousMonthValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getPreviousMonthValueCard();
        payForm.payByCard(card);

        payForm.monthWrongDateNotification();
    }

    @Test
    @DisplayName("Значение поля \"Год\" пусто")
    public void testPayWithEmptyYearValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getEmptyYearValueCard();
        payForm.payByCard(card);

        payForm.yearNeedToBeFilledNotification();
    }

    @Test
    @DisplayName("Значение поля \"Год\" \"00\"")
    public void testPayWithYearZeroZeroValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getYearZeroZeroValueCard();
        payForm.payByCard(card);

        payForm.yearExpiredNotification();
    }

    @Test
    @DisplayName("Значение поля \"Год\" прошлого периода")
    public void testPayWithPreviousYearValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getPreviousYearValueCard();
        payForm.payByCard(card);

        payForm.yearExpiredNotification();
    }

    @Test
    @DisplayName("Значение поля \"Год\" текущий +5")
    public void testPayWithYearPlusFiveValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getYearPlusFiveValueCard();
        payForm.payByCard(card);

        payForm.yearWrongDateNotification();
    }

    @Test
    @DisplayName("Значение поля \"CVC/CVV\" пусто")
    public void testPayWithEmptyCVVValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getEmptyCVVValueCard();
        payForm.payByCard(card);

        payForm.cvvNeedToBeFilledNotification();
    }

    @Test
    @DisplayName("Значение поля \"CVC/CVV\" 1 цифра")
    public void testPayWithOneNumberCVVValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getOneNumberCVVValueCard();
        payForm.payByCard(card);

        payForm.cvvWrongFormatNotification();
    }

    @Test
    @DisplayName("Значение поля \"CVC/CVV\" 2 цифры")
    public void testPayWithTwoNumberCVVValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getTwoNumberCVVValueCard();
        payForm.payByCard(card);

        payForm.cvvWrongFormatNotification();
    }

    @Test
    @DisplayName("Значение поля \"CVC/CVV\" 000")
    public void testPayWithTripleZeroCVVValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getTripleZeroCVVValueCard();
        payForm.payByCard(card);

        payForm.checkSuccessNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" оформлено в верхнем регистре")
    public void testPayWithCapsOwnerValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getCapsOwnerValueCard();
        payForm.payByCard(card);

        payForm.checkSuccessNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" через дефис")
    public void testPayWithOwnerWithDashSymbolValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getOwnerWithDashSymbolValueCard();
        payForm.payByCard(card);

        payForm.checkSuccessNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" пустое")
    public void testPayWithEmptyOwnerValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getEmptyOwnerValueCard();
        payForm.payByCard(card);

        payForm.ownerNeedToBeFilledNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" фамилия на латинице, имя отсутсвует")
    public void testPayWithEmptyNameOwnerValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getEmptyNameOwnerValueCard();
        payForm.payByCard(card);

        payForm.ownerWrongFormatNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" фамилия и имя на кирилице")
    public void testPayWithCyrillicSymbolsOwnerValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getCyrillicSymbolsOwnerValueCard();
        payForm.payByCard(card);

        payForm.ownerWrongFormatNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" заполнено цифрами")
    public void testPayWithNumberSymbolsOwnerValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getNumberSymbolsOwnerValueCard();
        payForm.payByCard(card);

        payForm.ownerWrongFormatNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" заполнено цифрами")
    public void testPayWithSymbolsOwnerValueCardField() {
        TripPagePayForm payForm = page.getPayWebpage();
        Card card = CardFactory.getSymbolsOwnerValueCard();
        payForm.payByCard(card);

        payForm.ownerWrongFormatNotification();
    }

}
