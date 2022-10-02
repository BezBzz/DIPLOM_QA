package test.web;

import card.Card;
import card.CardFactory;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import db.DbUtil;
import db.dto.CreditDto;
import db.dto.OrderDto;
import db.dto.PayDto;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import webpage.TripPage;
import webpage.TripPagePayForm;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static webpage.TripPagePayForm.errorNotification;
import static webpage.TripPagePayForm.successNotification;

public class PayWebTest {

    private static TripPage page;
    private static final String url = "http://localhost:8080/";

    @BeforeAll
    public static void setupClass() {
        SelenideLogger.addListener("allure",
                new AllureSelenide()
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

        $x(successNotification).should(Condition.visible, Duration.ofSeconds(20));
        $x(errorNotification).shouldNot(Condition.visible, Duration.ofSeconds(20));

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

        $x(successNotification).shouldNot(Condition.visible, Duration.ofSeconds(20));
        $x(errorNotification).should(Condition.visible, Duration.ofSeconds(20));

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
}
