package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Users.User;
import ru.yandex.praktikum.Users.UserClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class TestWhenLoginUser {

    private User user;
    private UserClient userClient;
    private String accessToken;
    private int statusCode;
    private boolean fieldSuccess;

    @Before
    public void setUp() {
        user = User.getAllRandom();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void validLoginUser() {
        ValidatableResponse isUserCreated = userClient.create(user);
        accessToken = isUserCreated.extract().path("accessToken");

        ValidatableResponse isUserLogin = userClient.loginUser(user);
        statusCode = isUserLogin.extract().statusCode();
        fieldSuccess = isUserLogin.extract().path("success");

        assertThat("Пользователь не прошел авторизацию", statusCode, equalTo(SC_OK));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(true));
    }

    @Test
    @DisplayName("Авторизация пользователя без пароля")
    public void userLoginWithoutPass() {
        ValidatableResponse isUserCreated = userClient.create(user);
        accessToken = isUserCreated.extract().path("accessToken");

        user.setPassword("");

        ValidatableResponse isUserLogin = userClient.loginUser(user);
        statusCode = isUserLogin.extract().statusCode();
        fieldSuccess = isUserLogin.extract().path("success");

        assertThat("Возвращается неверный статус код, при авторизации без заполнения обязательного поля", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
    }

    @Test
    @DisplayName("Авторизация пользователя без логина")
    public void userLoginWithoutEmail() {
        ValidatableResponse isUserCreated = userClient.create(user);
        accessToken = isUserCreated.extract().path("accessToken");

        user.setEmail("");

        ValidatableResponse isUserLogin = userClient.loginUser(user);
        statusCode = isUserLogin.extract().statusCode();
        fieldSuccess = isUserLogin.extract().path("success");

        assertThat("Возвращается неверный статус код, при авторизации без заполнения обязательного поля", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
    }
}
