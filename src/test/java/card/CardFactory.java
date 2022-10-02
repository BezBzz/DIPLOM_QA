package card;

import com.github.javafaker.Faker;

import java.time.LocalDate;

public class CardFactory {

    static String approvedNumber = "4444 4444 4444 4441";
    static String declinedNumber = "4444 4444 4444 4442";
    static Faker f = new Faker();

    public static Card getApprovedCard() {
        Card c = new Card();
        c.setNumber(approvedNumber);
        c.setMonth(String.valueOf(LocalDate.now().getMonth().getValue()));
        c.setYear(String.valueOf(LocalDate.now().getYear()).substring(2));
        c.setOwner(f.name().fullName());
        c.setCvc(f.numerify("###"));

        return c;
    }

    public static Card getDeclinedCard() {
        Card c = new Card();
        c.setNumber(declinedNumber);
        c.setMonth(String.valueOf(LocalDate.now().getMonth().getValue()));
        c.setYear(String.valueOf(LocalDate.now().getYear()).substring(2));
        c.setOwner(f.name().fullName());
        c.setCvc(f.numerify("###"));

        return c;
    }

}
