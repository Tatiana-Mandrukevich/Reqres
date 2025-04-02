package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class OnlinerTest {

    @Test
    public void getCurrencyUSDTest() {
        given()
        .when()
                .get("https://www.onliner.by/sdapi/kurs/api/bestrate?currency=USD&type=nbrb")
        .then()
                .statusCode(200);
    }

    @Test
    public void getCurrencyEURTest() {
        given()
                .log().all()
        .when()
                .get("https://www.onliner.by/sdapi/kurs/api/bestrate?currency=EUR&type=nbrb")
        .then()
                .log().all()
                .body("amount", equalTo("3,3882"))
                .statusCode(200);
    }

    @Test
    public void getCurrencyRUBTest() {
        given()
        .when()
                .get("https://www.onliner.by/sdapi/kurs/api/bestrate?currency=RUB&type=nbrb")
        .then()
                .statusCode(200);
    }
}