package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Users.User;
import ru.yandex.praktikum.Users.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestWhenCreatingUser {

    private UserClient userClient;
    private String accessToken;
    private int statusCode;
    private String message;
    private boolean fieldSuccess;

    @Before
    public void setUp() {userClient = new UserClient();}

    @After
    public void tearDown(){
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создали пользователя с рандомными данными")
    public void userBeCreatedWithValidData() {
        User user = User.getAllRandom();

        ValidatableResponse isUserCreated = userClient.create(user);
        statusCode = isUserCreated.extract().statusCode();
        accessToken = isUserCreated.extract().path("accessToken");
        fieldSuccess = isUserCreated.extract().path("success");

        assertThat("Пользователь не создан, возвращается неверный статус код", statusCode, equalTo(SC_OK));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(true));
    }

    @Test
    @DisplayName("Пользователь уже был создан")
    public void createAnExistingUser() {
        User firstUser = User.getAllRandom();
        User secondUser = new User(firstUser.email, firstUser.password, firstUser.name);

        ValidatableResponse isFirstUserCreated = userClient.create(firstUser);
        int statusCodeFirstUser = isFirstUserCreated.extract().statusCode();
        accessToken = isFirstUserCreated.extract().path("accessToken");

        ValidatableResponse isSecondUserNotCreated = userClient.create(secondUser);
        int statusCodeSecondUser = isSecondUserNotCreated.extract().statusCode();
        fieldSuccess = isSecondUserNotCreated.extract().path("success");
        message = isSecondUserNotCreated.extract().path("message");

        assertThat("Пользователь один не создан, возвращается неверный статус код", statusCodeFirstUser, equalTo(SC_OK));
        assertThat("Возвращается неверный статус код, при создании уже существующего пользователя", statusCodeSecondUser, equalTo(SC_FORBIDDEN));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
        assertThat("Ошибка в тексте ответа", message, equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    public void creatingUserWithoutEmail() {
        User user = User.getRandomNoEmail();

        ValidatableResponse isUserNoCreated = userClient.create(user);
        statusCode = isUserNoCreated.extract().statusCode();
        fieldSuccess = isUserNoCreated.extract().path("success");
        message = isUserNoCreated.extract().path("message");

        assertThat("Возвращается неверный статус код, при создании пользователя без обязательного поля", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
        assertThat("Ошибка в тексте ответа", message, equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    public void creatingUserWithoutPassword() {
        User user = User.getRandomNoPassword();

        ValidatableResponse isUserNoCreated = userClient.create(user);
        statusCode = isUserNoCreated.extract().statusCode();
        fieldSuccess = isUserNoCreated.extract().path("success");
        message = isUserNoCreated.extract().path("message");

        assertThat("Возвращается неверный статус код, при создании пользователя без обязательного поля", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
        assertThat("Ошибка в тексте ответа", message, equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    public void creatingUserWithoutName() {
        User user = User.getRandomNoName();

        ValidatableResponse isUserNoCreated = userClient.create(user);
        statusCode = isUserNoCreated.extract().statusCode();
        fieldSuccess = isUserNoCreated.extract().path("success");
        message = isUserNoCreated.extract().path("message");

        assertThat("Возвращается неверный статус код, при создании пользователя без обязательного поля", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Возвращается неверное значение поля - success", fieldSuccess, equalTo(false));
        assertThat("Ошибка в тексте ответа", message, equalTo("Email, password and name are required fields"));
    }
}
