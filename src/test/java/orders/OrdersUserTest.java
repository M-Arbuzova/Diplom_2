package orders;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import orders.Order;
import orders.OrderApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserApi;
import user.UserGenerateData;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class OrdersUserTest {
    ValidatableResponse response;
    User user;
    Order order;
    private UserApi userApi;
    private OrderApi orderApi;
    private String bearerToken;

    @Before
    public void setUp() {
        userApi = new UserApi();
        user = new UserGenerateData().getRandomUser();
        orderApi = new OrderApi();
        order = new Order();
    }

    @After
    public void tearDown() {
        if (bearerToken == null) return;
        userApi.deleteUser(bearerToken);
    }
    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Проверка, что можно получить список заказов, когда пользователь авторизован")
    public void checkOrdersUserWithAuthTest() {
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
        UserApi.userLogin(user);
        OrderApi.createOrder(order, bearerToken);
        ValidatableResponse responseOrdersUser = orderApi.getUserOrdersWithAuth(bearerToken);
        responseOrdersUser.assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
    }
    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Проверка, что нельзя получить список заказов, когда пользователь не авторизован")
    public void checkOrdersUserWithoutAuthTest() {
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(SC_OK).body("success", is(true)).log().all();
        ValidatableResponse responseOrders = orderApi.getUserOrdersWithoutAuth();
        responseOrders.assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false))
                .and().body("message",is("You should be authorised")).log().all();
    }
}
