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

public class LoginUserTest {
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
    public void tearDown() {
        if (bearerToken != null) {
            userApi.deleteUser(bearerToken).statusCode(SC_ACCEPTED)
                    .and().body("message", is("User successfully removed")).log().all();
        }
    }
        @Test
        @DisplayName("Логин пользователя с валидными данными")
        @Description("Проверка, что можно залогиниться с валидными данными")
        public void loginUserTest () {
            response = userApi.userReg(user);
            bearerToken = response.extract().path("accessToken");
            response.assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
            ValidatableResponse responseLogin = userApi.userLogin(user);
            responseLogin.assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
        }
    @Test
    @DisplayName("Логин пользователя с неправильным email")
    @Description("Проверка, что можно нельзя залогиниться с неправильным email")
    public void loginUserWithWrongEmailTest () {
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
        user.setEmail(user.getEmail() + "test");
        ValidatableResponse responseLogin = userApi.userLogin(user);
        responseLogin.assertThat().statusCode(SC_UNAUTHORIZED)
                .body("success", is(false)).log().all()
                .and().body("message", is("email or password are incorrect")).log().all();
    }
    @Test
    @DisplayName("Логин пользователя с неправильным паролем")
    @Description("Проверка, что можно нельзя залогиниться с неправильным email")
    public void loginUserWithWrongPasswordTest () {
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
        user.setPassword(user.getPassword() + "test");
        ValidatableResponse responseLogin = userApi.userLogin(user);
        responseLogin.assertThat().statusCode(SC_UNAUTHORIZED)
                .body("success", is(false)).log().all()
                .and().body("message", is("email or password are incorrect")).log().all();
    }
    }
