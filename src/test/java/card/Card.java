package card;

import lombok.*;

@Value
public class Card {

    String number;
    String month;
    String year;
    String owner;
    String cvc;
}