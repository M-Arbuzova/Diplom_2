package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserApi;
import user.UserGenerateData;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateUserTest {
    ValidatableResponse response;
    User user;
    private UserApi userApi;
    private String bearerToken;

    @Before
    public void setUp(){
        userApi = new UserApi();
        user = new UserGenerateData().getRandomUser();
    }
    @After
    public void tearDown(){
        if (bearerToken == null) return;
        userApi.deleteUser(bearerToken);
    }
    @Test
    @DisplayName("Регистрации нового пользователя")
    @Description("Проверка, что можно создать нового пользователя с валидными значениями")
    public void createUserTest() {
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
    }
    @Test
    @DisplayName("Нельзя зарегистрироваться двух одинаковых пользователей")
    @Description("Проверка, что нельзя создать нового пользователя, если вводимый email уже есть в системе")
    public void createUserDuplicateTest() {
        ValidatableResponse responseUser1 = userApi.userReg(user);
        bearerToken = responseUser1.extract().path("accessToken");
        responseUser1.assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
        ValidatableResponse responseUser2 = userApi.userReg(user);
        responseUser2.assertThat().statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and().body("message", is("User already exists")).log().all();
    }
    @Test
    @DisplayName("Нельзя зарегистрироваться пользователя без email")
    @Description("Проверка, что нельзя создать нового пользователя, если не указать email")
    public void createUserWithoutEmailTest() {
        user.setEmail(null);
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and().body("message", is("Email, password and name are required fields")).log().all();
    }
    @Test
    @DisplayName("Нельзя зарегистрироваться пользователя без пароля")
    @Description("Проверка, что нельзя создать нового пользователя, если не указать пароль")
    public void createUserWithoutPasswordTest() {
        user.setPassword(null);
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and().body("message", is("Email, password and name are required fields")).log().all();
    }
    @Test
    @DisplayName("Нельзя зарегистрироваться пользователя без имени")
    @Description("Проверка, что нельзя создать нового пользователя, если не указать имя")
    public void createUserWithoutNameTest() {
        user.setName(null);
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and().body("message", is("Email, password and name are required fields")).log().all();
    }
}
