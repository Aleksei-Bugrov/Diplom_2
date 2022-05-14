package ru.yandex.praktikum.Order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.BaseSpec;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseSpec {
    public final String ORDER_PATH = BASE_URL + "orders";

    @Step("Создание заказа авторизованным пользователем")
    public ValidatableResponse createOrderWithAuthorization(String accessToken, Order order) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .body(order)
                .post(ORDER_PATH)
                .then();
    }

    @Step("Создание заказа не авторизованным пользователем")
    public ValidatableResponse createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(order)
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredient() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(BASE_URL + "ingredients")
                .then();
    }

    @Step("Получение списка заказов авторизованного пользователя")
    public ValidatableResponse getOrdersWithAuthorizationUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Получение списка заказов не авторизованного пользователя")
    public ValidatableResponse getOrdersWithoutAuthorizationUser() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
}
