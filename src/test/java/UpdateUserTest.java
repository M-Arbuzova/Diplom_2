import client.BurgerSpec;
import client.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserApi;
import user.UserGenerateData;
import user.UserNewData;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class UpdateUserTest {
    ValidatableResponse response;
    User user;
    UserNewData userNewData;
    private UserApi userApi;
    private String bearerToken;

    @Before
    public void setUp(){
        userApi = new UserApi();
        user = new UserGenerateData().getRandomUser();
        userNewData = new UserNewData();
    }
    @After
    public void tearDown() {
        if (bearerToken != null) {
            userApi.deleteUser(bearerToken);
        }
    }
    @Test
    @DisplayName("Изменение данных залогиненного пользователя")
    @Description("Проверка, что можно изменить данные, когда пользователь залогинен")
    public void updateUserTest () {
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(SC_OK).body("success", is(true));
        ValidatableResponse responseUpdate = userApi.updateDataUserWithAuth(userNewData, bearerToken);
        responseUpdate.assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
    }
    @Test
    @DisplayName("Изменение данных незалогиненного пользователя")
    @Description("Проверка, что нельзя изменить данные, когда пользователь не залогинен")
    public void updateUserWithoutAuthTest () {
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(SC_OK).body("success", is(true));
        ValidatableResponse responseUpdate = userApi.updateDataUserWithoutAuth(userNewData);
        responseUpdate.assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false))
                .and().body("message", is("You should be authorised")).log().all();
    }
}
