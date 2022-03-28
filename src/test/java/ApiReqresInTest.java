import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static uri.APIUri.*;

public class ApiReqresInTest extends BaseTest{

    @DisplayName("Получение списка пользователей GET \"LIST USERS\"")
    @Test
    void checkSuccessGetListTest(){
        String uri = LIST_USERS
                .replace("{page}", "2");

        Response response = given()
                .log().all()
                .when()
                .get(BASE_URL + uri)
                .then()
                .statusCode(200)
                .extract().response();

        assertThat(response.jsonPath().getString("data.id"))
                .as("id must be not null").isNotNull();
    }

    @DisplayName("Получение данных одного пользователя  \"SINGLE USER\"")
    @Test
    void checkSuccessSingleUserTest(){
        String uri = USER
                .replace("{user}", "2");

        Response response = given()
                .log().all()
                .contentType(JSON)
                .when()
                .get(BASE_URL + uri)
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();
        
        assertThat(response.jsonPath().getString("data.id"))
                .as("id must be not null").isNotNull();
    }

    @DisplayName("Получение ошибки пользователь не найден  \"USER NOT FOUND\"")
    @Test
    void singleUserNotFoundTest(){
        String uri = USER
                .replace("{user}", "23");

        given()
                .log().all()
                .contentType(JSON)
                .when()
                .get(BASE_URL + uri)
                .then()
                .log().all()
                .statusCode(404);
    }

    @DisplayName("проверка успешной регистрации  \"REGISTER - SUCCESSFUL\"")
    @Test
    void successfullyRegisterTest(){
        String body = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}";

        Response response = given()
                .log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .post(BASE_URL + REGISTER)
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        assertThat(response.jsonPath().getString("id"))
                .as("id must be not null").isNotNull();
        assertThat(response.jsonPath().getString("token"))
                .as("token must be not null").isNotNull();
    }

    @ParameterizedTest(name = "Проверка регистрации с ошибкой REGISTER - UNSUCCESSFUL(без поля {0})")
    @ValueSource(strings = {"email", "password"})
    void unsuccessfullyRegisterTest(String param){
        String body = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}"
                .replace(param, "/" + param);

        Response response = given()
                .log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .post(BASE_URL + REGISTER)
                .then()
                .log().all()
                .statusCode(400)
                .extract().response();

        assertThat(response.jsonPath().getString("error"))
                .as("id must be not null").isNotNull();
    }
}