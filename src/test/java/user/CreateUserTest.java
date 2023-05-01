package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;

public class CreateUserTest {
    User user;
    private UserApi userApi;
    private String bearerToken;

    @Before
    public void setUp() {
        userApi = new UserApi();
        user = new UserGenerateData().getRandomUser();
    }

    @After
    public void tearDown() {
        if (bearerToken == null) return;
        userApi.deleteUser(bearerToken);
    }

    @Test
    @DisplayName("Регистрации нового пользователя")
    @Description("Проверка, что можно создать нового пользователя с валидными значениями")
    public void createUserTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        responseReg.assertThat().statusCode(SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться двух одинаковых пользователей")
    @Description("Проверка, что нельзя создать нового пользователя, если вводимый email уже есть в системе")
    public void createUserDuplicateTest() {
        ValidatableResponse responseUser1 = userApi.userReg(user);
        bearerToken = responseUser1.extract().path("accessToken");
        responseUser1.assertThat().statusCode(SC_OK).body("success", is(true));
        ValidatableResponse responseUser2 = userApi.userReg(user);
        responseUser2.assertThat().statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and().body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться пользователя без email")
    @Description("Проверка, что нельзя создать нового пользователя, если не указать email")
    public void createUserWithoutEmailTest() {
        user.setEmail(null);
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        responseReg.assertThat().statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and().body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться пользователя без пароля")
    @Description("Проверка, что нельзя создать нового пользователя, если не указать пароль")
    public void createUserWithoutPasswordTest() {
        user.setPassword(null);
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        responseReg.assertThat().statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and().body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться пользователя без имени")
    @Description("Проверка, что нельзя создать нового пользователя, если не указать имя")
    public void createUserWithoutNameTest() {
        user.setName(null);
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        responseReg.assertThat().statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and().body("message", is("Email, password and name are required fields"));
    }
}
