package webpage;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class TripPage {

    private static final SelenideElement payButton = $x("//div/button[1]");
    private static final SelenideElement creditButton = $x("//div/button[2]");
    private static final SelenideElement tripPreview = $x("//div[contains(@class, 'cardPreview')]");

    public TripPage() {
        tripPreview.shouldBe(visible);
        payButton.shouldBe(visible);
        creditButton.shouldBe(visible);
    }

     public TripPagePayForm getPayWebpage() {
         payButton.click();
         return new TripPagePayForm();
     }

    public TripPageCreditForm getCreditWebpage() {
        creditButton.click();
        return new TripPageCreditForm();
    }

}
