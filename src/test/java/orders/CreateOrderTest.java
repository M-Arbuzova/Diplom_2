package orders;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserApi;
import user.UserGenerateData;;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {
    ValidatableResponse response;
    User user;
    Order order;
    UserApi userApi;
    OrderApi orderApi;
    private String bearerToken;

    @Before
    public void setUp(){
        userApi = new UserApi();
        user = new UserGenerateData().getRandomUser();
        orderApi = new OrderApi();
        order = new Order();
    }
    @After
    public void tearDown(){
        if (bearerToken == null) return;
        userApi.deleteUser(bearerToken);
    }
    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Проверка, что можно создать заказ, если пользователь авторизован")
    public void createOrderTest() {
        fillIngredientList();
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        UserApi.userLogin(user);
        ValidatableResponse responseCreateOrder = orderApi.createOrder(order, bearerToken);
        responseCreateOrder.assertThat().statusCode(SC_OK).body("success", is(true)).and().log().all();
    }
    //следующий тест баг? на сайте нет возможности создать заказ без логина, да и тут если получится создать заказ
//    то за кем он закрепляется?
    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    @Description("Проверка, что можно создать заказ, если пользователь не авторизован")
    public void createOrderWithoutAuthTest() {
        fillIngredientList();
        ValidatableResponse responseCreateOrder = orderApi.createOrderWithoutAuth(order);
        responseCreateOrder.assertThat().statusCode(SC_OK).body("success", is(true)).and().log().all();
    }
    @Test
    @DisplayName("Создание заказа без добавления ингредиентов авторизованным пользователем")
    @Description("Проверка, что нельзя создать заказ, если не добавить ингредиенты")
    public void createOrderWithoutIngredientTest() {
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = orderApi.createOrder(order, bearerToken);
        responseCreateOrder.assertThat().statusCode(SC_BAD_REQUEST).body("success", is(false))
                .and().body("message", is("Ingredient ids must be provided")).log().all();
    }
    @Test
    @DisplayName("Создание заказа c неверным хешем ингредиентов авторизованным пользователем")
    @Description("Проверка, что нельзя создать заказ, если указать неверный хеш ингредиентов")
    public void createOrderWithWrongHashIngredientTest() {
        fillWrongHashIngredientList();
        response = userApi.userReg(user);
        bearerToken = response.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = orderApi.createOrder(order, bearerToken);
        responseCreateOrder.assertThat().statusCode(SC_INTERNAL_SERVER_ERROR).log().all();
    }


    private void fillIngredientList() {
        ValidatableResponse validatableResponse = OrderApi.getAllIngredients();
        List<String> list = validatableResponse.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(4));
        ingredients.add(list.get(2));
        ingredients.add(list.get(0));
    }
    private void fillWrongHashIngredientList() {
        ValidatableResponse validatableResponse = OrderApi.getAllIngredients();
        List<String> list = validatableResponse.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(4).repeat(1));
        ingredients.add(list.get(2).repeat(2));
        ingredients.add(list.get(0));
    }
}
