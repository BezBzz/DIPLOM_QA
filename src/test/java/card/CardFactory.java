package card;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.Locale;

public class CardFactory {

    static String approvedNumber = "4444 4444 4444 4441";
    static String declinedNumber = "4444 4444 4444 4442";
    static Faker f = new Faker();

    public static Card getApprovedCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getDeclinedCard() {
        Card c = new Card(declinedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getUnknownCard() {
        Card c = new Card("1234 1234 1234 1234",
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getEmptyNumberCard() {
        Card c = new Card("",
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getOneNumberCard() {
        Card c = new Card(f.numerify("#"),
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getZerosNumberCard() {
        Card c = new Card(f.numerify("0000 0000 0000 0000"),
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getFifteenNumberCard() {
        Card c = new Card(f.numerify("0000 0000 0000 000"),
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getEmptyMonthCard() {
        Card c = new Card(f.numerify(approvedNumber),
                "",
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getMonthValueBiggerTwelveCard() {
        Card c = new Card(f.numerify(approvedNumber),
                "13",
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getMonthZeroZeroValueCard() {
        Card c = new Card(f.numerify(approvedNumber),
                "00",
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getPreviousMonthValueCard() {
        Card c = new Card(f.numerify(approvedNumber),
                String.valueOf(LocalDate.now().getMonth().minus(1).getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getEmptyYearValueCard() {
        Card c = new Card(f.numerify(approvedNumber),
                String.valueOf(LocalDate.now().getMonth().getValue()),
                "",
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getYearZeroZeroValueCard() {
        Card c = new Card(f.numerify(approvedNumber),
                String.valueOf(LocalDate.now().getMonth().getValue()),
                "00",
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getPreviousYearValueCard() {
        Card c = new Card(f.numerify(approvedNumber),
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear() - 1).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getYearPlusFiveValueCard() {
        Card c = new Card(f.numerify(approvedNumber),
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear() + 5).substring(2),
                f.name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getEmptyCVVValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                "");

        return c;
    }

    public static Card getOneNumberCVVValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("#"));

        return c;
    }

    public static Card getTwoNumberCVVValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                f.numerify("##"));

        return c;
    }

    public static Card getTripleZeroCVVValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName(),
                "000");

        return c;
    }

    public static Card getCapsOwnerValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().fullName().toUpperCase(),
                f.numerify("###"));

        return c;
    }

    public static Card getOwnerWithDashSymbolValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                "NIKITA IVANOVS-PETROVS",
                f.numerify("###"));

        return c;
    }

    public static Card getEmptyOwnerValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                "",
                f.numerify("###"));

        return c;
    }

    public static Card getEmptyNameOwnerValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.name().lastName(),
                f.numerify("###"));

        return c;
    }

    public static Card getCyrillicSymbolsOwnerValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                new Faker(new Locale("ru-RU")).name().fullName(),
                f.numerify("###"));

        return c;
    }

    public static Card getNumberSymbolsOwnerValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                f.numerify("##### #########"),
                f.numerify("###"));

        return c;
    }

    public static Card getSymbolsOwnerValueCard() {
        Card c = new Card(approvedNumber,
                String.valueOf(LocalDate.now().getMonth().getValue()),
                String.valueOf(LocalDate.now().getYear()).substring(2),
                "%%%^^^  $#@**^!@#",
                f.numerify("###"));

        return c;
    }
}
