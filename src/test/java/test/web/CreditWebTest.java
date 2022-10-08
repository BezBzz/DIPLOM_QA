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
import webpage.TripPageCreditForm;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditWebTest {
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
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getApprovedCard();
        creditForm.payByCard(card);

        creditForm.checkSuccessNotification();

        List<OrderDto> orders = DbUtil.getOrderQueryResult();
        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();

//        assertEquals(0, payments.size());
//        assertEquals(1, credits.size());
//        assertEquals(1, orders.size());
//
//        CreditDto credit = credits.get(0);
//        OrderDto order = orders.get(0);
//
//        assertEquals(order.getCredit_id(), credit.getId());
//        assertEquals("approved", credit.getStatus().toLowerCase());
//        assertEquals(credit.getBank_id(), order.getPayment_id());

        assertAll(
                () -> assertEquals(0, payments.size()),
                () -> assertEquals(1, credits.size()),
                () -> assertEquals(1, orders.size()),
                () -> assertEquals(orders.get(0).getCredit_id(), credits.get(0).getId()),
                () -> assertEquals("approved", credits.get(0).getStatus().toLowerCase()),
                () -> assertEquals(credits.get(0).getBank_id(), orders.get(0).getPayment_id())
        );
    }

    @Test
    @DisplayName("Заявка по карте со статусом DECLINED")
    public void testPayWithDeclinedCard() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getApprovedCard();
        creditForm.payByCard(card);

        creditForm.checkErrorNotification();

        List<OrderDto> orders = DbUtil.getOrderQueryResult();
        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();

        assertAll(
                () -> assertEquals(0, payments.size()),
                () -> assertEquals(1, credits.size()),
                () -> assertEquals(1, orders.size()),
                () -> assertEquals(orders.get(0).getCredit_id(), credits.get(0).getId()),
                () -> assertEquals("declined", credits.get(0).getStatus().toLowerCase()),
                () -> assertEquals(credits.get(0).getBank_id(), orders.get(0).getPayment_id())
        );
    }

    @Test
    @DisplayName("Заявка заполнена данными карты, отсутствующими в БД банка")
    public void testPayWithUnknownCard() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getUnknownCard();
        creditForm.payByCard(card);

        creditForm.checkErrorNotification();

        List<OrderDto> orders = DbUtil.getOrderQueryResult();
        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();

        assertAll(
                () -> assertEquals(0, payments.size()),
                () -> assertEquals(1, credits.size()),
                () -> assertEquals(1, orders.size()),
                () -> assertEquals("declined", credits.get(0).getStatus().toLowerCase()),
                () -> assertEquals(credits.get(0).getBank_id(), orders.get(0).getCredit_id())
        );
    }

    @Test
    @DisplayName("Значение поля \"Номер карты\" пустое, все остальные поля формы валидные")
    public void testPayWithEmptyCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getEmptyNumberCard();
        creditForm.payByCard(card);

        creditForm.wrongFormatCardNotification();
    }

    @Test
    @DisplayName("Значение поля \"Номер карты\" содержит одну цифру, все остальные поля формы валидные")
    public void testPayWithOneNumberCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getOneNumberCard();
        creditForm.payByCard(card);

        creditForm.wrongFormatCardNotification();
    }

    @Test
    @DisplayName("Значение поля \"Номер карты\" содержит нули, все остальные поля формы валидные")
    public void testPayWithZerosNumberCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getZerosNumberCard();
        creditForm.payByCard(card);

        creditForm.wrongFormatCardNotification();
    }

    @Test
    @DisplayName("Значение поля \"Номер карты\" из 15 цифр, все остальные поля формы валидные")
    public void testPayWithFifteenNumberCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getFifteenNumberCard();
        creditForm.payByCard(card);

        creditForm.wrongFormatCardNotification();
    }

    @Test
    @DisplayName("Значение поля \"Месяц\" пустое, все остальные поля формы валидные")
    public void testPayWithEmptyMonthCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getEmptyMonthCard();
        creditForm.payByCard(card);

        creditForm.monthNeedToBeFilledNotification();
    }

    @Test
    @DisplayName("Значение поля \"Месяц\" больше \"12\"")
    public void testPayWithMonthValueBiggerTwelveCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getMonthValueBiggerTwelveCard();
        creditForm.payByCard(card);

        creditForm.monthWrongDateNotification();
    }

    @Test
    @DisplayName("Значение поля \"Месяц\" \"00\"")
    public void testPayWithMonthZeroZeroValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getMonthZeroZeroValueCard();
        creditForm.payByCard(card);

        creditForm.monthWrongDateNotification();
    }

    @Test
    @DisplayName("Значение поля \"Месяц\" - месяц, предыдущий от текущего, остальные поля - валидные данные")
    public void testPayWithPreviousMonthValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getPreviousMonthValueCard();
        creditForm.payByCard(card);

        creditForm.monthWrongDateNotification();
    }

    @Test
    @DisplayName("Значение поля \"Год\" пусто")
    public void testPayWithEmptyYearValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getEmptyYearValueCard();
        creditForm.payByCard(card);

        creditForm.yearNeedToBeFilledNotification();
    }

    @Test
    @DisplayName("Значение поля \"Год\" \"00\"")
    public void testPayWithYearZeroZeroValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getYearZeroZeroValueCard();
        creditForm.payByCard(card);

        creditForm.yearExpiredNotification();
    }

    @Test
    @DisplayName("Значение поля \"Год\" прошлого периода")
    public void testPayWithPreviousYearValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getPreviousYearValueCard();
        creditForm.payByCard(card);

        creditForm.yearExpiredNotification();
    }

    @Test
    @DisplayName("Значение поля \"Год\" текущий +5")
    public void testPayWithYearPlusFiveValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getYearPlusFiveValueCard();
        creditForm.payByCard(card);

        creditForm.yearWrongDateNotification();
    }

    @Test
    @DisplayName("Значение поля \"CVC/CVV\" пусто")
    public void testPayWithEmptyCVVValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getEmptyCVVValueCard();
        creditForm.payByCard(card);

        creditForm.cvvNeedToBeFilledNotification();
    }

    @Test
    @DisplayName("Значение поля \"CVC/CVV\" 1 цифра")
    public void testPayWithOneNumberCVVValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getOneNumberCVVValueCard();
        creditForm.payByCard(card);

        creditForm.cvvWrongFormatNotification();
    }

    @Test
    @DisplayName("Значение поля \"CVC/CVV\" 2 цифры")
    public void testPayWithTwoNumberCVVValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getTwoNumberCVVValueCard();
        creditForm.payByCard(card);

        creditForm.cvvWrongFormatNotification();
    }

    @Test
    @DisplayName("Значение поля \"CVC/CVV\" 000")
    public void testPayWithTripleZeroCVVValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getTripleZeroCVVValueCard();
        creditForm.payByCard(card);

        creditForm.checkSuccessNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" оформлено в верхнем регистре")
    public void testPayWithCapsOwnerValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getCapsOwnerValueCard();
        creditForm.payByCard(card);

        creditForm.checkSuccessNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" через дефис")
    public void testPayWithOwnerWithDashSymbolValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getOwnerWithDashSymbolValueCard();
        creditForm.payByCard(card);

        creditForm.checkSuccessNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" пустое")
    public void testPayWithEmptyOwnerValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getEmptyOwnerValueCard();
        creditForm.payByCard(card);

        creditForm.ownerNeedToBeFilledNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" фамилия на латинице, имя отсутсвует")
    public void testPayWithEmptyNameOwnerValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getEmptyNameOwnerValueCard();
        creditForm.payByCard(card);

        creditForm.ownerWrongFormatNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" фамилия и имя на кирилице")
    public void testPayWithCyrillicSymbolsOwnerValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getCyrillicSymbolsOwnerValueCard();
        creditForm.payByCard(card);

        creditForm.ownerWrongFormatNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" заполнено цифрами")
    public void testPayWithNumberSymbolsOwnerValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getNumberSymbolsOwnerValueCard();
        creditForm.payByCard(card);

        creditForm.ownerWrongFormatNotification();
    }

    @Test
    @DisplayName("Значение поля \"Владелец\" заполнено цифрами")
    public void testPayWithSymbolsOwnerValueCardField() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getSymbolsOwnerValueCard();
        creditForm.payByCard(card);

        creditForm.ownerWrongFormatNotification();
    }
}