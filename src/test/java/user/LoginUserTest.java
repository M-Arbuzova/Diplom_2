package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest {
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
    @DisplayName("Логин пользователя с валидными данными")
    @Description("Проверка, что можно залогиниться с валидными данными")
    public void loginUserTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseLogin = UserApi.userLogin(user);
        responseLogin.assertThat().statusCode(SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Логин пользователя с неправильным email")
    @Description("Проверка, что можно нельзя залогиниться с неправильным email")
    public void loginUserWithWrongEmailTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        user.setEmail(user.getEmail() + "test");
        ValidatableResponse responseLogin = UserApi.userLogin(user);
        responseLogin.assertThat().statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and().body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин пользователя с неправильным паролем")
    @Description("Проверка, что можно нельзя залогиниться с неправильным email")
    public void loginUserWithWrongPasswordTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        user.setPassword(user.getPassword() + "test");
        ValidatableResponse responseLogin = UserApi.userLogin(user);
        responseLogin.assertThat().statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and().body("message", is("email or password are incorrect"));
    }
}
