package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Order.Order;
import ru.yandex.praktikum.Order.OrderClient;
import ru.yandex.praktikum.Users.User;
import ru.yandex.praktikum.Users.UserClient;

import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestGetUserOrders {
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
    @DisplayName("Получение списка заказов авторизованного пользователя")
    public void getOrdersWithAuthorizationUser() {
        userClient.loginUser(user);

        ValidatableResponse getIdOrder = orderClient.getIngredient();
        orderId = getIdOrder.extract().path("data[0]._id");

        order.setIngredients(Collections.singletonList(orderId));
        orderClient.createOrderWithAuthorization(accessToken, order);

        ValidatableResponse getOrdersUser = orderClient.getOrdersWithAuthorizationUser(accessToken);
        statusCode = getOrdersUser.extract().statusCode();
        fieldSuccess = getIdOrder.extract().path("success");
        List<String> orders = getOrdersUser.extract().path("orders");

        assertThat("Не удалось получить список заказов", statusCode, equalTo(SC_OK));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(true));
        assertThat("Список заказов пустой", orders, notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказов не авторизованного пользователя")
    public void getOrdersWithoutAuthorizationUser() {
        ValidatableResponse getIdOrder = orderClient.getIngredient();
        orderId = getIdOrder.extract().path("data[0]._id");

        order.setIngredients(Collections.singletonList(orderId));
        orderClient.createOrderWithAuthorization(accessToken, order);

        ValidatableResponse getOrdersUser = orderClient.getOrdersWithoutAuthorizationUser();
        statusCode = getOrdersUser.extract().statusCode();
        fieldSuccess = getOrdersUser.extract().path("success");
        message = getOrdersUser.extract().path("message");

        assertThat("Возвращается неверный статус код, при получении списка заказов не авторизованного пользователя", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
        assertThat("Ошибка в тексе ответа", message, equalTo("You should be authorised"));
    }
}
