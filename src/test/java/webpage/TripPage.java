package webpage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class TripPage {

    private static final String payButton = "//div/button[1]";
    private static final String creditButton = "//div/button[2]";
    private static final String tripPreview = "//div[contains(@class, 'cardPreview')]";

    public TripPage() {
        $x(tripPreview).shouldBe(visible);
        $x(payButton).shouldBe(visible);
        $x(creditButton).shouldBe(visible);
    }

     public TripPagePayForm getPayWebpage() {
         $x(payButton).click();
         return new TripPagePayForm();
     }

    public TripPageCreditForm getCreditWebpage() {
        $x(creditButton).click();
        return new TripPageCreditForm();
    }

}
