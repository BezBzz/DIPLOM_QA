package card;

import com.github.javafaker.Faker;

public class CardFactory {

    static String validNumber = "4444 4444 4444 4441";
    static String invalidNumber = "4444 4444 4444 4442";
    static Faker f = new Faker();

    public static Card getValidCard() {
        Card c = new Card();
        c.setNumber(validNumber);
        c.setMonth("12");
        c.setYear("2022");
        c.setHolder(f.name().fullName());
        c.setCvc(f.numerify("###"));

        return c;
    }

    public static Card getInvalidCard() {
        Card c = new Card();
        c.setNumber(invalidNumber);
        c.setMonth("12");
        c.setYear("2022");
        c.setHolder(f.name().fullName());
        c.setCvc(f.numerify("###"));

        return c;
    }

}
