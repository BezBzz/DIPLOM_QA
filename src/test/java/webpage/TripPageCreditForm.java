package webpage;

import card.Card;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class TripPageCreditForm {
    public static SelenideElement errorNotification = $x("//div[contains(@class, 'notification_status_error')]");
    public static SelenideElement successNotification = $x("//div[contains(@class, 'notification_status_ok')]");
    public static SelenideElement creditHeader = $x("//div/h3[text()='Кредит по данным карты']");
    public static SelenideElement creditPayForm = $x("//div/form");
    public static SelenideElement continueButton = $x("//span[text()='Продолжить']//ancestor::button");
    public static SelenideElement numberField = $x("//span[text()='Номер карты']//ancestor::div/span//input");
    public static SelenideElement monthField = $x("//span[text()='Месяц']//ancestor::div/span/span[1]/span//input");
    public static SelenideElement yearField = $x("//span[text()='Год']//ancestor::div/span/span[2]/span//input");
    public static SelenideElement ownerField = $x("//span[text()='Владелец']//ancestor::div/span/span[1]/span//input");
    public static SelenideElement cvvField = $x("//span[text()='CVC/CVV']//ancestor::div/span/span[2]/span//input");
    public static SelenideElement cardNumberNotification = $x("//span[text()='Номер карты']/..//span[@class='input__sub']");
    public static SelenideElement monthNotification = $x("//span[text()='Месяц']/..//span[@class='input__sub']");
    public static SelenideElement yearNotification = $x("//span[text()='Год']/..//span[@class='input__sub']");
    public static SelenideElement cvvNotification = $x("//span[text()='CVC/CVV']/..//span[@class='input__sub']");
    public static SelenideElement ownerNotification = $x("//span[text()='Владелец']/..//span[@class='input__sub']");

    public TripPageCreditForm() {
        creditHeader.shouldBe(visible);
        creditPayForm.shouldBe(visible);
        continueButton.shouldBe(visible);
    }

    public void payByCard(Card card) {
        numberField.setValue(card.getNumber());
        monthField.setValue(card.getMonth());
        yearField.setValue(card.getYear());
        ownerField.setValue(card.getOwner());
        cvvField.setValue(card.getCvc());
        continueButton.click();
    }

    public void checkSuccessNotification() {
        successNotification.should(Condition.visible, Duration.ofSeconds(20));
        errorNotification.shouldNot(Condition.visible, Duration.ofSeconds(20));
    }

    public void checkErrorNotification() {
        successNotification.shouldNot(Condition.visible, Duration.ofSeconds(20));
        errorNotification.should(Condition.visible, Duration.ofSeconds(20));
    }

    public void wrongFormatCardNotification() {
       cardNumberNotification.shouldBe(visible, text("Неверный формат"));
    }

    public void monthNeedToBeFilledNotification() {
        monthNotification.shouldBe(visible, text("Поле обязательно для заполнения"));
    }

    public void monthWrongDateNotification() {
        monthNotification.shouldBe(visible, text("Неверно указан срок действия карты"));
    }

    public void yearNeedToBeFilledNotification() {
        yearNotification.shouldBe(visible, text("Поле обязательно для заполнения"));
    }

    public void yearExpiredNotification() {
        yearNotification.shouldBe(visible, text("Истёк срок действия карты"));
    }

    public void yearWrongDateNotification() {
        yearNotification.shouldBe(visible, text("Неверно указан срок действия карты"));
    }

    public void cvvNeedToBeFilledNotification() {
        cvvNotification.shouldBe(visible, text("Поле обязательно для заполнения"));
    }

    public void cvvWrongFormatNotification() {
        cvvNotification.shouldBe(visible, text("Неверный формат"));
    }

    public void ownerNeedToBeFilledNotification() {
        ownerNotification.shouldBe(visible, text("Поле обязательно для заполнения"));
    }

    public void ownerWrongFormatNotification() {
        ownerNotification.shouldBe(visible, text("Неверный формат"));
    }
}