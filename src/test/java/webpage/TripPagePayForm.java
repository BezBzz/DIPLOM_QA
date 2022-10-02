package webpage;

import card.Card;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class TripPagePayForm {
    public static String errorNotification = "//div[contains(@class, 'notification_status_error')]";
    public static String successNotification = "//div[contains(@class, 'notification_status_ok')]//div[text()='Операция одобрена Банком.']";
    public static String payHeader = "//div/h3[text()='Оплата по карте']";
    public static String payForm = "//div/form";
    public static String continueButton = "//span[text()='Продолжить']//ancestor::button";
    public static String numberField = "//span[text()='Номер карты']//ancestor::div/span//input";
    public static String monthField = "//span[text()='Месяц']//ancestor::div/span/span[1]/span//input";
    public static String yearField = "//span[text()='Год']//ancestor::div/span/span[2]/span//input";
    public static String ownerField = "//span[text()='Владелец']//ancestor::div/span/span[1]/span//input";
    public static String cvvField = "//span[text()='CVC/CVV']//ancestor::div/span/span[2]/span//input";

    public TripPagePayForm() {
        $x(payHeader).shouldBe(visible);
        $x(payForm).shouldBe(visible);
        $x(continueButton).shouldBe(visible);
    }

    public void payByCard(Card card) {
        $x(numberField).setValue(card.getNumber());
        $x(monthField).setValue(card.getMonth());
        $x(yearField).setValue(card.getYear());
        $x(ownerField).setValue(card.getOwner());
        $x(cvvField).setValue(card.getCvc());
        $x(continueButton).click();
    }

}
