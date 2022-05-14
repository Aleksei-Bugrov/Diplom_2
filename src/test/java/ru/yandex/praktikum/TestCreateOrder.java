package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Order.Order;
import ru.yandex.praktikum.Order.OrderClient;
import ru.yandex.praktikum.Users.User;
import ru.yandex.praktikum.Users.UserClient;

import java.util.Collections;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestCreateOrder {

    private User user;
    private UserClient userClient;
    private OrderClient orderClient;
    private Order order;
    private String accessToken;
    private int statusCode;
    private String message;
    private boolean fieldSuccess;
    private String orderId;

    @Before
    public void setUp() {
        user = User.getAllRandom();
        userClient = new UserClient();
        orderClient = new OrderClient();
        order = new Order();
        ValidatableResponse isUserCreation = userClient.create(user);
        accessToken = isUserCreation.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void createOrderWithAuthorization() {
        userClient.loginUser(user);

        ValidatableResponse getIdOrder = orderClient.getIngredient();
        orderId = getIdOrder.extract().path("data[0]._id");

        order.setIngredients(Collections.singletonList(orderId));
        ValidatableResponse setOrder = orderClient.createOrderWithAuthorization(accessToken, order);
        statusCode = setOrder.extract().statusCode();
        fieldSuccess = setOrder.extract().path("success");

        assertThat("Не удалось создать заказ", statusCode, equalTo(SC_OK));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем")
    public void createOrderWithoutAuthorization() {
        ValidatableResponse getIdOrder = orderClient.getIngredient();
        orderId = getIdOrder.extract().path("data[0]._id");

        order.setIngredients(Collections.singletonList(orderId));
        ValidatableResponse setOrder = orderClient.createOrderWithoutAuthorization(order);
        statusCode = setOrder.extract().statusCode();
        fieldSuccess = setOrder.extract().path("success");

        assertThat("Не удалось создать заказ", statusCode, equalTo(SC_OK));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем без ингредиентов")
    public void createOrderWithAuthorizationWithoutIngredients() {
        userClient.loginUser(user);

        ValidatableResponse setOrder = orderClient.createOrderWithAuthorization(accessToken, order);
        statusCode = setOrder.extract().statusCode();
        fieldSuccess = setOrder.extract().path("success");
        message = setOrder.extract().path("message");

        assertThat("Возвращается неверный статус код, при создании заказа без ингредиентов", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
        assertThat("Ошибка в тексте ответа", message, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем без ингредиентов")
    public void createOrderWithoutAuthorizationWithoutIngredients() {
        ValidatableResponse setOrder = orderClient.createOrderWithoutAuthorization(order);
        statusCode = setOrder.extract().statusCode();
        fieldSuccess = setOrder.extract().path("success");
        message = setOrder.extract().path("message");

        assertThat("Возвращается неверный статус код, при создании заказа без ингредиентов", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
        assertThat("Ошибка в тексте ответа", message, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем")
    public void createOrderWithAuthorizationInvalidHash() {
        userClient.loginUser(user);

        orderId = RandomStringUtils.randomAlphabetic(24);

        order.setIngredients(Collections.singletonList(orderId));
        ValidatableResponse setOrder = orderClient.createOrderWithAuthorization(accessToken, order);
        statusCode = setOrder.extract().statusCode();

        assertThat("Возвращается неверный статус код, при отправке неправильного хеша", statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));
    }
}
