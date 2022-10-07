package test.api;

import card.Card;
import card.CardFactory;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.gson.Gson;
import db.DbUtil;
import db.dto.CreditDto;
import db.dto.OrderDto;
import db.dto.PayDto;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ApiTest {

    private static final Gson gson = new Gson();
    private static final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL).build();
    private final String payLink = "/api/v1/pay";
    private final String creditLink = "/api/v1/credit";

    @BeforeAll
    public static void setupClass() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    @BeforeEach
    public void prepare() {
        DbUtil.prepareDB();
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured());
    }

    @AfterAll
    public static void setDownClass() {
        SelenideLogger.removeListener("allure");
    }


    //    1. Отправка POST запроса платежа с валидно заполненным body и данными действующей карты на http://localhost:8080/payment
//
//    Ожидаемый результат: статус 200, появление соответствующей записей в БД
    @DisplayName("Отправка POST запроса платежа с валидно заполненным body и данными действующей карты")
    @Test
    public void checkPaymentValidCard() {
        Card card = CardFactory.getApprovedCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(payLink)
                .then()
                .statusCode(200);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals(1, orders.size());

        assertEquals(payments.get(0).getStatus().toLowerCase(), "approved");
        assertEquals(payments.get(0).getTransaction_id(), orders.get(0).getPayment_id());
        assertNull(orders.get(0).getCredit_id());

    }

    @DisplayName("Отправка POST запроса на кредит с валидно заполненным body и данными действующей карты")
    @Test
    public void checkCreditPaymentValidCard() {
        Card card = CardFactory.getApprovedCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(creditLink)
                .then()
                .statusCode(200);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(1, credits.size());
        assertEquals(1, orders.size());

        assertEquals(credits.get(0).getStatus().toLowerCase(), "approved");
        assertEquals(credits.get(0).getBank_id(), orders.get(0).getPayment_id());
        assertEquals(credits.get(0).getId(), orders.get(0).getCredit_id());

    }

    @DisplayName("Отправка POST запроса платежа с валидно заполненным body и данными declined карты")
    @Test
    public void checkPaymentDeclinedCard() {
        Card card = CardFactory.getDeclinedCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(payLink)
                .then()
                .statusCode(200);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals(1, orders.size());

        assertEquals(payments.get(0).getStatus().toLowerCase(), "declined");
        assertEquals(payments.get(0).getTransaction_id(), orders.get(0).getPayment_id());
        assertNull(orders.get(0).getCredit_id());

    }

    @DisplayName("Отправка POST запроса на кредит с валидно заполненным body и данными declined карты")
    @Test
    public void checkCreditPaymentDeclinedCard() {
        Card card = CardFactory.getDeclinedCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(creditLink)
                .then()
                .statusCode(200);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(1, credits.size());
        assertEquals(1, orders.size());

        assertEquals(credits.get(0).getStatus().toLowerCase(), "declined");
        assertEquals(credits.get(0).getBank_id(), orders.get(0).getPayment_id());
        assertEquals(credits.get(0).getId(), orders.get(0).getCredit_id());

    }

    @DisplayName("Отправка POST запроса на платеж с пустым body")
    @Test
    public void checkPaymentEmptyBody() {
        given()
                .spec(spec)
                .when()
                .post(payLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }

    @DisplayName("Отправка POST запроса на кредит с пустым body")
    @Test
    public void checkCreditEmptyBody() {
        given()
                .spec(spec)
                .when()
                .post(creditLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }

    @DisplayName("Отправка POST запроса платежа с пустым значением у атрибута number в body (остальные данные заполнены валидно)")
    @Test
    public void checkPaymentEmptyCardNumber() {
        Card card = CardFactory.getEmptyNumberCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(payLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }

    @DisplayName("Отправка POST запроса на кредит с пустым значением у атрибута number в body (остальные данные заполнены валидно)")
    @Test
    public void checkCreditPaymentEmptyCardNumber() {
        Card card = CardFactory.getEmptyNumberCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(creditLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }

    @DisplayName("Отправка POST запроса платежа с пустым значением у атрибута month в body (остальные данные заполнены валидно)")
    @Test
    public void checkPaymentEmptyMonth() {
        Card card = CardFactory.getEmptyMonthCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(payLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }

    @DisplayName("Отправка POST запроса на кредит с пустым значением у атрибута month в body (остальные данные заполнены валидно)")
    @Test
    public void checkCreditPaymentEmptyMonth() {
        Card card = CardFactory.getEmptyMonthCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(creditLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }

    @DisplayName("Отправка POST запроса платежа с пустым значением у атрибута year в body (остальные данные заполнены валидно)")
    @Test
    public void checkPaymentEmptyYear() {
        Card card = CardFactory.getEmptyYearValueCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(payLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }

    @DisplayName("Отправка POST запроса на кредит с пустым значением у атрибута year в body (остальные данные заполнены валидно)")
    @Test
    public void checkCreditPaymentEmptyYear() {
        Card card = CardFactory.getEmptyYearValueCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(creditLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }


    @DisplayName("Отправка POST запроса платежа с пустым значением у атрибута holder в body (остальные данные заполнены валидно)")
    @Test
    public void checkPaymentEmptyOwner() {
        Card card = CardFactory.getEmptyOwnerValueCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(payLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }

    @DisplayName("Отправка POST запроса на кредит с пустым значением у атрибута holder в body (остальные данные заполнены валидно)")
    @Test
    public void checkCreditPaymentEmptyOwner() {
        Card card = CardFactory.getEmptyOwnerValueCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(creditLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }


    @DisplayName("Отправка POST запроса платежа с пустым значением у атрибута cvc в body (остальные данные заполнены валидно)")
    @Test
    public void checkPaymentEmptyCVV() {
        Card card = CardFactory.getEmptyCVVValueCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(payLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }

    @DisplayName("Отправка POST запроса на кредит с пустым значением у атрибута cvc в body (остальные данные заполнены валидно)")
    @Test
    public void checkCreditPaymentEmptyCVV() {
        Card card = CardFactory.getEmptyCVVValueCard();
        var body = gson.toJson(card);
        given()
                .spec(spec)
                .body(body)
                .when()
                .post(creditLink)
                .then()
                .statusCode(400);

        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();
        List<OrderDto> orders = DbUtil.getOrderQueryResult();

        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());

    }


}
