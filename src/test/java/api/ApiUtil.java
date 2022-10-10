package api;

import card.Card;
import com.google.gson.Gson;
import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiUtil {

    private static final Gson gson = new Gson();
    private static final RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(8080)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL).build();

    public static void checkSuccessResponse(Card card, String url) {
        given()
                .spec(specification)
                .body(gson.toJson(card))
                .when()
                .post(url)
                .then()
                .statusCode(200);
    }

    public static void checkBadRequestResponse(Card card, String url) {
        given()
                .spec(specification)
                .body(gson.toJson(card))
                .when()
                .post(url)
                .then()
                .statusCode(400);
    }

    public static void checkBadRequestResponse(String url) {
        given()
                .spec(specification)
                .when()
                .post(url)
                .then()
                .statusCode(400);
    }

}
