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

public class TestChangingUserData {

    private User user;
    private UserClient userClient;
    private String accessToken;
    private int statusCode;
    private String message;
    private boolean fieldSuccess;

    @Before
    public void setUp() {
        user = User.getAllRandom();
        userClient = new UserClient();
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
    @DisplayName("Изменение имени авторизованного пользователя")
    public void ChangingUserNameWithAuthorization() {
        userClient.loginUser(user);

        user.setName("123456");

        ValidatableResponse changingData = userClient.updateDataUser(user, accessToken);
        statusCode = changingData.extract().statusCode();
        fieldSuccess = changingData.extract().path("success");
        assertThat("Не удалось изменить данные", statusCode, equalTo(SC_OK));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(true));
    }

    @Test
    @DisplayName("Изменение логина авторизованного пользователя")
    public void ChangingUserEmailWithAuthorization() {
        userClient.loginUser(user);

        user.setEmail("abcde@test.ru");

        ValidatableResponse changingData = userClient.updateDataUser(user, accessToken);
        statusCode = changingData.extract().statusCode();
        fieldSuccess = changingData.extract().path("success");
        assertThat("Не удалось изменить данные", statusCode, equalTo(SC_OK));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(true));
    }

    @Test
    @DisplayName("Изменение пароля авторизованного пользователя")
    public void ChangingUserPasswordWithAuthorization() {
        userClient.loginUser(user);

        user.setPassword("336699");

        ValidatableResponse changingData = userClient.updateDataUser(user, accessToken);
        statusCode = changingData.extract().statusCode();
        fieldSuccess = changingData.extract().path("success");
        assertThat("Не удалось изменить данные", statusCode, equalTo(SC_OK));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(true));
    }

    @Test
    @DisplayName("Изменение пароля не авторизованного пользователя")
    public void ChangingUserPasswordWithoutAuthorization() {
        userClient.loginUser(user);

        user.setPassword("336699");

        ValidatableResponse changingData = userClient.updateDataUser(user, "");
        statusCode = changingData.extract().statusCode();
        fieldSuccess = changingData.extract().path("success");
        message = changingData.extract().path("message");
        assertThat("Возвращается неверный статус код, при изменении данных не авторизованного пользователя", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
        assertThat("Ошибка в тексте ответа", message, equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение логина не авторизованного пользователя")
    public void ChangingUserEmailWithoutAuthorization() {
        userClient.loginUser(user);

        user.setEmail("abcde@test.ru");

        ValidatableResponse changingData = userClient.updateDataUser(user, "");
        statusCode = changingData.extract().statusCode();
        fieldSuccess = changingData.extract().path("success");
        message = changingData.extract().path("message");
        assertThat("Возвращается неверный статус код, при изменении данных не авторизованного пользователя", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
        assertThat("Ошибка в тексте ответа", message, equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение имени не авторизованного пользователя")
    public void ChangingUserNameWithoutAuthorization() {
        userClient.loginUser(user);

        user.setName("123456");

        ValidatableResponse changingData = userClient.updateDataUser(user, "");
        statusCode = changingData.extract().statusCode();
        fieldSuccess = changingData.extract().path("success");
        message = changingData.extract().path("message");
        assertThat("Возвращается неверный статус код, при изменении данных не авторизованного пользователя", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
        assertThat("Ошибка в тексте ответа", message, equalTo("You should be authorised"));
    }
}
