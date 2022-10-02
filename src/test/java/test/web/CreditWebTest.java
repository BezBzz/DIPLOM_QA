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
import webpage.TripPageCreditForm;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static webpage.TripPagePayForm.errorNotification;
import static webpage.TripPagePayForm.successNotification;

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

        $x(successNotification).should(Condition.visible, Duration.ofSeconds(20));
        $x(errorNotification).shouldNot(Condition.visible, Duration.ofSeconds(20));

        List<OrderDto> orders = DbUtil.getOrderQueryResult();
        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();

        assertEquals(0, payments.size());
        assertEquals(1, credits.size());
        assertEquals(1, orders.size());

        CreditDto credit = credits.get(0);
        OrderDto order = orders.get(0);

        assertEquals(order.getCredit_id(), credit.getId());
        assertEquals("approved", credit.getStatus().toLowerCase());
        assertEquals(credit.getBank_id(), order.getPayment_id());

    }

    @Test
    @DisplayName("Заявка по карте со статусом DECLINED")
    public void testPayWithDeclinedCard() {
        TripPageCreditForm creditForm = page.getCreditWebpage();
        Card card = CardFactory.getApprovedCard();
        creditForm.payByCard(card);

        $x(successNotification).shouldNot(Condition.visible, Duration.ofSeconds(20));
        $x(errorNotification).should(Condition.visible, Duration.ofSeconds(20));

        List<OrderDto> orders = DbUtil.getOrderQueryResult();
        List<PayDto> payments = DbUtil.getPayQueryResult();
        List<CreditDto> credits = DbUtil.getCreditQueryResult();

        assertEquals(0, payments.size());
        assertEquals(1, credits.size());
        assertEquals(1, orders.size());

        CreditDto credit = credits.get(0);
        OrderDto order = orders.get(0);

        assertEquals(order.getCredit_id(), credit.getId());
        assertEquals("declined", credit.getStatus().toLowerCase());
        assertEquals(credit.getBank_id(), order.getPayment_id());

    }
}
