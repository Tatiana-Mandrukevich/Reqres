package tests;

import com.google.gson.Gson;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import reqres_objects.ResourcesList;
import reqres_objects.User;
import reqres_objects.UsersList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTests {
    public static final String BASE_URL = "https://reqres.in/api/";
    SoftAssert softAssert = new SoftAssert();

    @Test(description = "Create User, check status code, 'name' field, 'job' field")
    public void postCreateUserTest() {
        User user = User.builder()
                        .name("morpheus")
                        .job("leader")
                        .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
        .when()
                .post(BASE_URL + "users")
        .then()
                .log().all()
                .body("name", equalTo("morpheus"),
                        "job", equalTo("leader"))
                .statusCode(201);
    }

    @Test(description = "Get list of users and check status code and all exists fields of the first user")
    public void getUsersListTest() {

        String body =
                given()
                        .log().all()
                        .when()
                        .get(BASE_URL + "users?page=2")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();

        UsersList usersList = new Gson().fromJson(body, UsersList.class);
        softAssert.assertEquals(usersList.getData().get(0).getId(), 7);
        softAssert.assertEquals(usersList.getData().get(0).getFirstName(), "Michael");
        softAssert.assertEquals(usersList.getData().get(0).getLastName(), "Lawson");
        softAssert.assertEquals(usersList.getData().get(0).getEmail(), "michael.lawson@reqres.in");
        softAssert.assertEquals(usersList.getData().get(0).getAvatar(), "https://reqres.in/img/faces/7-image.jpg");
        softAssert.assertAll();
    }

    @Test(description = "Get single user and check status code and all fields")
    public void getSingleUserTest() {
        given()
                .log().all()
                .when()
                .get(BASE_URL + "users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", equalTo(2),
                        "data.email", equalTo("janet.weaver@reqres.in"),
                        "data.first_name", equalTo("Janet"),
                        "data.last_name", equalTo("Weaver"),
                        "data.avatar", equalTo("https://reqres.in/img/faces/2-image.jpg"));
    }

    @Test(description = "Get single user not found and check status code and response body")
    public void getSingleUserNotFoundTest() {
        given()
                .log().all()
                .when()
                .get(BASE_URL + "users/23")
                .then()
                .log().all()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test(description = "Get list of resources and check status code and all exists fields of the first resource")
    public void getResourcesListTest() {

        String body =
                given()
                        .log().all()
                        .when()
                        .get(BASE_URL + "unknown")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();

        ResourcesList resourcesList = new Gson().fromJson(body, ResourcesList.class);
        softAssert.assertEquals(resourcesList.getData().get(0).getId(), 1);
        softAssert.assertEquals(resourcesList.getData().get(0).getName(), "cerulean");
        softAssert.assertEquals(resourcesList.getData().get(0).getYear(), 2000);
        softAssert.assertEquals(resourcesList.getData().get(0).getColor(), "#98B2D1");
        softAssert.assertEquals(resourcesList.getData().get(0).getPantoneValue(), "15-4020");
        softAssert.assertAll();
    }

    @Test(description = "Get single resource and check status code and all fields")
    public void getSingleResourceTest() {
        given()
                .log().all()
                .when()
                .get(BASE_URL + "unknown/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", equalTo(2),
                        "data.name", equalTo("fuchsia rose"),
                        "data.year", equalTo(2001),
                        "data.color", equalTo("#C74375"),
                        "data.pantone_value", equalTo("17-2031"));
    }

    @Test(description = "Get single resource not found and check status code and response body")
    public void getSingleResourceNotFoundTest() {
        given()
                .log().all()
                .when()
                .get(BASE_URL + "unknown/23")
                .then()
                .log().all()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test(description = "Update User, check status code, 'name' field, 'job' field")
    public void putUpdateUserTest() {
        User user = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .put(BASE_URL + "users/2")
                .then()
                .log().all()
                .body("name", equalTo("morpheus"),
                        "job", equalTo("zion resident"))
                .statusCode(200);
    }

    @Test(description = "Patch User, check status code, 'name' field, 'job' field")
    public void patchUpdateUserTest() {
        User user = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .log().all()
                .when()
                .patch(BASE_URL + "users/2")
                .then()
                .log().all()
                .body("name", equalTo("morpheus"),
                        "job", equalTo("zion resident"))
                .statusCode(200);
    }

    @Test(description = "Delete user and check status code and response body")
    public void deleteUserTest() {
        given()
                .log().all()
                .when()
                .delete(BASE_URL + "users/2")
                .then()
                .log().all()
                .statusCode(204)
                .body(equalTo(""));
    }

    @Test(description = "Register user, check status code, 'id' field, 'token' field")
    public void postRegisterSuccessfulTest() {
        User user = User.builder()
                .email(PropertyReader.getProperty("emailForRegister"))
                .password(PropertyReader.getProperty("passwordForRegister"))
                .build();

        given()
                .body(user)
                .header("Content-type", "application/json")
                .when()
                .post(BASE_URL + "register")
                .then()
                .log().all()
                .body("id", equalTo(4),
                        "token", notNullValue())
                .statusCode(200);
    }

    @Test(description = "Register user with missing password, check status code and response body")
    public void postRegisterUnsuccessfulTest() {
        User user = User.builder()
                .email("sydney@fife")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .when()
                .log().all()
                .post(BASE_URL + "register")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test(description = "Login user, check status code, 'token' field")
    public void postLoginSuccessfulTest() {
        User user = User.builder()
                .email(PropertyReader.getProperty("emailForLogin"))
                .password(PropertyReader.getProperty("passwordForLogin"))
                .build();

        given()
                .body(user)
                .header("Content-type", "application/json")
                .when()
                .post(BASE_URL + "login")
                .then()
                .log().all()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test(description = "Login user with missing password, check status code and response body")
    public void postLoginUnsuccessfulTest() {
        User user = User.builder()
                .email("peter@klaven")
                .build();

        given()
                .log().all()
                .body(user)
                .header("Content-type", "application/json")
                .when()
                .log().all()
                .post(BASE_URL + "login")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test(description = "Get delayed response, check status code and all exists fields of the first user")
    public void getDelayedResponseTest() {
        String body =
                given()
                        .log().all()
                        .when()
                        .get(BASE_URL + "users?delay=3")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();

        UsersList usersList = new Gson().fromJson(body, UsersList.class);
        softAssert.assertEquals(usersList.getData().get(0).getId(), 1);
        softAssert.assertEquals(usersList.getData().get(0).getFirstName(), "George");
        softAssert.assertEquals(usersList.getData().get(0).getLastName(), "Bluth");
        softAssert.assertEquals(usersList.getData().get(0).getEmail(), "george.bluth@reqres.in");
        softAssert.assertEquals(usersList.getData().get(0).getAvatar(), "https://reqres.in/img/faces/1-image.jpg");
        softAssert.assertAll();
    }
}