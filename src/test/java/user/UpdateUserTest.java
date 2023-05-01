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

public class UpdateUserTest {
    User user;
    UserNewData userNewData;
    private UserApi userApi;
    private String bearerToken;

    @Before
    public void setUp() {
        userApi = new UserApi();
        user = new UserGenerateData().getRandomUser();
        userNewData = new UserNewData();
    }

    @After
    public void tearDown() {
        if (bearerToken == null) return;
        userApi.deleteUser(bearerToken);
    }

    //смущает, что в тесте ниже получается изменить данные у пользователя, хотя не было входа в систему под его логин
    //правильно понимаю, что это баг?
    @Test
    @DisplayName("Изменение данных авторизованного пользователя")
    @Description("Проверка, что можно изменить данные, когда пользователь авторизован")
    public void updateUserTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseUpdate = userApi.updateDataUserWithAuth(userNewData.random(), bearerToken);
        responseUpdate.assertThat().statusCode(SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Изменение данных незалогиненного пользователя")
    @Description("Проверка, что нельзя изменить данные, когда пользователь не залогинен")
    public void updateUserWithoutAuthTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseUpdate = userApi.updateDataUserWithoutAuth(userNewData);
        responseUpdate.assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false))
                .and().body("message", is("You should be authorised"));
    }
}
