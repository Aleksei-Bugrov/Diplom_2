package ru.yandex.praktikum.Users;

import io.qameta.allure.Step;
import ru.yandex.praktikum.BaseSpec;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseSpec {

    public final String PATH = BASE_URL + "auth/";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(PATH + "register")
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
            .spec(getBaseSpec())
            .header("Authorization", accessToken)
            .when()
            .delete(PATH + "user")
            .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(PATH + "login")
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse updateDataUser(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .body(user)
                .patch(PATH + "user")
                .then();
    }
}
